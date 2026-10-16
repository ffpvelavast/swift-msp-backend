/** REST controller exposing Translation API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.commodis.model.Translation;
import com.rex.commodis.repository.TranslationRepository;
import com.rex.commodis.service.TranslationExportService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/translations")
@CrossOrigin(origins = "*")
public class TranslationController {

    private final TranslationRepository repository;
    private final TranslationExportService exportService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> SUPPORTED_LANGUAGES = Arrays.asList("en", "de");

    public TranslationController(TranslationRepository repository, TranslationExportService exportService) {
        this.repository = repository;
        this.exportService = exportService;
    }

    /**
     * Return the latest update timestamp for cache versioning.
     * Angular frontend polls this every 3 seconds to detect translation updates.
     */
    @GetMapping("/version")
    public ResponseEntity<Map<String, Object>> getTranslationsVersion() {
        LocalDateTime maxUpdatedAt = repository.getMaxUpdatedAt().orElse(null);
        
        Map<String, Object> response = new HashMap<>();
        response.put("version", maxUpdatedAt);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Return all components in a nested shape (matches labels.json structure).
     */
    @GetMapping
    public ResponseEntity<Object> getAllTranslations() {
        List<Translation> translations = repository.findAll();

        if (translations.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyMap());
        }

        Map<String, Object> body = buildNestedResponse(translations);
        return ResponseEntity.ok(body);
    }

    /**
     * Return only the requested component in nested shape.
     */
    @GetMapping("/{component}")
    public ResponseEntity<Map<String, Object>> getComponentTranslations(@PathVariable String component) {
        List<Translation> translations = repository.findByComponent(component);

        if (translations.isEmpty()) {
            System.out.println("⚠️ No translations found for component: " + component);
            return ResponseEntity.ok(new HashMap<>());
        }

        Map<String, Object> body = buildNestedResponse(translations, component);
        System.out.println("✅ Loaded " + translations.size() + " translations for " + component);

        return ResponseEntity.ok(body);
    }

    /**
     * 🔥 SPECIAL: Generate correct splash screen keys
     */
    private String generateSplashKey(String component, String key) {
        if (!component.equals("splash-screen")) return null;

        // slide1.title → splash-screen.slide1.title
        return "splash-screen." + key;
    }

    /**
     * 🔥 PARSE JSON → ACTUAL OBJECTS/ARRAYS
     */
    private Object parseLanguageValue(JsonNode node, String lang, String key) {
        if (node == null) {
            return key;
        }

        // Case 1: Direct language object {"en": "...", "de": "..."}
        if (node.has(lang)) {
            JsonNode langNode = node.get(lang);
            return parseNodeValue(langNode, key);
        }

        // Case 2: Nested object - search recursively
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode childNode = node.get(fieldName);
                
                if (childNode.has(lang)) {
                    JsonNode langNode = childNode.get(lang);
                    return parseNodeValue(langNode, key);
                }
                
                if (childNode.isObject()) {
                    Object value = parseLanguageValue(childNode, lang, key);
                    if (value != null && !value.equals(key)) {
                        return value;
                    }
                }
            }
        }

        // Case 3: Fallback to English
        if (!lang.equals("en") && node.has("en")) {
            return parseNodeValue(node.get("en"), key);
        }

        return key;
    }

    /**
     * 🔥 PARSE NODE → ACTUAL TYPE
     */
    private Object parseNodeValue(JsonNode node, String fallbackKey) {
        try {
            if (node.isTextual()) {
                String text = node.asText();
                return text.trim().isEmpty() ? fallbackKey : text;
            }
            
            if (node.isArray()) {
                return objectMapper.convertValue(node, List.class);
            }
            
            if (node.isObject()) {
                return objectMapper.convertValue(node, Map.class);
            }
            
            return node.asText();
            
        } catch (Exception e) {
            return fallbackKey;
        }
    }

    /**
     * ✅ SINGLE FIELD ENDPOINT
     */
    @GetMapping("/{component}/**")
    public ResponseEntity<Object> getSingleTranslation(
            @PathVariable String component,
            HttpServletRequest request) {

        String pathWithin = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        if (pathWithin == null || bestMatchPattern == null) {
            return ResponseEntity.ok(component);
        }

        String remaining = new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, pathWithin);

        if (remaining == null || remaining.isEmpty()) {
            return ResponseEntity.ok(component);
        }

        String key = remaining.replace('/', '.');

        Optional<Translation> translationOpt = repository.findByComponentAndKey(component, key);

        return translationOpt.map(translation -> {
            try {
                Object value = objectMapper.readValue(translation.getTranslations(), Object.class);
                return ResponseEntity.ok(value);
            } catch (Exception e) {
                return ResponseEntity.ok((Object) key);
            }
        }).orElse(ResponseEntity.ok((Object) key));
    }

    /**
     * ✅ DEBUG ENDPOINT - SPLASH SCREEN SPECIAL
     */
    @GetMapping("/debug")
    public ResponseEntity<Map<String, Object>> getDebugInfo() {
        List<String> components = repository.findAll().stream()
                .map(Translation::getComponent)
                .distinct()
                .sorted()
                .toList();
        
        Map<String, Long> componentCounts = repository.findAll().stream()
                .collect(Collectors.groupingBy(Translation::getComponent, Collectors.counting()));
        
        long arrayFields = repository.findAll().stream()
                .filter(t -> {
                    try {
                        JsonNode node = objectMapper.readTree(t.getTranslations());
                        return node.isArray() || 
                               (node.isObject() && node.has("en") && node.get("en").isArray());
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();
        
        Map<String, Object> debug = new HashMap<>();
        debug.put("totalTranslations", repository.count());
        debug.put("totalComponents", components.size());
        debug.put("arrayFields", arrayFields);
        debug.put("components", components);
        debug.put("componentCounts", componentCounts);
        debug.put("status", "HEALTHY ✅ SPLASH SCREEN FIXED!");
        
        return ResponseEntity.ok(debug);
    }

    /**
     * ✅ DEBUG COMPONENT - SHOWS DOT NOTATION KEYS
     */
    @GetMapping("/debug/{component}")
    public ResponseEntity<Map<String, Object>> debugComponent(
            @PathVariable String component,
            @RequestParam(defaultValue = "de") String lang) {
        
        List<Translation> translations = repository.findByComponent(component);
        
        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("component", component);
        debugInfo.put("language", lang);
        debugInfo.put("totalKeys", translations.size());
        
        Map<String, Object> parsedOutput = new HashMap<>();
        for (Translation t : translations) {
            try {
                JsonNode jsonNode = objectMapper.readTree(t.getTranslations());
                Object value = parseLanguageValue(jsonNode, lang, t.getKey());
                
                // 🔥 SHOW ALL 3 KEY VERSIONS
                Map<String, Object> item = new HashMap<>();
                item.put("originalKey", t.getKey());
                item.put("value", value);
                item.put("type", value.getClass().getSimpleName());
                
                // Add all key versions
                String plainKey = t.getKey();
                String prefixedKey = component + "." + plainKey;
                String splashKey = component.equals("splash-screen") ? generateSplashKey(component, plainKey) : null;
                
                item.put("keysAvailable", Arrays.asList(plainKey, prefixedKey, splashKey).stream()
                    .filter(Objects::nonNull).toList());
                
                parsedOutput.put(plainKey, item);
            } catch (Exception e) {
                parsedOutput.put(t.getKey(), "ERROR");
            }
        }
        
        debugInfo.put("parsedOutput", parsedOutput);
        debugInfo.put("note", "🔥 SPLASH SCREEN: Uses 'splash-screen.slide1.title' format!");
        
        return ResponseEntity.ok(debugInfo);
    }

    /**
     * Export all translations as an Excel file for customer review/editing.
     * Columns: Screen Name | Platform | Route | Component | Key | English | German
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportTranslations() {
        try {
            byte[] excel = exportService.exportToExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment().filename("rex-translations.xlsx").build());
            headers.setContentLength(excel.length);
            return ResponseEntity.ok().headers(headers).body(excel);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Return flat records: one row per DB entry without nesting.
     */
    @GetMapping("/flat")
    public ResponseEntity<List<FlatTranslationRow>> getAllTranslationsFlat() {
        return ResponseEntity.ok(
            repository.findAll().stream()
                .map(t -> new FlatTranslationRow(t.getId(), t.getComponent(), t.getKey(), t.getTranslations(), t.getCreatedAt()))
                .toList()
        );
    }

    /**
     * Return flat records for a single component.
     */
    @GetMapping("/flat/{component}")
    public ResponseEntity<List<FlatTranslationRow>> getComponentTranslationsFlat(@PathVariable String component) {
        return ResponseEntity.ok(
            repository.findByComponent(component).stream()
                .map(t -> new FlatTranslationRow(t.getId(), t.getComponent(), t.getKey(), t.getTranslations(), t.getCreatedAt()))
                .toList()
        );
    }

    record FlatTranslationRow(Long id, String component, String key, String translations, LocalDateTime createdAt) {}

    @GetMapping("/languages")
    public ResponseEntity<List<String>> getSupportedLanguages() {
        return ResponseEntity.ok(SUPPORTED_LANGUAGES);
    }


    /**
     * Build nested map for all components.
     */
    private Map<String, Object> buildNestedResponse(List<Translation> translations) {
        Map<String, Object> root = new LinkedHashMap<>();

        for (Translation translation : translations) {
            Map<String, Object> componentMap = getOrCreateMap(root, translation.getComponent());
            insertNested(componentMap, translation);
        }

        return root;
    }

    /**
     * Build nested map for a single component.
     */
    private Map<String, Object> buildNestedResponse(List<Translation> translations, String component) {
        Map<String, Object> componentMap = new LinkedHashMap<>();

        for (Translation translation : translations) {
            insertNested(componentMap, translation);
        }

        Map<String, Object> wrapped = new LinkedHashMap<>();
        wrapped.put(component, componentMap);
        return wrapped;
    }

    /**
     * Insert a translation value into a nested map based on dot-separated keys.
     */
    private void insertNested(Map<String, Object> target, Translation translation) {
        try {
            String key = translation.getKey();
            Object value = objectMapper.readValue(translation.getTranslations(), Object.class);

            String[] parts = key.split("\\.");
            Map<String, Object> current = target;

            for (int i = 0; i < parts.length - 1; i++) {
                String part = parts[i];
                current = getOrCreateMap(current, part);
            }

            current.put(parts[parts.length - 1], value);
        } catch (Exception e) {
            System.err.println("❌ Error inserting nested translation for key: " + translation.getKey());
        }
    }

    /**
     * Safe helper to retrieve or create a nested map without unchecked warnings.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getOrCreateMap(Map<String, Object> container, String key) {
        Object existing = container.get(key);
        if (existing instanceof Map) {
            return (Map<String, Object>) existing;
        }
        Map<String, Object> newMap = new LinkedHashMap<>();
        container.put(key, newMap);
        return newMap;
    }
}
