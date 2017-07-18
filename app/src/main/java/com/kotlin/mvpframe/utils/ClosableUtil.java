package com.kotlin.mvpframe.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Zijin on 2017/7/5.
 */

public final class ClosableUtil {
    /**
     * 关闭对象
     * @param object
     */
    public static void closeObject(Object object){
        if(object instanceof Closeable){
            Closeable closeable = (Closeable) object;
            try {
                closeable.close();
            } catch (IOException e) { }
        }
    }
}
