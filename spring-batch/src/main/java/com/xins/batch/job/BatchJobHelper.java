package com.xins.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @Author xinsong
 * @Date 2025/6/25 10:12
 * @description
 */
@Service
public class BatchJobHelper {

    @Autowired
    JobRepository jobRepository;

    @Bean
    public Job exportUserJob(@Qualifier("exportUserData") Step step1) {
        return new JobBuilder("exportUserDataJob", jobRepository)
                .start(step1)
                .build();
    }
}
