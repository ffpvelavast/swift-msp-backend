/** Service implementation for ReceiptFile business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.model.receipts.ReceiptFile;
import com.rex.commodis.repository.receipts.ReceiptFileRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptFileServiceImpl  implements ReceiptFileService{

    private final ReceiptFileRepository receiptFileRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void createReceiptFile(Long receiptId, Long platformId, List<MultipartFile> receiptFiles){

        if (receiptFiles == null || receiptFiles.isEmpty()) return;

        int sequence = 1;

        for(MultipartFile file : receiptFiles){

            UUID receiptFileId = UUID.randomUUID();

            String originalName = file.getOriginalFilename();
            String ext = (originalName != null && originalName.contains("."))
                    ? originalName.substring(originalName.lastIndexOf('.'))
                    : "";
            String s3Key = platformId + "/" + receiptFileId + ext;
            s3Service.uploadFile(file, s3Key);

            //Create record in receipt_files
            ReceiptFile receiptFile = new ReceiptFile();
            receiptFile.setReceiptId(receiptId);
            receiptFile.setReceiptFileId(receiptFileId);
            receiptFile.setReceiptNumberSeq(sequence++);
            receiptFile.setFileTypeId((short) 1);
            receiptFile.setFilename(s3Key);
            receiptFile.setOriginalFilename(file.getOriginalFilename());
            receiptFile.setMediaType(file.getContentType());

            receiptFileRepository.save(receiptFile);
        }

    }
}