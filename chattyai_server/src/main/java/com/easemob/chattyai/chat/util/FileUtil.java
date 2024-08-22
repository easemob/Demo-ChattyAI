/**
 * Project Name:codgen
 * File Name:FileUtil
 * Package Name:com.codgen.utils
 * Date:2020/12/28　17:25
 * Copyright (c)　2020, zzstxx.com All Rights Reserved.
 */
package com.easemob.chattyai.chat.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 文件上传工具类
 */
@Component
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 绝对路径
     **/
    private  String absolutePath = "";

    /**
     * 静态目录
     **/
    private  String staticDir = "static";





    /**
     * 上传单个文件
     * 最后文件存放路径为：static/upload/image/test.jpg
     * 文件访问路径为：http://127.0.0.1:8080/upload/image/test.jpg
     * 该方法返回值为：/upload/image/test.jpg
     *
     * @param inputStream 文件流
     * @param path        文件路径，如：image/
     * @param filename    文件名，如：test.jpg
     * @return 成功：上传后的文件访问路径，失败返回：null
     */
    public String upload(InputStream inputStream, String path, String filename) {
        return uploadLocal(inputStream,path,filename);
    }



    /**
     * @description:文件上传到本地
     * @author: alonecoder
     * @date: 2023/5/4 16:38
     * @param: [inputStream, path, filename]
     * @return: java.lang.String
     * @throws:
     **/
    public String uploadLocal(InputStream inputStream, String path, String filename){
        //第一次会创建文件夹

        createDirIfNotExists(path);

        String resultPath = File.separator+path + filename;

        //存文件
        File uploadFile = new File(absolutePath, staticDir + resultPath);
        try {
            FileUtils.copyInputStreamToFile(inputStream, uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return resultPath;
    }

    /**
     * 创建文件夹路径
     */
    private  void createDirIfNotExists(String path) {
        if (!absolutePath.isEmpty()) {
            return;
        }

        //获取跟目录
        File file = null;
        try {
            file = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("获取根目录失败，无法创建上传目录！");
        }
        if (!file.exists()) {
            file = new File("");
        }

        absolutePath = file.getAbsolutePath();

        File upload = new File(absolutePath, staticDir + File.separator + path);
        if (!upload.exists()) {
            upload.mkdirs();
        }
    }



    /**
     * 图片压缩阈值  超过这个值的时，进行压缩  目前设置的为20KB
     */
    public static final Long THERESHOLD_THRESHOLD_SIZE = 1024 * 20L;
    public static String getFileSuffix(File file) {
        if(!file.exists()){
            return null;
        }

        return file.getName().substring(file.getName().lastIndexOf(".")+1);
    }

    public static byte[] compressImage(byte[] bytes,String filename){

        String fileSuffix = filename.substring(filename.lastIndexOf(".")+1);
        assert fileSuffix != null;
        InputStream input = null;
        BufferedImage originalImage = null;
        ByteArrayOutputStream os = null;
        try {
            input = new ByteArrayInputStream(bytes);
            BufferedImage pngImage = ImageIO.read(input);
            if(!"jpg".equalsIgnoreCase(fileSuffix) ) {
                originalImage = new BufferedImage(pngImage.getWidth(), pngImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = originalImage.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, pngImage.getWidth(), pngImage.getHeight());
                g2d.drawImage(pngImage, 0, 0, null);
                g2d.dispose();

            }else{
                originalImage = pngImage;
            }
            os = new ByteArrayOutputStream();
            ImageIO.write(originalImage,"jpg",os);
            byte[] byteArray = os.toByteArray();
            long size = byteArray.length;
            System.out.println("转码之后大小为"+size);
            os.close();
            os = new ByteArrayOutputStream();
            if(size > THERESHOLD_THRESHOLD_SIZE) {
                //LOGGER.info("[ImgUtil.compressImage]文件："+name+"压缩图片时，图片大小超过阈值，需要进行压缩");
                // 计算压缩后的图片大小
                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();
                int compressedWidth = 800;
                int compressedHeight = (int) (originalHeight * ((double) compressedWidth / originalWidth));

                // 压缩图片
                BufferedImage compressedImage = new BufferedImage(compressedWidth, compressedHeight, originalImage.getType());
                compressedImage.createGraphics().drawImage(originalImage.getScaledInstance(compressedWidth, compressedHeight, Image.SCALE_SMOOTH),
                        0, 0, compressedWidth, compressedHeight, null);

                ImageIO.write(compressedImage, "jpg", os);
                return os.toByteArray();
            }else{
                //LOGGER.info("[ImgUtil.compressImage]文件："+name+"压缩图片时，图片大小没超过阈值，不需要压缩");
                ImageIO.write(originalImage, "jpg", os);
                return os.toByteArray();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            IoUtil.close(input);
            IoUtil.close(os);
        }
        return null;
    }
}
