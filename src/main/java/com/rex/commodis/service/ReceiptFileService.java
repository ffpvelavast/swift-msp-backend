/** Service interface defining ReceiptFile operations; implemented by ReceiptFileServiceImpl. */
package com.rex.commodis.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReceiptFileService {

    void createReceiptFile(Long receiptId, Long platformId, List<MultipartFile> receiptFiles);
}