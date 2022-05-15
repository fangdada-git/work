package com.tuanche.directselling.util;

import com.alibaba.fastjson.JSON;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @description: 图片工具类
 * @return:
*/
public class ImageUtil {

    public static InputStream getInputStreamByGet(String url) throws Exception {
        boolean flage = false;
        try {
            URL picUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.setRequestProperty("Referer","no-referrer");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(20000);
            CommonLogUtil.addInfo("根据HttpUrl获取图片 end:状态", JSON.toJSONString(conn.getResponseCode()));
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                CommonLogUtil.addInfo("根据HttpUrl获取图片 流", JSON.toJSONString(conn.getResponseMessage()));
                return inputStream;
            }
        } catch (IOException e) {
            flage = true;
            e.printStackTrace();
        }
        if (flage) {
            throw new Exception("链接下载文件报错");
        }
        return null;
    }

    // 将服务器响应的数据流存到本地文件
    public static void saveData(InputStream is, File file) throws Exception {
        boolean flage = false;
        try (BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));) {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
        } catch (IOException e) {
            flage = true;
            e.printStackTrace();
        }
        if (flage) {
            throw new Exception("下载文件报错");
        }

    }

    public static File getUrlImageFile(String url, File file) throws Exception {
        InputStream inputStream = getInputStreamByGet(url);
        saveData(inputStream, file);
        return file;
    }


    /**
     *
     * 功能描述: <br>
     * 〈调整图像到固定大小〉
     *
     * @param srcImageFile  源图像文件地址
     * @param descImageFile 缩放后的图像地址
     * @param width         缩放后的宽度
     * @param height        缩放后的高度
     * @param isPadding     是否补白
     *
     */
    public final static void changeSize(String srcImageFile, String descImageFile, int width, int height, boolean isPadding) {
        try {
            // 缩放比例
            double ratio = 0.0;
            File file = new File(srcImageFile);
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = bufferedImage.getScaledInstance(width, height, bufferedImage.SCALE_SMOOTH);
            // 计算缩放比例
            if (bufferedImage.getHeight() > bufferedImage.getWidth()) {
                ratio = (new Integer(height)).doubleValue() / bufferedImage.getHeight();
            } else {
                ratio = (new Integer(width)).doubleValue() / bufferedImage.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            image = op.filter(bufferedImage, null);

            // 是否需要补白
            if (isPadding) {
                BufferedImage tempBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2d = tempBufferedImage.createGraphics();
                graphics2d.setColor(Color.white);
                graphics2d.fillRect(0, 0, width, height);
                graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                if (width == image.getWidth(null)) {
                    graphics2d.drawImage(image, 0, (height - image.getHeight(null)) / 2, image.getWidth(null), image.getHeight(null), Color.white, null);
                } else {
                    graphics2d.drawImage(image, (width - image.getWidth(null)) / 2, 0, image.getWidth(null), image.getHeight(null), Color.white, null);
                }
                graphics2d.dispose();
                image = tempBufferedImage;
            }
            ImageIO.write((BufferedImage) image, "jpg", new File(descImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 功能描述: <br>
     * 〈利用画布生成新的图片〉
     *
     * @param backImage 背景图文件地址
     * @param srcImage  前景图文件地址
     * @param descImage 生成图文件地址
     * @return
     *
     */
    public static void mergeImage(String backImage, String srcImage, String descImage,int calcWidth ,int calcHeight) {
        try {
            int offset = 0;
            BufferedImage backBufferedImage = ImageIO.read(new File(backImage));
            BufferedImage srcBufferedImage = ImageIO.read(new File(srcImage));
            // 输出图片宽度
            int width = backBufferedImage.getWidth() + offset;
            // 输出图片高度
            int height = backBufferedImage.getHeight() + offset;
            BufferedImage descBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2d = (Graphics2D) descBufferedImage.getGraphics();
            graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // 往画布上添加图片,并设置边距
            graphics2d.drawImage(backBufferedImage, null, 0, 0);
            graphics2d.drawImage(srcBufferedImage, null, width / 2 - calcWidth, height - calcHeight);
            graphics2d.dispose();
            // 输出新图片
            ImageIO.write(descBufferedImage, "jpg", new File(descImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
