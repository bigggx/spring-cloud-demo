package com.xins.batch.service;

import com.xins.batch.domain.User;
import com.xins.batch.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ExportUserByBufferService {

    private static final int PAGE_SIZE = 10000; // 每页大小
    private static final int BUFFER_SIZE = 8192; // 8k缓冲区
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserMapper userMapper;

    public void exportToCsv(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath), BUFFER_SIZE)) {
            // 写入CSV表头
            writer.write("id,username,password,email,phone,birth_date," +
                    "registration_time,last_login_time,status,address\n");
            userMapper.selectLargeData(resultContext -> {
                User user = resultContext.getResultObject();
                try {
                    // 写入 CSV 行
                    writer.write(toCsvRow(user));
                    // 定期刷新缓冲区
                    if (resultContext.getResultCount() % 10000 == 0) {
                        writer.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("写入 CSV 失败", e);
                }
            });

            System.out.println("导出完成，文件路径: " + filePath);
        }
    }

    private String toCsvRow(User user) {
        return String.join(",",
                user.getId().toString(),
                escapeCsv(user.getUsername()),
                escapeCsv(user.getPassword()),
                escapeCsv(user.getEmail()),
                escapeCsv(user.getPhone()),
                formatDate(user.getBirthDate()),
                formatDateTime(user.getRegistrationTime()),
                formatDateTime(user.getLastLoginTime()),
                user.getStatus().toString(),
                escapeCsv(user.getAddress())
        ) + "\n";
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        return DATE_FORMAT.format(date);
    }

    private String formatDateTime(Date date) {
        if (date == null) return "";
        return DATETIME_FORMAT.format(date);
    }
}