/** Service interface defining ReceiptStatusCountProjection operations; implemented by ReceiptStatusCountProjectionImpl. */
package com.rex.commodis.dto;

/**
 * Native query projection for status counts.
 * Returned by ReceiptRepository.getStatusCounts().
 */
public interface ReceiptStatusCountProjection {
    String getStatus();
    Long getCount();
}