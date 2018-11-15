package com.xytsz.xytaj.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.widget.ImageView;

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

            switch (orientation) {
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
        if (fileResult == null) {
            return null;
        } else {
            try {
                bitmap = getBitmap(fileResult);
                if (bitmap != null) {
                    bitmap =Bitmap.createScaledBitmap(bitmap,600,600,true);
                    return bitmap;
                }
            } catch (IOException e) {
                return null;
            }
            return null;

        }
    }


    private static Bitmap getBitmap(String path) throws IOException {
        Bitmap bitmap;
        int width = 600;
        int height = 600;

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, factoryOptions);

        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = 1;

        if (imageHeight > height || imageWidth > width) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / scaleFactor) >= height && (halfWidth / scaleFactor) >= width) {
                scaleFactor *= 2;
            }
        }
        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;
        factoryOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(path, factoryOptions);
        //check the rotation of the image and display it properly
        ExifInterface exif;

        exif = new ExifInterface(path);

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        Matrix matrix = new Matrix();
        if (orientation == 6) {
            matrix.postRotate(90);
        } else if (orientation == 3) {
            matrix.postRotate(180);
        } else if (orientation == 8) {
            matrix.postRotate(270);
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

}
