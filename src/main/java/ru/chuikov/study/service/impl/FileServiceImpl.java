package ru.chuikov.study.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.chuikov.study.entity.MusicItemInfo;
import ru.chuikov.study.entity.TypeOfMedia;
import ru.chuikov.study.entity.User;
import ru.chuikov.study.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Log
public class FileServiceImpl implements FileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    @Override
    public void add(MultipartFile multipartFile, User user, TypeOfMedia type) throws IOException {

        log.info("add file start by " + user.getUsername() + " with id " + user.getId());

        DBObject metadata = new BasicDBObject();
        metadata.put("userId", user.getId());
        metadata.put("type", type.name());

        gridFsTemplate.store(multipartFile.getInputStream()
                , multipartFile.getOriginalFilename()
                , multipartFile.getContentType()
                , metadata);

        log.info("add file end by " + user.getUsername() + " with id " + user.getId());
    }

    @Override
    public void delete(String id, User user) {
        log.info("delete file start by " + user.getUsername() + " with id " + user.getId());
        gridFsTemplate.delete(
                new Query(Criteria.where("_id").is(id)
                        .andOperator(Criteria.where("metadata.userId").is(user.getId()))));
        log.info("delete file end by " + user.getUsername() + " with id " + user.getId());

    }

    @Override
    public GridFsResource getMusicById(String id, TypeOfMedia type) {
        return gridFsOperations.getResource(gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(id)
                        .andOperator(
                                Criteria.where("metadata.type").is(type.name())))
                )
        );
    }

    @Override
    public List<MusicItemInfo> findAllByUserId(Long userid, TypeOfMedia type) {
        ArrayList<MusicItemInfo> result = new ArrayList<>();
        List<GridFSFile> gridFSFiles = new ArrayList<GridFSFile>();
        gridFsTemplate.find(new Query(
                        Criteria.where("metadata.userId")
                                .is(userid)
                                .andOperator(
                                        Criteria.where("metadata.type").is(type.name()))
                )
        ).into(gridFSFiles);

        for (GridFSFile file : gridFSFiles) {
            //result.add(file.getObjectId().toString());
            GridFsResource resource = gridFsOperations.getResource(file);
            result.add(MusicItemInfo.builder()
                    .id(resource.getFileId().toString())
                    .name(resource.getFilename())
                    .length(file.getLength())
                    .contentType(resource.getContentType())
                    .uploadDate(file.getUploadDate())
                    .build());
        }
        return result;
    }
}
