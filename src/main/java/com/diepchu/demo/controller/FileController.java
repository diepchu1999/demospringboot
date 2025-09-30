package com.diepchu.demo.controller;

import com.diepchu.demo.domain.response.file.ResUploadFileDTO;
import com.diepchu.demo.service.FileService;
import com.diepchu.demo.util.anotation.ApiMessage;
import com.diepchu.demo.util.error.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    @Value("${hoidanit.upload-file.base-uri}")
    private String baseUri;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
                                                   @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException, StorageException {
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValidExtension = allowedExtensions.stream().anyMatch(ext -> fileName.toLowerCase().endsWith("." + ext));
        if (!isValidExtension) {
            throw new StorageException("File is empty");
        }


        this.fileService.createUploadFolder(baseUri + folder);

        String uploadFile = this.fileService.store(file, folder);
        ResUploadFileDTO resUploadFileDTO = new ResUploadFileDTO(uploadFile, Instant.now());
        return ResponseEntity.ok().body(resUploadFileDTO);
    }

    @GetMapping("/file")
    @ApiMessage("Download a file")
    public ResponseEntity<Resource> download(@RequestParam(name= "fileName", required = false) String fileName,
                                             @RequestParam(name = "folder", required = false) String folder)
    throws StorageException, URISyntaxException, FileNotFoundException {
        if(fileName == null || fileName.isEmpty()) {
            throw new StorageException("File name is empty");
        }

        long fileLength = this.fileService.getFileLength(fileName, folder);

        if(fileLength == 0){
            throw new StorageException("File with name " + fileName + " not found");
        }

        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"")
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }





}
