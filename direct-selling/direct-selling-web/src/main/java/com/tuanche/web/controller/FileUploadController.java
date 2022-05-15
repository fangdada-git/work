package com.tuanche.web.controller;

import com.tuanche.commons.util.Resources;
import com.tuanche.upload.FileUtil;
import com.tuanche.upload.FtpUtil;
import com.tuanche.upload.UploadProperties;
import com.tuanche.web.util.CommonLogUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class FileUploadController {

    private static String serverPath = UploadProperties.getString("temfilePath");
    private static String temfilePath = "/" + UploadProperties.getString("temfilePath");

    /**
     * 上传文件
     * @author：HuangHao
     * @CreatTime： 2017-10-26 上午11:34:59
     */
    @RequestMapping("/upload/image")
    public @ResponseBody
    Map<String, Object> uploadPic(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> ret = new HashMap<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String typePath = "eap";
        String type = request.getParameter("type");
        if (type == null || type.length() == 0) {
            return ret;
        }
        String startPath = temfilePath;
        MultipartFile imgFile = multipartRequest.getFile("file");
        String imgName = System.currentTimeMillis() + "" + new Random().nextInt(10000) + ".jpg";
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String path = date +  "/" + imgName;
        CommonLogUtil.addInfo("uploadPic", "-----------path------------", path);
        String realPath = request.getSession().getServletContext().getRealPath(request.getContextPath());
        CommonLogUtil.addInfo("uploadPic", "-----------realPath------------", realPath);
        // 放到项目根目录下
        String fullPath = new File(realPath) + startPath + typePath + "/" + path;
        CommonLogUtil.addInfo("uploadPic", "-----------fullPath------------", fullPath);
        File dir = new File(fullPath).getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            InputStream inputStream = imgFile.getInputStream();
            bis = new BufferedInputStream(inputStream);
            OutputStream out = new FileOutputStream(fullPath);
            bos = new BufferedOutputStream(out);
            byte[] tem = new byte[1024];
            @SuppressWarnings("unused")
            int len = 0;
            while ((len = bis.read(tem)) != -1) {
                bos.write(tem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret.put("code", "1");
            return ret;
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
        }
        CommonLogUtil.addInfo("uploadPic", "---typePath----" + typePath, " picDeal=" + Resources.getString("picDeal"));
        // 项目真实路径+/pic_tmp/direct
        if ("ftp".equals(Resources.getString("picDeal"))) {
            FtpUtil.ftpUpload(Resources.getString(typePath + "ftp.host"), Resources.getString(typePath + "ftp.username"),
                    Resources.getString(typePath + "ftp.password"), new File(realPath) + startPath + typePath + "/" + date + "/", typePath+"/"+date, imgName);
        } else {
            FileUtil.copyFile(fullPath, serverPath + typePath + "/" + date + "/", imgName);
        }
        ret.put("location", Resources.getString("pic.url") + "/zhuanti/" + typePath + "/" + path);
        ret.put("code", "0");
        ret.put("dataId", 0);
        ret.put("dataName", imgName);
        return ret;
    }


    /**
     * @param response
     * @param request
     * @param info
     * @Title: callBack
     * @Description: TODO
     * @author: zhaojl
     * @date: 2015年7月23日 下午3:01:42
     * @return: void
     */
    protected void callBack(HttpServletResponse response, HttpServletRequest request, String info) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String jsonpcallback = request.getParameter("jsonpcallback");
        String scriptcallback = request.getParameter("scriptcallback");
        String callback = request.getParameter("callback");

        response.setDateHeader("Expires", 0);
        String result = null;
        if (jsonpcallback != null) {
            result = jsonpcallback + "('" + info + "' )";

        } else if (callback != null) {
            result = callback + "('" + info + "')";
        } else if (scriptcallback != null) {
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            result = "<script>" + scriptcallback + "( " + info + " ) </script>";
        } else {
            result = info;
        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                System.out.println(result);
                out.print(result);
                out.flush();
                out.close();
            }
        }
    }
}
