package com.xytsz.xytaj.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by admin on 2017/11/17.
 * 图片处理工具
 */
public class BitmapUtil {

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            //从指定路径读取图片，获取exif信息
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }

            return degree;

        } catch (IOException e) {
            //e.printStackTrace();
        }

        return degree;
    }


    public static Bitmap rotateBitmap(Bitmap bm, float orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {

            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {

        }

        return null;
    }

    public static Bitmap getScaleBitmap(String fileResult) {
        Bitmap bitmap;
        if (fileResult == null){
            return null;
        }else {
            bitmap = BitmapFactory.decodeFile(fileResult);
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            return bitmap;
        }
    }

}
