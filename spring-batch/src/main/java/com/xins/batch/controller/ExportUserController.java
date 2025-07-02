package com.xins.batch.controller;

import com.xins.batch.service.ExportUserByBufferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author xinsong
 * @Date 2025/7/2 19:47
 * @description
 */
@RestController
@RequestMapping("/export/user")
@Slf4j
public class ExportUserController {

    @Autowired
    private ExportUserByBufferService exportUserByBufferService;

    @Autowired
    @Qualifier("asyncJobLauncher") // 注入我们自定义的异步 Launcher
    private JobLauncher asyncJobLauncher;

    @Autowired
    @Qualifier("exportUserJob")
    private Job exportUserJob;

    @GetMapping("/buffer")
    public String bufferExportUsers(@RequestParam String path) {
        try {
            exportUserByBufferService.exportToCsv(path);
            return "导出成功，文件路径: " + path;
        } catch (IOException e) {
            log.error(e.getMessage());
            return "导出失败: " + e.getMessage();
        }
    }


    @GetMapping("/batch")
    public void batchExportUsers(String path) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .addString("outputFilePath", path)
                    .toJobParameters();

            asyncJobLauncher.run(exportUserJob, jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
