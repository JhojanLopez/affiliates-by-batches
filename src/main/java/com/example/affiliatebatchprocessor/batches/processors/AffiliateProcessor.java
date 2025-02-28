package com.example.affiliatebatchprocessor.batches.processors;

import com.example.affiliatebatchprocessor.exceptions.ValidationException;
import com.example.affiliatebatchprocessor.models.AffiliateDTO;
import com.example.affiliatebatchprocessor.services.AffiliateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class AffiliateProcessor implements ItemProcessor<AffiliateDTO, AffiliateDTO> {
    private final AffiliateService affiliateService;
    @Override
    public AffiliateDTO process(AffiliateDTO affiliateDTO) throws Exception {
        try {
            affiliateService.validateAffiliate(affiliateDTO);
            return affiliateDTO;
        } catch (ValidationException e) {
            log.warn("Warning: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
    }
}
