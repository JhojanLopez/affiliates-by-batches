package com.example.affiliatebatchprocessor.batches.readers;

import com.example.affiliatebatchprocessor.models.AffiliateDTO;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class AffiliateReader {
    @Bean
    public FlatFileItemReader <AffiliateDTO> reader() {
        return new FlatFileItemReaderBuilder<AffiliateDTO>()
                .name("affiliateItemReader")
                .resource(new ClassPathResource("affiliates.csv")) // CSV file in src/main/resources
                .delimited()
                //.delimiter(",") by default spring batch takes , as delimiter
                .names("dni", "firstName", "lastName") // column names
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(AffiliateDTO.class); // mapping AffiliateDTOAffiliateDTO
                }})
                .build();
    }
}
