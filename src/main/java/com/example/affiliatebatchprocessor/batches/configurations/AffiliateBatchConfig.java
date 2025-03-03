package com.example.affiliatebatchprocessor.batches.configurations;

import com.example.affiliatebatchprocessor.models.AffiliateDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class AffiliateBatchConfig {
    @Value("${spring.application.affiliate-chunk-size}")
    private int chunkSize;

    @Bean
    public Job processAffiliatesJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("affiliatesJob", jobRepository)//registering a job with its step in jobRepository
                .start(step)
                .build();
    }

    /**
     * creating the affiliate step setting its reader, processor and writer
     * PlatformTransactionManager -> it manages transactions on the step, ensuring operations such as read, process and writer can be atomics
     * */
    @Bean
    public Step AffiliatesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                     ItemReader<AffiliateDTO> reader,
                     ItemProcessor<AffiliateDTO, AffiliateDTO> processor,
                     ItemWriter<AffiliateDTO> writer) {
        return new StepBuilder("affiliatesStep", jobRepository)//registering its name
                .<AffiliateDTO, AffiliateDTO>chunk(chunkSize, transactionManager)//chunk size
                .reader(reader)
                .processor(processor)
                .writer(writer)
                //.faultTolerant() // it allows exceptions
                //.skip(ValidationException.class) // ⚠️ skip exceptions if the exception is ValidationException
                //.skipLimit(5) // 🔥 it allows until x quantity of exceptions
                //.retry(SQLException.class) // 🔄 it allows if there is an error from db
                //.retryLimit(3) // 🔄 it repeats until 3 times
                .build();
    }
}
