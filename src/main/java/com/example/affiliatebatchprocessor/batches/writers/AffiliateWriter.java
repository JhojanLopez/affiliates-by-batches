package com.example.affiliatebatchprocessor.batches.writers;

import com.example.affiliatebatchprocessor.models.AffiliateDTO;
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
    private final JdbcTemplate jdbcTemplate; //using native SQL improves the performance

    @Transactional //improves the performance indicating that it uses one transaction
    @Override
    public void write(Chunk<? extends AffiliateDTO> chunk) throws Exception {
        log.info("saving affiliates ... ({})", chunk.getItems().size());
        String sql = "INSERT INTO affiliates (dni, first_name, last_name) VALUES (?, ?, ?)";
        List<Object[]> list = chunk.getItems().stream()
                .map(dto -> new Object[]{dto.getDni(), dto.getFirstName(), dto.getLastName()})
                .toList();
        jdbcTemplate.batchUpdate(sql, list);
        log.info("{} affiliates has been saved.", chunk.getItems().size());
    }
}
