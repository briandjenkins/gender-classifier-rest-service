/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bana274.gender.services;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.datavec.image.loader.ImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.imgscalr.Scalr;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.stereotype.Service;

/**
 *
 * @author brianj
 */
@Service
public class ModelService {

    private MultiLayerNetwork classifier;

    public ModelService() {
        System.out.println("Service running...");
        loadCNNModelFromResources();
    }

    /**
     * Load the CNN classifier as a resource.
     */
    private void loadCNNModelFromResources() {
        String fileName = "gender_cnn_model.zip";        
        InputStream inputStream = getClass().getResourceAsStream(fileName);        
        try {
            File modelFile = File.createTempFile( "model", ".zip" );
            FileUtils.copyToFile( inputStream, modelFile );
            classifier = MultiLayerNetwork.load(modelFile, false);
        } catch (IOException ex) {
            Logger.getLogger(ModelService.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Model has been loaded.");
    }
    
    /**
     * Classify the image as either female or male using the CNN model.
     * 
     * @param image
     * @return
     * @throws IOException 
     */
    public INDArray classify(BufferedImage image) throws IOException {
        final int HEIGHT = 32;
        final int WIDTH = 32;
        final int CHANNELS = 3;
        final boolean CROP_BEFORE_RESCHALING = true;
        
//        System.out.println("Height in pixels: "+image.getHeight());
//        System.out.println("Width in pixels: "+image.getWidth());
        
        BufferedImage scaledImage = Scalr.resize(image, 90, 120);
        
//        System.out.println("Height in pixels: "+scaledImage.getHeight());
//        System.out.println("Width in pixels: "+scaledImage.getWidth());
                      
        ImageLoader imageLoader = new ImageLoader(WIDTH, HEIGHT, CHANNELS, CROP_BEFORE_RESCHALING);        
        INDArray input = imageLoader.asMatrix(scaledImage).reshape(1, 3, 32, 32);      
        INDArray output = classifier.output(input);    
        System.out.println(output);
        return output;
    }
    
   
}
