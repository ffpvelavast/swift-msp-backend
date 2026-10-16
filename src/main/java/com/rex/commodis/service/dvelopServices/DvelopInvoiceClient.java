/** DTO representing the DvelopInvoiceClient data structure received from the D-velop document management webhook. */
package com.rex.commodis.service.dvelopServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.commodis.configuration.DvelopApiProperties;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferResultDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransfersResponseDto;
import com.rex.commodis.service.invoiceServices.InvoicePayloadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class DvelopInvoiceClient {

    private final RestClient restClient;
    private final DvelopApiProperties apiProperties;

    private final InvoicePayloadService invoicePayloadService;
    private final ObjectMapper objectMapper;

    public DvelopInvoiceClient(RestClient.Builder restClientBuilder,
                               DvelopApiProperties apiProperties,
                               InvoicePayloadService invoicePayloadService,
                               ObjectMapper objectMapper) {
        this.invoicePayloadService = invoicePayloadService;
        this.objectMapper = objectMapper;
        this.restClient = restClientBuilder.baseUrl(apiProperties.getBaseUrl()).build();
        this.apiProperties = apiProperties;
    }


    public InvoiceTransfersResponseDto fetchTransfers() {
        log.info("Requesting invoices from d.velop");

        log.info("The overall URL called is: {}{}?integration_key={}",
                apiProperties.getBaseUrl(),
                apiProperties.getTransfersPath(),
                apiProperties.getIntegrationKey());

        String rawResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiProperties.getTransfersPath())
                        .queryParam("integration_key", apiProperties.getIntegrationKey())
                        .build())
                .retrieve()
                .body(String.class);

        log.info("Response returned from D.velop: {}", rawResponse);

        try{
            InvoiceTransfersResponseDto response = objectMapper.readValue(
                    rawResponse,
                    InvoiceTransfersResponseDto.class
            );

            if(response != null &&
                    response.getTransfers() != null &&
                    !response.getTransfers().isEmpty()){
                invoicePayloadService.saveTransfer(rawResponse);
            }

            return response;

        } catch (Exception e){
            throw new RuntimeException("Failed to deserialize transfers response", e);
        }
    }

    public void reportSuccess(String callbackUrl) {

        restClient.post()
                .uri(callbackUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new InvoiceTransferResultDto(true, null))
                .retrieve()
                .toBodilessEntity();

    }
}
