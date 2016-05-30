package com.cn.clound.base.common.io;

import android.os.Build;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年3月29日11:45:49
 */
public class StringCodingUtils {

    public static byte[] getBytes(String src, Charset charSet) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            try {
                return src.getBytes(charSet.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return src.getBytes(charSet);
        }
    }
}
