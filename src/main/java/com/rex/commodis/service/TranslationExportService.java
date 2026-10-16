package com.rex.commodis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.commodis.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationExportService {

    private final TranslationRepository translationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private record ScreenInfo(String screenName, String platform, String route) {}

    private static final Map<String, ScreenInfo> SCREEN_MAP = Map.ofEntries(
        Map.entry("global-login",                       new ScreenInfo("Login",                     "Both",    "/login")),
        Map.entry("login-component",                    new ScreenInfo("Login (Desktop)",            "Desktop", "/login-desktop")),
        Map.entry("dashboard-screen-component",         new ScreenInfo("Dashboard",                  "Mobile",  "/dashboard")),
        Map.entry("dashboard-desktop",                  new ScreenInfo("Dashboard (Desktop)",        "Desktop", "/dashboard-desktop")),
        Map.entry("add-expense-component",              new ScreenInfo("Add Expense",                "Mobile",  "/add-expense")),
        Map.entry("create-expense-report-screen",       new ScreenInfo("Create Expense Report",      "Mobile",  "/create-expense-report")),
        Map.entry("summary-expense-screen",             new ScreenInfo("Expense Summary",            "Mobile",  "/summary-expense")),
        Map.entry("congratulations-screen",             new ScreenInfo("Congratulations",            "Mobile",  "/congratulations")),
        Map.entry("manage-receipts",                    new ScreenInfo("Manage Receipts",            "Desktop", "/manage-receipts")),
        Map.entry("workspace-receipts",                 new ScreenInfo("Workspace Receipts",         "Desktop", "/workspace-receipts")),
        Map.entry("receipt-details",                    new ScreenInfo("Receipt Details",            "Desktop", "/receipts/:id")),
        Map.entry("reports-component",                  new ScreenInfo("Expense Reports",            "Desktop", "/reports-desktop")),
        Map.entry("notification-screen",                new ScreenInfo("Notifications",              "Mobile",  "/notifications")),
        Map.entry("add-deputy-screen",                  new ScreenInfo("Add Deputy",                 "Mobile",  "/add-deputy")),
        Map.entry("deputy-profile-screen",              new ScreenInfo("Deputy Profile",             "Mobile",  "/deputy-profile")),
        Map.entry("manager-profile-screen",             new ScreenInfo("Manager Profile",            "Mobile",  "/settings")),
        Map.entry("user-profile",                       new ScreenInfo("User Profile",               "Desktop", "/profile-desktop")),
        Map.entry("add-beneficiaries-component",        new ScreenInfo("Add Beneficiaries",          "Mobile",  "/add-beneficiaries")),
        Map.entry("add-internal-beneficiary-component", new ScreenInfo("Add Internal Beneficiary",   "Mobile",  "/add-internal-beneficiary")),
        Map.entry("add-external-beneficiary-component", new ScreenInfo("Add External Beneficiary",   "Mobile",  "/add-external-beneficiary")),
        Map.entry("co-workers-component",               new ScreenInfo("Co-Workers",                 "Mobile",  "/co-workers")),
        Map.entry("navbar-desktop",                     new ScreenInfo("Navigation Bar",             "Desktop", "N/A")),
        Map.entry("trip-users",                         new ScreenInfo("Trip / Event Users",         "Desktop", "/events"))
    );

    public byte[] exportToExcel() throws IOException {
        // Returns [component, key, translations] — avoids ORM mapping issues with optional columns
        List<Object[]> rows = translationRepository.findAllForExport();

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Translations");

            CellStyle headerStyle = buildHeaderStyle(workbook);
            CellStyle[] rowStyles = buildAlternatingRowStyles(workbook);

            // Header row
            Row header = sheet.createRow(0);
            String[] columns = {"Screen Name", "Platform", "Route", "Component", "Key", "English", "German"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowIdx = 1;
            int colorToggle = 0;
            String lastComponent = null;

            for (Object[] r : rows) {
                String component    = (String) r[0];
                String key          = (String) r[1];
                String translations = (String) r[2];

                if (!component.equals(lastComponent)) {
                    colorToggle = (colorToggle + 1) % 2;
                    lastComponent = component;
                }

                ScreenInfo info = SCREEN_MAP.getOrDefault(component,
                    new ScreenInfo(component, "Unknown", "Unknown"));

                Row row = sheet.createRow(rowIdx++);
                CellStyle style = rowStyles[colorToggle];

                writeCell(row, 0, info.screenName(), style);
                writeCell(row, 1, info.platform(), style);
                writeCell(row, 2, info.route(), style);
                writeCell(row, 3, component, style);
                writeCell(row, 4, key, style);
                writeCell(row, 5, extractLang(translations, "en"), style);
                writeCell(row, 6, extractLang(translations, "de"), style);
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
                // Add some padding
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
            }

            // Freeze header row and enable auto-filter
            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, rowIdx - 1, 0, columns.length - 1));

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private String extractLang(String json, String lang) {
        try {
            JsonNode node = objectMapper.readTree(json);
            if (node.has(lang)) {
                JsonNode val = node.get(lang);
                return val.isTextual() ? val.asText() : val.toString();
            }
        } catch (Exception ignored) {}
        return "";
    }

    private void writeCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private CellStyle buildHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle[] buildAlternatingRowStyles(Workbook wb) {
        CellStyle light = wb.createCellStyle();
        light.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        light.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        light.setVerticalAlignment(VerticalAlignment.CENTER);
        light.setWrapText(false);

        CellStyle shaded = wb.createCellStyle();
        shaded.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        shaded.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        shaded.setVerticalAlignment(VerticalAlignment.CENTER);
        shaded.setWrapText(false);

        return new CellStyle[]{light, shaded};
    }
}
