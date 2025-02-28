package com.example.affiliatebatchprocessor.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/affiliate")
@RequiredArgsConstructor
public class AffiliateController {
    private final JobLauncher jobLauncher;
    private final Job affiliatesJob;

    @PostMapping("/start")
    public ResponseEntity<?> load() {
        try {
            log.info("Starting batches with static file..");

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // Evita cache
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(affiliatesJob, jobParameters);
            return ResponseEntity.ok("Batch Job started with ID: " + execution.getId());

        } catch (Exception e) {
            log.error("Error starting the affiliatesJob", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error starting the affiliatesJob: " + e.getMessage());
        }
    }
}
