package com.example.affiliatebatchprocessor.batches.writers;

import com.example.affiliatebatchprocessor.models.AffiliateDTO;
import com.example.affiliatebatchprocessor.services.AffiliateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class AffiliateWriter implements ItemWriter<AffiliateDTO> {
    private final AffiliateService affiliateService;
    @Override
    public void write(Chunk<? extends AffiliateDTO> chunk) throws Exception {
        List<Object[]> affiliates = chunk.getItems().stream()
                .map(dto -> new Object[]{dto.getDni(), dto.getFirstName(), dto.getLastName()})
                .toList();
        affiliateService.insertByBatches(affiliates);
        log.info("{} affiliates has been saved.", chunk.getItems().size());
    }
}
