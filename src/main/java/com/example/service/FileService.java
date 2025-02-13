package com.example.service;

import com.example.entity.FileEntity;
import com.example.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    private final Path root = Paths.get("uploads");
    @Autowired
    private FileMapper fileMapper;

    public FileService() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public void save(MultipartFile file,String username,String fileName) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(fileName));
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setFilePath(this.root.resolve(fileName).toString());
            fileEntity.setFileSize(file.getSize());
            fileEntity.setUploadTime(LocalDateTime.now());
            fileEntity.setUploader(username);
            fileMapper.save(fileEntity);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void delete(String filename) {
        try {
            Files.delete(root.resolve(filename));
            fileMapper.deleteByFileName(filename);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public List<FileEntity> loadAll() {
        return fileMapper.findAll();
    }

    public Boolean isExistFile(String filename){
        return fileMapper.existsByFileName(filename);
    }
}