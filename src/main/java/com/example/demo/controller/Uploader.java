package com.example.demo.controller;

import com.example.demo.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class Uploader {

    @Value("${app.upload-path:public/upload}")
    private String uploadPath;

    /**
     * 单文件上传 → public/upload/YYYY/MM/uuid_原始文件名
     */
    @PostMapping
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        // 年月子目录: 2026/06
        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        Path dir = Paths.get(uploadPath, dateDir);
        Files.createDirectories(dir);

        // 生成唯一文件名
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String newName = UUID.randomUUID().toString().replace("-", "") + ext;
        Path target = dir.resolve(newName);

        file.transferTo(target.toFile());

        // 可访问的 URL
        String url = "/upload/" + dateDir + "/" + newName;

        return Result.success(Map.of(
                "url", url,
                "name", newName,
                "originalName", originalName,
                "size", file.getSize()
        ));
    }

    /**
     * 多文件上传
     */
    @PostMapping("/batch")
    public Result uploadBatch(@RequestParam("files") MultipartFile[] files) throws IOException {
        java.util.List<Object> list = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            Result r = upload(file);
            if (r.isStatus()) {
                list.add(r.getData());
            }
        }
        return Result.success(Map.of("files", list, "count", list.size()));
    }
}
