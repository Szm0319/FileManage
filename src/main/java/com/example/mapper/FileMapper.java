package com.example.mapper;

import com.example.entity.FileEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO files (file_name, file_path, file_size, upload_time, uploader) " +
            "VALUES (#{fileName}, #{filePath}, #{fileSize},#{uploadTime},#{uploader})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(FileEntity fileEntity);

    @Select("SELECT * FROM files WHERE file_name = #{fileName}")
    FileEntity findByFileName(String fileName);

    @Delete("DELETE FROM files WHERE file_name = #{fileName}")
    void deleteByFileName(String fileName);

    @Select("SELECT * FROM files")
    List<FileEntity> findAll();

    @Select("SELECT COUNT(*) FROM files WHERE file_name = #{fileName}")
    boolean existsByFileName(String fileName);
}