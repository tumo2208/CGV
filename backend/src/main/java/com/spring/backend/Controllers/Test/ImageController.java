package com.spring.backend.Controllers.Test;

import com.spring.backend.Services.Common.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private StorageService storageService;
    @PostMapping("/upload")
    public ResponseEntity uploadImage(@RequestParam("img") MultipartFile img) throws Exception {
        return ResponseEntity.status(201).body(storageService.uploadImage(img));
    }
}
