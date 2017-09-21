package com.hudongwx.origin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 图片压缩工具
 */
public class ImgUtils {

    /**
     * 按比例缩小图片的像素以达到压缩的目的
     * @param imgPath 图片路径
     * @param sizeKB  图片大小kb
     * @throws FileNotFoundException
     */
    public static void compressImageByPixel(String imgPath, int sizeKB) {
        if(imgPath == null) return;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(imgPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        float maxSize = 1200;//最大的高宽像素
        int be = 1;
        if (width >= height && width > maxSize) {//缩放比,用高或者宽其中较大的一个数据进行计算
            be = (int) (newOpts.outWidth / maxSize);
            be++;
        } else if (width < height && height > maxSize) {
            be = (int) (newOpts.outHeight / maxSize);
            be++;
        }
        newOpts.inSampleSize =be;//设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//当系统内存不够时候图片自动被回收
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        compressImageByQuality(bitmap,sizeKB,imgPath);//压缩好比例大小后再进行质量压缩
    }
    /**
     * 多线程压缩图片的质量
     * @author JPH
     * @param bitmap 内存中的图片
     * @param imgPath 图片的保存路径
     * @date 2014-12-5下午11:30:43
     */
    public static void compressImageByQuality(final Bitmap bitmap, final int sizeKB , final String imgPath){
        if(bitmap==null) return;
        new Thread(new Runnable() {//开启多线程进行压缩处理
            @Override
            public void run() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int options = 90;
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
                while (baos.toByteArray().length >sizeKB*1024) {//循环判断如果压缩后图片是否大于指定大小,大于继续压缩
                    baos.reset();//重置baos即让下一次的写入覆盖之前的内容
                    options -= 5;//图片质量每次减少5
                    if(options<=5)options=5;//如果图片质量小于5，为保证压缩后的图片质量，图片最底压缩质量为5
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//将压缩后的图片保存到baos中
                    if(options==5) break;//如果图片的质量已降到最低则，不再进行压缩
                }
                try {
                    //将压缩后的图片保存的本地上指定路径中
                    File thumbnailFile=new File(imgPath);
                    FileOutputStream fos = new FileOutputStream(thumbnailFile);
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
