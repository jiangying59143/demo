package com.mall.demo.rest.controller;

import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import io.swagger.annotations.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Api(description = "文件上传下载相关接口")
@RequestMapping(value = "/file", produces = "application/json;charset=utf-8")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    @Value("${me.upload.path}")
    private String baseFolderPath;

    @ApiOperation("单文件上传")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 204, message = "80000:上传文件为空", response=Result.class),
            @ApiResponse(code = 500, message = "1:失败")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping
    public ResponseEntity<Result> fileUpload(MultipartFile file) {
        Result r = new Result();
        return singleFileUpload(file, r);
    }

    private ResponseEntity<Result> singleFileUpload(MultipartFile file, Result r){
        if (null == file) {
            log.error("上传文件不能为空");
            r.setResultCode(ResultCode.UPLOAD_FILE_NULL);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(r);
        }
        String fileName = file.getOriginalFilename();
        r.setData(fileName);
        try {
            byte[] bytes = file.getBytes();
            log.info("开始上传文件【{}】...", fileName);
            FileUtils.writeByteArrayToFile(new File(baseFolderPath + fileName), bytes);
            r.setResultCode(ResultCode.SUCCESS);
            log.info("文件【{}】上传成功...", fileName);
        }catch(Exception e){
            log.error(e.getMessage());
            r.setResultCode(ResultCode.ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(r);
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation("多文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File", paramType = "form"),
            @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "File", paramType = "form"),
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 500, message = "1:失败")})
    @PostMapping("/batch")
    public ResponseEntity<Result> batchFileUpload(HttpServletRequest request) throws IOException {
        List<MultipartFile> fileList = ((MultipartRequest)request).getFiles("file");
        boolean failFlag = false;
        List<Result> list = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            MultipartFile file = fileList.get(i);
            Result r = new Result();
            ResponseEntity<Result> rer = singleFileUpload(file, r);
            list.add(r);
            if(rer.getStatusCode() != HttpStatus.OK){
                failFlag = true;
            }
        }
        Result r = new Result();
        r.setData(list);
        if(failFlag){
            r.setResultCode(ResultCode.ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(r);
        }
        r.setResultCode(ResultCode.SUCCESS);
        return ResponseEntity.ok(r);
    }

    @ApiOperation("多文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "files", value = "文件", required = true, dataType = "File", allowMultiple = true, paramType = "form")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 500, message = "1:失败")})
    @PostMapping("/batch2")
    public ResponseEntity<Result> batchFileUpload2(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<MultipartFile> fileList = Arrays.asList(files);
        boolean failFlag = false;
        List<Result> list = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            MultipartFile file = fileList.get(i);
            Result r = new Result();
            ResponseEntity<Result> rer = singleFileUpload(file, r);
            list.add(r);
            if(rer.getStatusCode() != HttpStatus.OK){
                failFlag = true;
            }
        }
        Result r = new Result();
        r.setData(list);
        if(failFlag){
            r.setResultCode(ResultCode.ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(r);
        }
        r.setResultCode(ResultCode.SUCCESS);
        return ResponseEntity.ok(r);
    }

    @ApiOperation("文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 500, message = "1:失败")})
    @GetMapping("/download")
    public ResponseEntity<Result> download(HttpServletResponse response) throws UnsupportedEncodingException {
        String fileName="1.docx";
        Result r = new Result();
        r.setResultCode(ResultCode.SUCCESS);
        // 设置强制下载不打开
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
//        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buff = FileUtils.readFileToByteArray(new File(baseFolderPath + fileName));
            outputStream.write(buff);
        } catch (IOException e) {
            e.printStackTrace();
            r.setResultCode(ResultCode.ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(r);
        }
        return ResponseEntity.ok(r);
    }
}
