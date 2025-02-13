package com.example.controller;

import com.example.entity.FileEntity;
import com.example.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("username") String username) {
        log.info("request：用户正在上传文件---");
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // 检查文件大小
            long maxSize = 1024 * 1024 * 1024; // 1GB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest().body("File size exceeds limit");
            }

            log.info("接收到文件信息：{}", file.getOriginalFilename());
            log.info("上传者：{}", username);
            // 检查文件是否存在
            String originalFileName = file.getOriginalFilename();
            String fileName = originalFileName;
            int num = 1;

            while (fileService.isExistFile(fileName)) {
                // 如果文件名已存在，则在文件名后加上 (num)，并递增 num
                fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.')) + "(" + num + ")" + originalFileName.substring(originalFileName.lastIndexOf('.'));
                num++;
            }

            // 保存文件
            fileService.save(file, username, fileName);
            return ResponseEntity.ok().body("File uploaded successfully: " + fileName);
        } catch (Exception e) {
            log.error("文件上传失败：{}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }


    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        log.info("用户请求下载文件：" + filename);

        Resource resource = fileService.load(filename);
        // 对文件名进行URL编码
        String encodedFilename = URLEncoder.encode(resource.getFilename(), "UTF-8").replace("+", "%20");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // 设置正确的 MIME 类型
                .body(resource);
    }


    @DeleteMapping("/delete/{filename:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        log.info("request：用户请求删除文件---"+ filename);
        fileService.delete(filename);
        return ResponseEntity.ok().body("File deleted successfully: " + filename);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> listFiles() {
        log.info("request：用户请求获取文件列表---");
        List<FileEntity> fileEntities = fileService.loadAll();
        return ResponseEntity.ok().body(fileEntities);
    }
}