package com.xytsz.xytaj.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by admin on 2018/5/16.
 *
 * 文件工具类
 */
public class FileUtils {

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
