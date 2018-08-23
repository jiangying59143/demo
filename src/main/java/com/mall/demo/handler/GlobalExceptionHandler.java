package com.mall.demo.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
    private Map<String, Object> exceptionHandler(HttpServletRequest req, Exception e){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success","false");
        modelMap.put("errMsg",e.getMessage());
        return modelMap;
    }

    @ExceptionHandler(MultipartException.class)
    public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage() + "test");
        return "redirect:/uploadStatus";
    }

}
