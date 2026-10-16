/** Service implementation for Receipt business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import java.util.List;

import com.rex.commodis.dto.receipts.CreateReceiptResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.model.ProjectLeader;
import com.rex.commodis.repository.ProjectLeaderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rex.commodis.dto.receipts.ReceiptEventRequestDto;
import com.rex.commodis.dto.receipts.CreateReceiptRequestDto;
import com.rex.commodis.model.receipts.Receipt;
import com.rex.commodis.repository.receipts.ReceiptRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptNumberGeneratorService receiptNumberGeneratorService;
    private final ReceiptHistoryService receiptHistoryService;
    private final ReceiptFileService receiptFileService;
    private final ReceiptToBeneficiaryService receiptToBeneficiaryService;
    private final ReceiptToGuestService receiptToGuestService;
    private final ReceiptToCorporateEventService receiptToCorporateEventService;
    private final ReceiptToTeamEventService receiptToTeamEventService;
    private final ReceiptDetailsService receiptDetailsService;
    private final ProjectLeaderRepository projectLeaderRepository;

    @Override
    @Transactional
    public CreateReceiptResponseDto createReceipt(CreateReceiptRequestDto createReceiptRequestDto,
                                                  Long platformId,
                                                  Long rexUserId,
                                                  List<MultipartFile> receiptFiles) {

        // 1. Create record in receipts
        Long receiptId = createAndSaveReceipt(createReceiptRequestDto, platformId, rexUserId);

        // 2. Create record in receipt_history
        receiptHistoryService.createReceiptHistory(receiptId, rexUserId);

        Long projectNumberId = createReceiptRequestDto.getProjectNumberId();

        List<Long> approverIds = projectLeaderRepository.findByProjectNumberId(projectNumberId).stream()
                .map(ProjectLeader::getRexUserId)
                .toList();

        if (approverIds.isEmpty()) {
            throw new BadRequestException("No approvers found for given project");
        }

        receiptDetailsService.createApprovers(receiptId, approverIds);
        // 3. Create record in receipt_files
        receiptFileService.createReceiptFile(receiptId, platformId, receiptFiles);

        // 4. Load Beneficiary and Guest Ids into receipt_to_beneficiary, receipt_to_guest
        receiptToBeneficiaryService.saveBeneficiaries(
                receiptId,
                createReceiptRequestDto.getBeneficiaryIds()
        );

        receiptToGuestService.saveGuests(
                receiptId,
                createReceiptRequestDto.getGuestIds()
        );

        // 5. Load Event data into receipt_to_corporate_event OR receipt_to_team_event
        ReceiptEventRequestDto receiptEventRequestDto = createReceiptRequestDto.getReceiptEvent();

        if(receiptEventRequestDto != null
                && receiptEventRequestDto.getEventSource() != null
                && !receiptEventRequestDto.getEventSource().isEmpty()
                && receiptEventRequestDto.getEventId() != 0){
            switch(receiptEventRequestDto.getEventSource()){
                case "CORPORATE_EVENT":
                    receiptToCorporateEventService.saveCorporateEvent(
                            receiptId,
                            createReceiptRequestDto.getReceiptEvent().getEventId()
                    );
                    break;

                case "TEAM_EVENT":
                    receiptToTeamEventService.saveTeamEvent(
                            receiptId,
                            createReceiptRequestDto.getReceiptEvent().getEventId()
                    );
                    break;

                default:
                    throw new BadRequestException("Event Source Name does not match");
            }
        }
        
        return new CreateReceiptResponseDto(receiptId);
    }
    
    public boolean existisByProjectNumber(Long projectNumberId){
        return receiptRepository.existsByProjectNumberId(projectNumberId);

    }

    //HELPER METHODS

    private Long createAndSaveReceipt(CreateReceiptRequestDto createReceiptRequestDto,
                                      Long platformId,
                                      Long rexUserId) {

        Receipt receipt = new Receipt();

        //Generate and Save unique receiptNumber
        String receiptNumber = receiptNumberGeneratorService.generateUniqueReceiptCode();
        receipt.setReceiptNumber(receiptNumber);

        receipt.setPlatformId(platformId);
        receipt.setRexUserId(rexUserId);
        receipt.setExpenseReportId(createReceiptRequestDto.getExpenseReportId());
        receipt.setIssuanceRegionId(createReceiptRequestDto.getIssuanceRegionId());
        receipt.setReceiptTitle(createReceiptRequestDto.getReceiptTitle());
        receipt.setReceiptDate(createReceiptRequestDto.getReceiptDate());
        receipt.setReceiptAmount(createReceiptRequestDto.getReceiptAmount());
        receipt.setProjectNumberId(createReceiptRequestDto.getProjectNumberId());
        receipt.setReceiptCurrency(createReceiptRequestDto.getReceiptCurrency());
        receipt.setGratuity(createReceiptRequestDto.getGratuity());
        receipt.setAmountNetReducedVat(createReceiptRequestDto.getAmountNetReducedVat());
        receipt.setAmountNetFullVat(createReceiptRequestDto.getAmountNetFullVat());
        receipt.setVatRateReduced(createReceiptRequestDto.getVatRateReduced());
        receipt.setVatRateFull(createReceiptRequestDto.getVatRateFull());
        receipt.setDeductTeamBalance(
                createReceiptRequestDto.getDeductTeamBalance() != null ? createReceiptRequestDto.getDeductTeamBalance() : false
        );
        receipt.setTaxReasonClientId(createReceiptRequestDto.getTaxReasonClientId());

        Receipt dbReceipt = receiptRepository.save(receipt);
        return dbReceipt.getId();
    }

}