package com.stockini.easyproducts.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stockini.easyproducts.services.MultiImageAnalayzerVisionService;


@RestController
@RequestMapping("/products")
public class MultiImageAnalayzerController {
    

    private final MultiImageAnalayzerVisionService visionService;


    @Autowired
    public MultiImageAnalayzerController(MultiImageAnalayzerVisionService visionService) {
        this.visionService = visionService;
    }


    @PostMapping(value = "/analyze" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> analyze(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("comment") String comment
    ) throws IOException{
        
        return visionService.analyzeImages(files, comment);
    }




    @GetMapping
    public void test() {
        System.out.println(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
    } 
}
