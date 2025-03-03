package com.example.affiliatebatchprocessor.services;

import com.example.affiliatebatchprocessor.batches.processors.AffiliateProcessor;
import com.example.affiliatebatchprocessor.batches.readers.AffiliateReaderFactory;
import com.example.affiliatebatchprocessor.batches.writers.AffiliateWriter;
import com.example.affiliatebatchprocessor.models.AffiliateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@Service
@RequiredArgsConstructor
public class AffiliateBatchFactoryServiceImpl implements AffiliateBatchFactoryService {
    @Value("${spring.application.affiliate-chunk-size}")
    private int chunkSize;
    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AffiliateReaderFactory readerFactory;
    private final AffiliateProcessor processor;
    private final AffiliateWriter writer;
    @Override
    public JobExecution runJobStaticFile() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Resource resource = new ClassPathResource("affiliates.csv");
        return this.runJob(resource);
    }

    @Override
    public JobExecution runJobWithDynamicFile(MultipartFile file) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Resource resource = new InputStreamResource(file.getInputStream());
        return this.runJob(resource);

    }
    private JobExecution runJob(Resource resource) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        FlatFileItemReader<AffiliateDTO> reader = AffiliateReaderFactory.dynamicReader(resource);

        Step step = this.createStep(reader);
        Job job = new JobBuilder("dynamicJob", jobRepository)
                .start(step)
                .build();

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        return jobLauncher.run(job, jobParameters);
    }
    private Step createStep(ItemReader<AffiliateDTO> reader) {
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
