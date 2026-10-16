/** Spring configuration class for InvoiceScheduler; registers beans or properties used across the application. */
package com.rex.commodis.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rex.commodis.service.dvelopServices.InvoiceSyncService;

@Component
@ConditionalOnProperty(prefix = "invoice-sync", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class InvoiceScheduler {

    private final InvoiceSyncService invoiceSyncService;

    @Scheduled(fixedDelay = 60000)
    public void runInvoiceSync() {
        invoiceSyncService.syncInvoices();
    }
}
