/** Service implementation for ReceiptNumberGenerator business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ReceiptNumberGeneratorServiceImpl implements ReceiptNumberGeneratorService{

    private static final String CHAR_POOL = "ABCDEFGHJKLMNOPQRSTUVWXYZ123456789";
    private static final int CODE_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 10000;
    private final Random random = new SecureRandom();

    private final ReceiptRepository receiptRepository;

    public String generateUniqueReceiptCode() {

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {

            String code = generateRandomCode();

            boolean exists = receiptRepository.existsByReceiptNumber(code);

            if (!exists) {
                return code;
            }
        }

        throw new BadRequestException("Receipt Number already exists.");
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            code.append(CHAR_POOL.charAt(index));
        }

        return code.toString();
    }
}
