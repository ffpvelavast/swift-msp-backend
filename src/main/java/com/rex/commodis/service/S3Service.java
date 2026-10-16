/** Service interface defining S3 operations; implemented by S3ServiceImpl. */
package com.rex.commodis.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    void  uploadFile(MultipartFile file, String s3Key);
}
