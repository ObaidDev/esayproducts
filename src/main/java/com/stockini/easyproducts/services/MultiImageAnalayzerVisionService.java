package com.stockini.easyproducts.services;


import com.google.cloud.vision.v1.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.protobuf.ByteString;



@Service
public class MultiImageAnalayzerVisionService {
    


    public Map<String, Object> analyzeImages(List<MultipartFile> files, String comment) throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            List<AnnotateImageRequest> requests = new ArrayList<>();

            // Prepare batch request
            for (MultipartFile file : files) {
                ByteString imgBytes = ByteString.copyFrom(file.getBytes());
                Image img = Image.newBuilder().setContent(imgBytes).build();

                List<Feature> features = List.of(
                        Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build() ,
                        Feature.newBuilder().setType(Feature.Type.LOGO_DETECTION).build(),
                        Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build()
                );

                AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                        .addAllFeatures(features)
                        .setImage(img)
                        .build();

                requests.add(request);
            }

            // Call Vision API once for all images
            BatchAnnotateImagesResponse batchResponse = vision.batchAnnotateImages(requests);

            // Process responses
            for (int i = 0; i < batchResponse.getResponsesList().size(); i++) {
                AnnotateImageResponse res = batchResponse.getResponsesList().get(i);

                Map<String, Object> imageResult = new HashMap<>();
                imageResult.put("id", files.get(i).getOriginalFilename());

                if (!res.getLabelAnnotationsList().isEmpty()) {
                    imageResult.put("labels",
                            res.getLabelAnnotationsList().stream().map(e -> e.getDescription()).toList());
                }
                if (!res.getLogoAnnotationsList().isEmpty()) {
                    imageResult.put("logos",
                            res.getLogoAnnotationsList().stream().map(e -> e.getDescription()).toList());
                }
                if (!res.getTextAnnotationsList().isEmpty()) {
                    imageResult.put("text", res.getTextAnnotationsList().get(0).getDescription());
                }

                results.add(imageResult);
            }
        }

        responseMap.put("comment", comment);
        responseMap.put("results", results);
        return responseMap;
    }
}
