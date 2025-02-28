package com.example.affiliatebatchprocessor.services;

import com.example.affiliatebatchprocessor.database.respositories.AffiliateRepository;
import com.example.affiliatebatchprocessor.exceptions.ValidationException;
import com.example.affiliatebatchprocessor.models.AffiliateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AffiliateServiceImpl implements AffiliateService {
    private final AffiliateRepository affiliateRepository;

    @Override
    public boolean existAffiliateByDni(String dni) {
        return affiliateRepository.existsByDni(dni);
    }

    @Override
    public void validateAffiliate(AffiliateDTO affiliateDTO) throws ValidationException {
        if (affiliateDTO.getDni() == null || affiliateDTO.getDni().trim().isBlank()) {
            throw new ValidationException("dni is required. ","'dni'");
        }

        if (this.existAffiliateByDni(affiliateDTO.getDni())) {
            throw new ValidationException("already exists an affiliate with the same dni. ","'dni'");
        }

        if (affiliateDTO.getFirstName() == null || affiliateDTO.getFirstName().trim().isBlank()) {
            throw new ValidationException("first name is required. ","'first name'");
        }

        if (affiliateDTO.getLastName() == null || affiliateDTO.getLastName().trim().isBlank()) {
            throw new ValidationException("last name is required. ","'last name'");
        }

    }
    @Override
    public void insert(AffiliateDTO affiliateDTO) {

    }
}
