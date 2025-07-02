package com.xins.batch.mapper;

import com.xins.batch.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;

@Mapper
public interface UserMapper {

    // 使用游标方式查询大数据量
    @Select("SELECT * FROM user")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    @ResultType(User.class)
    void selectLargeData(ResultHandler<User> handler);

    @Select("SELECT id, username, password, email, phone, birth_date, registration_time, last_login_time, status, address FROM user")
    @Options(fetchSize = Integer.MIN_VALUE) // 启用 Cursor 模式
    Cursor<User> selectAllUsers();

    // 分页查询
    List<User> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 获取总记录数
    long countAll();
}