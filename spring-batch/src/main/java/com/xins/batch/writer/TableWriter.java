package com.xins.batch.writer;

import com.xins.batch.domain.User;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * @Author xinsong
 * @Date 2025/7/2 20:00
 * @description
 */
@Configuration
public class TableWriter {

    @Bean
    @StepScope
    public FlatFileItemWriter<User> userWriter(@Value("#{jobParameters['outputFilePath']}") String outputFilePath) {
        FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(outputFilePath));
        // 设置CSV文件的头部
        writer.setHeaderCallback(writer1 -> writer1.write("id,username,password,email,phone,birth_date,registration_time,last_login_time,status,address"));

        // 设置行聚合器
        DelimitedLineAggregator<User> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        // 设置字段提取器
        BeanWrapperFieldExtractor<User> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"id", "username", "password", "email", "phone", "birthDate", "registrationTime", "lastLoginTime", "status", "address"});
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);
        return writer;
    }

}
