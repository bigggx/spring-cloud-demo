package com.xins.batch.config;

import com.xins.batch.bean.domain.User;
import com.xins.batch.listener.JobCompletionListener;
import com.xins.batch.processor.UserItemProcessor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @Author xinsong
 * @Date 2025/7/2 20:05
 * @description
 */
@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchTaskConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    JobCompletionListener jobCompletionListener;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("batch-task-");
        executor.initialize();
        return executor;
    }
    @Bean
    public JobLauncher asyncJobLauncher() {
        TaskExecutorJobLauncher launcher = new TaskExecutorJobLauncher();
        launcher.setJobRepository(jobRepository);
        launcher.setTaskExecutor(taskExecutor()); // 设置异步线程池
        return launcher;
    }

    @Bean
    public Step exportUserData(MyBatisCursorItemReader<User> reader, UserItemProcessor processor, FlatFileItemWriter<User> writer) {
        return new StepBuilder("exportUserData", jobRepository)
                .<User, User>chunk(3000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(jobCompletionListener)
                .build();
    }

}
