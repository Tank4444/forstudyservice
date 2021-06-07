package ru.chuikov.study.service;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;
import ru.chuikov.study.entity.MusicItemInfo;
import ru.chuikov.study.entity.TypeOfMedia;
import ru.chuikov.study.entity.User;

import java.io.IOException;
import java.util.List;

public interface FileService {
    void add (MultipartFile multipartFile, User user, TypeOfMedia type) throws IOException;
    void delete(String id,User user);
    GridFsResource getMusicById(String id,TypeOfMedia type);
    List<MusicItemInfo> findAllByUserId(Long userid, TypeOfMedia type);


}
