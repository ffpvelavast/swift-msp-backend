/** Request DTO for the TeamEventGetList operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.rex.commodis.model.enums.TeamEventSortBy;
import com.rex.commodis.model.enums.TeamEventStatusEnum;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamEventGetListRequestDto {
    @Parameter(hidden = true)
    private Long rexUserId;

    @Schema(description = "Filter by event status")
    private TeamEventStatusEnum status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "Filter events from this date (format: yyyy-MM-dd). Must be used together with toDate.")
    private LocalDate fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "Filter events up to this date (format: yyyy-MM-dd). Must be used together with fromDate.")
    private LocalDate toDate;

    @Schema(description = "Partial search on event title (case-insensitive)")
    private String eventTitle;

    @Schema(description = "Partial search on submitted by user name (case-insensitive)")
    private String submittedBy;

    @Schema(description = "Partial search on project number")
    private String projectNumber;

    @Schema(description = "Page number (1-based)",defaultValue = "1")
    @Builder.Default
    private Integer pageNumber = 1;

    @Schema(description = "Number of records per page",defaultValue = "10")
    @Builder.Default
    private Integer pageSize = 10;

    @Schema(description = "Field used for sorting results")
    private TeamEventSortBy sortBy;

    @Schema(
            description = "Sort direction",
            allowableValues = {"ASC", "DESC"},
            defaultValue = "DESC"
    )
    @Builder.Default
    private String direction = "DESC";
}
