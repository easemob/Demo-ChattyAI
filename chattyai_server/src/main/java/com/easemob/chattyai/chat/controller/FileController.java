package com.easemob.chattyai.chat.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.easemob.chattyai.api.ResponseModel;
import com.easemob.chattyai.chat.util.CommonUtil;
import com.easemob.chattyai.chat.util.FileUtil;
import org.apache.ibatis.mapping.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class FileController {


    @Autowired
    private FileUtil fileUtil;

    private static List<String> wihteFileList = new ArrayList<String>();

    static{
        wihteFileList.add(".jpg");
        wihteFileList.add(".png");
        wihteFileList.add(".jpeg");
        wihteFileList.add(".bmp");
    }

    /**
     * 用户自己更新头像  --- 上传头像
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/upload/avatar.json")
    public ResponseModel upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IllegalStateException, IOException {
        if (!file.isEmpty()) {
            String filename = CommonUtil.getSnowflakeId();
            String fileSuffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
            if(StrUtil.isBlank(fileSuffix) || !wihteFileList.contains(fileSuffix.toLowerCase())){
                return ResponseModel.error("not support file type,support file type in [jpg,jpeg,png,bmp]");
            }
            String filepath = fileUtil.upload(file.getInputStream(), "avatar/", filename + fileSuffix);
            JSONObject json = new JSONObject();
            json.set("filepath", filepath);
            json.set("viewpath", CommonUtil.getLocalHostAddress()+filepath);
            return ResponseModel.ok(json);
        } else {
            return ResponseModel.error("file is empty");
        }
    }
}
