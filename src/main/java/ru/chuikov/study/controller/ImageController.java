package ru.chuikov.study.controller;


import javassist.NotFoundException;
import lombok.extern.java.Log;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.chuikov.study.entity.UserStatus;
import ru.chuikov.study.entity.TypeOfMedia;
import ru.chuikov.study.entity.User;
import ru.chuikov.study.service.FileService;
import ru.chuikov.study.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/img")
@Log
public class ImageController {

    private final FileService fileService;
    private final Tika tika;
    private final UserService userService;

    private final TypeOfMedia typeOfMedia = TypeOfMedia.image;

    public ImageController(FileService fileService, Tika tika, UserService userService) {
        this.fileService = fileService;
        this.tika = tika;
        this.userService = userService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity addMusic(@RequestParam("file") MultipartFile multipartFile,
                                   @AuthenticationPrincipal User user) {
        try {
            var detect=tika.detect(multipartFile.getInputStream());
            if (!detect.contains(typeOfMedia.name())) {
                throw new Exception("Media type not supported");
            }
            fileService.add(multipartFile, user, typeOfMedia);
            return new ResponseEntity<>("Music added", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/getlistByUser/{id}")
    public ResponseEntity getListMusicByUserId(@PathVariable Long id,
                                               @AuthenticationPrincipal User user) {
        User owner = userService.getUserById(id);
        if (user.getId() == owner.getId() || owner.getUserStatus() == UserStatus.OPEN)
            return new ResponseEntity<List>(
                    fileService.findAllByUserId(id, typeOfMedia),
                    HttpStatus.OK);
        else
            return new ResponseEntity<String>(
                    "user with id " + id + " closed or blocked",
                    HttpStatus.FORBIDDEN);
    }

    @PostMapping(value = "/get/{id}")
    public ResponseEntity getListMusicByUser(@PathVariable("id") String id,
                                             @AuthenticationPrincipal User user) {
        try {
            GridFsResource fsResource = fileService.getMusicById(id, typeOfMedia);
            User owner = userService.getUserById((long) fsResource.getGridFSFile().getMetadata().get("userId"));
            if (owner.getId() == user.getId() || owner.getUserStatus() == UserStatus.OPEN)
                return ResponseEntity.ok()
                        .contentLength(fsResource.contentLength())
                        .contentType(MediaType.parseMediaType(fsResource.getContentType()))
                        .body(new InputStreamResource(fsResource.getInputStream()));
            else throw new NotFoundException("User with id = " + id + " not found or user is closed of blocked");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity deleteMusic(@PathVariable("id") String id,
                                      @AuthenticationPrincipal User user) {
        GridFsResource resource = fileService.getMusicById(id, typeOfMedia);
        User owner = userService.getUserById((long) resource.getGridFSFile().getMetadata().get("userId"));
        if (owner.getId() == user.getId()) {
            fileService.delete(id, user);
            return new ResponseEntity<String>("Music deleted", HttpStatus.OK);
        } else return new ResponseEntity<String>("Delete only owner", HttpStatus.FORBIDDEN);
    }
}
