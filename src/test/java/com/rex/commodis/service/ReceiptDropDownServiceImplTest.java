package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.BookableEventDto;
import com.rex.commodis.dto.receipts.ExpenseCategoryDropDownDto;
import com.rex.commodis.dto.receipts.ExpenseReportDropDownDto;
import com.rex.commodis.dto.receipts.IssuanceRegionDropDownDto;
import com.rex.commodis.dto.receipts.ReceiptDropDownResponseDto;
import com.rex.commodis.repository.events.BookableEventProjection;
import com.rex.commodis.repository.events.TeamEventRepository;
import com.rex.commodis.repository.expenses.ExpenseReportProjection;
import com.rex.commodis.repository.expenses.ExpenseReportRepository;
import com.rex.commodis.repository.receipts.IssuanceRegionProjection;
import com.rex.commodis.repository.receipts.IssuanceRegionRepository;
import com.rex.commodis.repository.taxation.TaxCategoryProjection;
import com.rex.commodis.repository.taxation.TaxCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptDropDownServiceImplTest {

    @Mock private ExpenseReportRepository expenseReportRepository;
    @Mock private IssuanceRegionRepository issuanceRegionRepository;
    @Mock private TeamEventRepository teamEventRepository;
    @Mock private TaxCategoryRepository taxCategoryRepository;
    @Mock private RexUserService rexUserService;

    @InjectMocks
    private ReceiptDropDownServiceImpl service;

    private static final Long   REX_USER_ID = 1L;
    private static final Long   PLATFORM_ID = 10L;
    private static final String LOCALE      = "en_US";

    @BeforeEach
    void setUp() {
        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        // Default each collaborator to "empty list". Tests override only the repo they're inspecting.
        lenient().when(teamEventRepository.findBookableEvents(REX_USER_ID)).thenReturn(List.of());
        lenient().when(expenseReportRepository.findExpenseReports(REX_USER_ID)).thenReturn(List.of());
        lenient().when(issuanceRegionRepository.findIssuanceRegions(PLATFORM_ID, LOCALE)).thenReturn(List.of());
        lenient().when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE)).thenReturn(List.of());
    }

    // ── Happy path ─────────────────────────────────────────────────────────────

    @Test
    void getDropDowns_allFourListsPopulated_returnsFullyMappedResponse() {
        BookableEventProjection event    = bookableEvent("TEAM_EVENT", 1L, "Q2 Offsite", true);
        ExpenseReportProjection report   = expenseReport(11L, "Travel Report",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 31));
        IssuanceRegionProjection region  = issuanceRegion(5, "DE", "Germany", "EU country");
        TaxCategoryProjection category   = taxCategory(3L, "Meals", "Meals & entertainment");

        when(teamEventRepository.findBookableEvents(REX_USER_ID)).thenReturn(List.of(event));
        when(expenseReportRepository.findExpenseReports(REX_USER_ID)).thenReturn(List.of(report));
        when(issuanceRegionRepository.findIssuanceRegions(PLATFORM_ID, LOCALE)).thenReturn(List.of(region));
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE)).thenReturn(List.of(category));

        ReceiptDropDownResponseDto response = service.getDropDowns(REX_USER_ID, PLATFORM_ID);

        assertThat(response.getEvents()).hasSize(1);
        assertThat(response.getExpenseReports()).hasSize(1);
        assertThat(response.getIssuanceRegions()).hasSize(1);
        assertThat(response.getExpenseCategories()).hasSize(1);
        verify(rexUserService).getLocale(REX_USER_ID);
    }

    @Test
    void getDropDowns_passesLocaleAndPlatformIdToRegionAndCategoryRepos() {
        service.getDropDowns(REX_USER_ID, PLATFORM_ID);

        verify(issuanceRegionRepository).findIssuanceRegions(PLATFORM_ID, LOCALE);
        verify(taxCategoryRepository).findTaxCategories(PLATFORM_ID, LOCALE);
        verify(teamEventRepository).findBookableEvents(REX_USER_ID);
        verify(expenseReportRepository).findExpenseReports(REX_USER_ID);
    }

    @Test
    void getDropDowns_allRepositoriesReturnEmpty_returnsAllEmptyLists() {
        ReceiptDropDownResponseDto response = service.getDropDowns(REX_USER_ID, PLATFORM_ID);

        assertThat(response.getEvents()).isEmpty();
        assertThat(response.getExpenseReports()).isEmpty();
        assertThat(response.getIssuanceRegions()).isEmpty();
        assertThat(response.getExpenseCategories()).isEmpty();
    }

    // ── Mapping integrity ──────────────────────────────────────────────────────

    @Test
    void getDropDowns_bookableEventMapping_copiesAllFields() {
        BookableEventProjection e1 = bookableEvent("TEAM_EVENT", 1L, "Offsite", true);
        BookableEventProjection e2 = bookableEvent("CORPORATE_EVENT", 2L, "Conference", false);
        when(teamEventRepository.findBookableEvents(REX_USER_ID)).thenReturn(List.of(e1, e2));

        List<BookableEventDto> events = service.getDropDowns(REX_USER_ID, PLATFORM_ID).getEvents();

        assertThat(events).hasSize(2);
        assertThat(events.get(0).getEventSource()).isEqualTo("TEAM_EVENT");
        assertThat(events.get(0).getEventId()).isEqualTo(1L);
        assertThat(events.get(0).getEventTitle()).isEqualTo("Offsite");
        assertThat(events.get(0).getInheritParticipants()).isTrue();
        assertThat(events.get(1).getEventSource()).isEqualTo("CORPORATE_EVENT");
        assertThat(events.get(1).getInheritParticipants()).isFalse();
    }

    @Test
    void getDropDowns_expenseReportMapping_copiesAllFieldsIncludingDates() {
        LocalDate from    = LocalDate.of(2024, 1, 1);
        LocalDate through = LocalDate.of(2024, 6, 30);
        ExpenseReportProjection report = expenseReport(11L, "Q1 Travel", from, through);
        when(expenseReportRepository.findExpenseReports(REX_USER_ID)).thenReturn(List.of(report));

        List<ExpenseReportDropDownDto> reports =
                service.getDropDowns(REX_USER_ID, PLATFORM_ID).getExpenseReports();

        assertThat(reports).hasSize(1);
        assertThat(reports.get(0).getId()).isEqualTo(11L);
        assertThat(reports.get(0).getExpenseReportTitle()).isEqualTo("Q1 Travel");
        assertThat(reports.get(0).getReportDateFrom()).isEqualTo(from);
        assertThat(reports.get(0).getReportDateThrough()).isEqualTo(through);
    }

    @Test
    void getDropDowns_issuanceRegionMapping_copiesAllFields() {
        IssuanceRegionProjection r1 = issuanceRegion(5, "DE", "Germany", "EU");
        IssuanceRegionProjection r2 = issuanceRegion(7, "FR", "France", null);
        when(issuanceRegionRepository.findIssuanceRegions(PLATFORM_ID, LOCALE))
                .thenReturn(List.of(r1, r2));

        List<IssuanceRegionDropDownDto> regions =
                service.getDropDowns(REX_USER_ID, PLATFORM_ID).getIssuanceRegions();

        assertThat(regions).hasSize(2);
        assertThat(regions.get(0).getIssuanceRegionId()).isEqualTo(5);
        assertThat(regions.get(0).getRegionCode()).isEqualTo("DE");
        assertThat(regions.get(0).getDisplayText()).isEqualTo("Germany");
        assertThat(regions.get(0).getDescription()).isEqualTo("EU");
        assertThat(regions.get(1).getDescription()).isNull();
    }

    // ── Tax category displaytext branches ──────────────────────────────────────

    @Test
    void getDropDowns_taxCategoryWithValidDisplaytext_setsDisplaytextInDto() {
        TaxCategoryProjection cat = taxCategory(3L, "Meals", "Meals & Entertainment");
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE)).thenReturn(List.of(cat));

        ExpenseCategoryDropDownDto category =
                service.getDropDowns(REX_USER_ID, PLATFORM_ID).getExpenseCategories().get(0);

        assertThat(category.getTaxCategoryId()).isEqualTo(3L);
        assertThat(category.getDescription()).isEqualTo("Meals");
        assertThat(category.getDisplaytext()).isEqualTo("Meals & Entertainment");
    }

    @Test
    void getDropDowns_taxCategoryWithNullDisplaytext_omitsCategoryFromDto() {
        TaxCategoryProjection cat = taxCategory(3L, "Meals", null);
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE)).thenReturn(List.of(cat));

        List<ExpenseCategoryDropDownDto> categories =
                service.getDropDowns(REX_USER_ID, PLATFORM_ID).getExpenseCategories();

        assertThat(categories).isEmpty();
    }

    @Test
    void getDropDowns_taxCategoryWithBlankDisplaytext_omitsCategoryFromDto() {
        TaxCategoryProjection cat = taxCategory(3L, "Meals", "   ");
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE)).thenReturn(List.of(cat));

        List<ExpenseCategoryDropDownDto> categories =
                service.getDropDowns(REX_USER_ID, PLATFORM_ID).getExpenseCategories();

        assertThat(categories).isEmpty();
    }

    @Test
    void getDropDowns_mixedDisplaytextValues_keepsOnlyCategoriesWithDisplaytext() {
        TaxCategoryProjection c1 = taxCategory(1L, "Cat1", "Display1");
        TaxCategoryProjection c2 = taxCategory(2L, "Cat2", null);
        TaxCategoryProjection c3 = taxCategory(3L, "Cat3", "");
        when(taxCategoryRepository.findTaxCategories(PLATFORM_ID, LOCALE))
                .thenReturn(List.of(c1, c2, c3));

        List<ExpenseCategoryDropDownDto> categories =
                service.getDropDowns(REX_USER_ID, PLATFORM_ID).getExpenseCategories();

        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getTaxCategoryId()).isEqualTo(1L);
        assertThat(categories.get(0).getDisplaytext()).isEqualTo("Display1");
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private BookableEventProjection bookableEvent(String source, Long id, String title, Boolean inherit) {
        BookableEventProjection p = mock(BookableEventProjection.class);
        when(p.getEventSource()).thenReturn(source);
        when(p.getEventId()).thenReturn(id);
        when(p.getEventTitle()).thenReturn(title);
        when(p.getInheritParticipants()).thenReturn(inherit);
        return p;
    }

    private ExpenseReportProjection expenseReport(Long id, String title, LocalDate from, LocalDate through) {
        ExpenseReportProjection p = mock(ExpenseReportProjection.class);
        when(p.getId()).thenReturn(id);
        when(p.getReportTitle()).thenReturn(title);
        when(p.getReportDateFrom()).thenReturn(from);
        when(p.getReportDateThrough()).thenReturn(through);
        return p;
    }

    private IssuanceRegionProjection issuanceRegion(Integer id, String code, String display, String description) {
        IssuanceRegionProjection p = mock(IssuanceRegionProjection.class);
        when(p.getIssuanceRegionId()).thenReturn(id);
        when(p.getRegionCode()).thenReturn(code);
        when(p.getDisplayText()).thenReturn(display);
        when(p.getDescription()).thenReturn(description);
        return p;
    }

    private TaxCategoryProjection taxCategory(Long id, String description, String displaytext) {
        TaxCategoryProjection p = mock(TaxCategoryProjection.class);
        lenient().when(p.getTaxCategoryId()).thenReturn(id);
        lenient().when(p.getDescription()).thenReturn(description);
        lenient().when(p.getDisplaytext()).thenReturn(displaytext);
        return p;
    }
}
