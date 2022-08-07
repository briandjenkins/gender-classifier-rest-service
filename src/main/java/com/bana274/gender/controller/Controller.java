/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bana274.gender.controller;

import com.bana274.gender.models.ImageUploadResponse;
import com.bana274.gender.services.ModelService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author brianj
 */
@RestController
//@CrossOrigin(origins = "http://localhost:8082") open for specific port
@CrossOrigin() // open for all ports
public class Controller {

    @Autowired
    ModelService modelService;

    @GetMapping("/")
    public String helloWorld() {
        return "hello v0.91";
    }

    /**
     * Image posted from the client.
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload/image")
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {

        byte[] content = file.getBytes();      
        InputStream is = new ByteArrayInputStream(content);
        BufferedImage imageBI = ImageIO.read(is);
        INDArray prediction = modelService.classify(imageBI);
        double[] labels = prediction.toDoubleVector();
        String label = (labels[0] == 1 ? "female" : "male");
        return ResponseEntity.status(HttpStatus.OK).body(new ImageUploadResponse("Image uploaded successfully: " + file.getOriginalFilename(), label));
    }

}
