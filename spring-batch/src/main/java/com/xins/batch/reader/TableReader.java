package com.xins.batch.reader;

import com.xins.batch.bean.domain.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author xinsong
 * @Date 2025/7/2 19:58
 * @description
 */
@Configuration
public class TableReader {

    @Bean
    @StepScope
    public MyBatisCursorItemReader<User> userReader(SqlSessionFactory sqlSessionFactory) {
        MyBatisCursorItemReader<User> reader = new MyBatisCursorItemReader<>();
        reader.setSqlSessionFactory(sqlSessionFactory);
        reader.setQueryId("com.xins.batch.mapper.UserMapper.selectAllUsers");
        return reader;
    }
}


