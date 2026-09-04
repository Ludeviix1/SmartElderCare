package com.elder.controller.admin;

import com.elder.util.AliOSSUtil;
import com.elder.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class UploadController {

    //MultipartFile file 封装了上传的文件的所有信息
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        //7c45616c1e8740d987c41e95f33b9abe
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //a.png
        String filename = file.getOriginalFilename();
        //.png
        String extension = filename.substring(filename.lastIndexOf(".") );
        //7c45616c1e8740d987c41e95f33b9abe.png
        String newFilename = uuid + extension;
        String url = "";
        try {
            url = AliOSSUtil.uploadFile(newFilename, file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.ok("上传成功", url);
    }
}
