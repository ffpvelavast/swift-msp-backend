package com.rex.commodis.controller;

import com.rex.commodis.model.TranslationClient;
import com.rex.commodis.repository.TranslationClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/platform-translations")
@CrossOrigin(origins = "*")
public class PlatformTranslationController {

    private final TranslationClientRepository repository;

    public PlatformTranslationController(TranslationClientRepository repository) {
        this.repository = repository;
    }

    record FlatTranslationRow(Long id, String component, String key, String translations, LocalDateTime createdAt) {}

    /**
     * All client-specific translations for a platform (flat).
     * GET /api/platform-translations/{platformId}/flat
     */
    @GetMapping("/{platformId}/flat")
    public ResponseEntity<List<FlatTranslationRow>> getFlat(@PathVariable Long platformId) {
        List<FlatTranslationRow> result = repository.findByPlatformId(platformId).stream()
                .map(t -> new FlatTranslationRow(t.getId(), t.getComponent(), t.getKey(),
                        t.getTranslations(), t.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(result);
    }

    /**
     * Client-specific translations for a single component (flat).
     * GET /api/platform-translations/{platformId}/flat/{component}
     */
    @GetMapping("/{platformId}/flat/{component}")
    public ResponseEntity<List<FlatTranslationRow>> getFlatByComponent(
            @PathVariable Long platformId,
            @PathVariable String component) {
        List<FlatTranslationRow> result = repository.findByPlatformIdAndComponent(platformId, component).stream()
                .map(t -> new FlatTranslationRow(t.getId(), t.getComponent(), t.getKey(),
                        t.getTranslations(), t.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(result);
    }
}
