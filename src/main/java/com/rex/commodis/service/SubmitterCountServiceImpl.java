package com.rex.commodis.service;

import org.springframework.stereotype.Service;

import com.rex.commodis.dto.receipts.SubmitterCountDto;
import com.rex.commodis.repository.receipts.SubmitterCountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmitterCountServiceImpl implements SubmitterCountService {

    private final SubmitterCountRepository submitterCountRepository;

    @Override
    public SubmitterCountDto getSubmitterReceiptCount(Long rexUserId) {

        Long count = submitterCountRepository
                .getSubmitterReceiptCount(rexUserId);

        return new SubmitterCountDto(count);
    }
}
