package com.example.affiliatebatchprocessor.services;

import com.example.affiliatebatchprocessor.database.respositories.AffiliateRepository;
import com.example.affiliatebatchprocessor.exceptions.ValidationException;
import com.example.affiliatebatchprocessor.models.AffiliateDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
@Log4j2
@Service
@RequiredArgsConstructor
public class AffiliateServiceImpl implements AffiliateService {
    private final AffiliateRepository affiliateRepository;
    private final JdbcTemplate jdbcTemplate; //using native SQL improves the performance
    private static final String INSERT_BATCHES_SQL = "INSERT INTO affiliates (dni, first_name, last_name) VALUES (?, ?, ?)";
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
    @Transactional //improves the performance indicating that it uses one transaction
    @Override
    public void insertByBatches(List<Object[]> affiliates) {
        log.info("saving affiliates ... ({})", affiliates.size());
        jdbcTemplate.batchUpdate(INSERT_BATCHES_SQL, affiliates);
    }
}
