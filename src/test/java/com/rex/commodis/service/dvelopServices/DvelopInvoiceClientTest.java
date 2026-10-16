package com.rex.commodis.service.dvelopServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rex.commodis.configuration.DvelopApiProperties;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferResultDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransfersResponseDto;
import com.rex.commodis.service.invoiceServices.InvoicePayloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DvelopInvoiceClientTest {

    @Mock private RestClient.Builder restClientBuilder;
    @Mock private RestClient restClient;
    @Mock private InvoicePayloadService invoicePayloadService;
    @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private RestClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock private RestClient.RequestBodySpec requestBodySpec;
    @Mock private RestClient.ResponseSpec responseSpec;

    private DvelopInvoiceClient client;

    @BeforeEach
    void setUp() {
        DvelopApiProperties properties = new DvelopApiProperties();
        properties.setBaseUrl("https://api.example");
        properties.setTransfersPath("/transfers");
        properties.setIntegrationKey("secret-key");

        when(restClientBuilder.baseUrl("https://api.example")).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);

        client = new DvelopInvoiceClient(
                restClientBuilder,
                properties,
                invoicePayloadService,
                new ObjectMapper()
        );
    }

    @Test
    void fetchTransfers_deserializesResponseAndSavesRawPayloadWhenTransfersExist() {
        String rawJson = """
                {"transfers":[{"workflow":{"voucher":{"doc_id":"DOC-1"}}}]}
                """;
        stubGet(rawJson);

        InvoiceTransfersResponseDto result = client.fetchTransfers();

        assertThat(result.getTransfers()).hasSize(1);
        verify(invoicePayloadService).saveTransfer(rawJson);
    }

    @Test
    void fetchTransfers_whenTransfersEmpty_doesNotSaveRawPayload() {
        String rawJson = """
                {"transfers":[]}
                """;
        stubGet(rawJson);

        InvoiceTransfersResponseDto result = client.fetchTransfers();

        assertThat(result.getTransfers()).isEmpty();
        verifyNoInteractions(invoicePayloadService);
    }

    @Test
    void fetchTransfers_whenResponseCannotBeDeserialized_throwsRuntimeException() {
        stubGet("{not-json");

        assertThatThrownBy(() -> client.fetchTransfers())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to deserialize transfers response");
        verifyNoInteractions(invoicePayloadService);
    }

    @Test
    void reportSuccess_postsSuccessResultToCallbackUrl() {
        String callbackUrl = "https://api.example/callback";
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(callbackUrl)).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(InvoiceTransferResultDto.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(ResponseEntity.ok().build());

        client.reportSuccess(callbackUrl);

        verify(requestBodyUriSpec).uri(callbackUrl);
        verify(requestBodySpec).contentType(MediaType.APPLICATION_JSON);
        verify(requestBodySpec).body(any(InvoiceTransferResultDto.class));
        verify(responseSpec).toBodilessEntity();
    }

    @SuppressWarnings("unchecked")
    private void stubGet(String rawJson) {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(rawJson);
    }
}
