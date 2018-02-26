package com.xytsz.xytaj.util;

import java.io.File;
import java.io.FileInputStream;
import android.util.Base64;

/**
 * Created by admin on 2017/9/20.
 * 轉換工具类
 */
public class FileBase64Util {

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer,Base64.DEFAULT);
    }
}
