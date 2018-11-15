package com.xytsz.xytaj.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by admin on 2018/5/16.
 * <p/>
 * 文件工具类
 */
public class FileUtils {


    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     *
     * @param context
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     *
     * @param filePath
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesAll(new File(filePath));
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     *
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    private static void deleteFilesAll(File directory) {
        if (directory != null && directory.exists()) {
            // 删除文件夹中的所有文件包括子目录
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 删除子文件
                if (files[i].isFile()) {
                    files[i].delete();
                }
                // 删除子目录
                else if (files[i].isDirectory()) {
                    deleteFilesAll(files[i]);
                }
            }
            // 删除当前目录
            directory.delete();
        }
    }

    public static File createTakePhotoFile(String pathUrl) {
        File rootDirectory = Environment.getExternalStorageDirectory();
        File file = new File(rootDirectory, pathUrl);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = UUID.randomUUID().toString() + ".jpg";
        return new File(file, fileName);
    }

    public static void deletFile(File file) {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();

                file.createNewFile();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
