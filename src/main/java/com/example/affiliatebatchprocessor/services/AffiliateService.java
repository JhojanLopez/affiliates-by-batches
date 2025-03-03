package com.example.affiliatebatchprocessor.services;

import com.example.affiliatebatchprocessor.exceptions.ValidationException;
import com.example.affiliatebatchprocessor.models.AffiliateDTO;

import java.util.List;

public interface AffiliateService {
    boolean existAffiliateByDni(String dni);
    void validateAffiliate(AffiliateDTO affiliateDTO) throws ValidationException;
    void insertByBatches(List<Object[]> affiliates);
}
