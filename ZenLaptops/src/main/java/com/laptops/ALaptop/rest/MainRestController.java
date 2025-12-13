package com.laptops.ALaptop.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api")
public class MainRestController {
    @GetMapping("/{account_id}/{imageLink}")
    public byte[] getImage(
            @PathVariable("account_id") long id,
            @PathVariable("imageLink") String imageLink
    ){
        System.out.println(id);
    File file = new File("src/main/resources/static/image_base/user_"+id+"/"+imageLink);
    byte[] byteFile = new byte[0];

    try {
        byteFile = Files.readAllBytes(file.toPath());
    }catch(IOException e){
        e.printStackTrace();
        }
    return byteFile;
    }
}
