package com.mall.demo.common.utils;

import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.rest.controller.FileController;
import io.swagger.annotations.*;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private static final String TEMP_PATH = "temp";

    @Value("${me.upload.path}")
    private static String baseFolderPath;

    public static boolean singleFileUpload(MultipartFile file, Long userId, String newFileName){
        String fileType = "";
        String fileName = file.getOriginalFilename();
        if(!StringUtils.isEmpty(file.getName()) && file.getName().contains(".")){
            String[] ss = fileName.split(".");
            fileName = ss[0];
            fileType = "."+ss[1];
        }
        if(BooleanUtils.and(new boolean[]{userId !=null, !new File(baseFolderPath + userId).exists()}) ){
            new File(baseFolderPath + userId).mkdirs();
        }
        try {
            byte[] bytes = file.getBytes();
            org.apache.commons.io.FileUtils.writeByteArrayToFile(
                    new File(baseFolderPath
                            + (userId == null ? "" : userId + File.separator) + TEMP_PATH + File.separator
                            + (StringUtils.isEmpty(newFileName)?fileName:newFileName)
                            + fileType), bytes);
        }catch(Exception e){
            log.error(fileName + "file upload error", e);
            return false;
        }
        return true;
    }

    public static void deleteArticleTempFolder(Long userId){
        File file = new File(baseFolderPath + userId + File.separator + TEMP_PATH );
        if(file.exists()){
            file.delete();
        }
    }

    public static boolean renameArticleTempFolder(Long userId, Long articleId){
        File file = new File(baseFolderPath + userId + File.separator + TEMP_PATH );
        if(file.exists()){
            file.renameTo(new File(baseFolderPath + userId + File.separator + articleId));
            return true;
        }
        return false;
    }

}
