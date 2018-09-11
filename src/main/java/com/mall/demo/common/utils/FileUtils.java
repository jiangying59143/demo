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

    public static final String UPLOAD_FILE_VIDEO = "video";

    public static final String UPLOAD_FILE_IMAGE = "image";

    public static boolean singleFileUpload(MultipartFile file, String baseFolderPath, String type, Long userId, String newFileName){
        String fileName = file.getOriginalFilename();
        File userFile = new File(baseFolderPath + type + File.separator + userId);
        if(BooleanUtils.and(new boolean[]{userId !=null, !userFile.exists()}) ){
            userFile.mkdirs();
        }
        try {
            byte[] bytes = file.getBytes();
            org.apache.commons.io.FileUtils.writeByteArrayToFile(
                    new File(baseFolderPath + (StringUtils.isEmpty(type) ? "":type + File.separator)
                            + (userId == null ? "" : userId + File.separator)
                            + (StringUtils.isEmpty(newFileName)?fileName:newFileName)), bytes);
        }catch(Exception e){
            log.error(fileName + "file upload error", e);
            return false;
        }
        return true;
    }

    public static void deleteArticleTempFolder(String baseFolderPath, String type, Long userId, String fileName){
        File file = new File(baseFolderPath + type + File.separator + userId + File.separator + fileName );
        if(file.exists()){
            file.delete();
        }
    }

}
