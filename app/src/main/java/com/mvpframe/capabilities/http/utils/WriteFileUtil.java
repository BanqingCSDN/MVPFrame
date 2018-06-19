package com.mvpframe.capabilities.http.utils;

import android.os.Handler;
import android.os.Looper;

import com.mvpframe.capabilities.http.interfaces.Progress;
import com.mvpframe.capabilities.http.interfaces.Success;
import com.mvpframe.capabilities.http.interfaces.Error;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import okhttp3.ResponseBody;

/**
 * CJY
 */

public class WriteFileUtil {
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void writeFile(ResponseBody body, String path, Progress progress, Success mSuccessCallBack, Error mErrorCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File futureStudioIconFile = new File(path);
                InputStream inputStream = null;
                OutputStream outputStream = null;
                ProgressInfo progressInfo = new ProgressInfo();
                try {
                    byte[] fileReader = new byte[1024];
                    progressInfo.total = body.contentLength();
                    progressInfo.read = 0;
                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(futureStudioIconFile);
                    while (true) {
                        int read = inputStream.read(fileReader);
                        if (read == -1) {
                            break;
                        }
                        outputStream.write(fileReader, 0, read);
                        progressInfo.read += read;
                        mHandler.post(() -> progress.progress(div(progressInfo.read,progressInfo.total,2)));
                    }
                    mHandler.post(() ->mSuccessCallBack.success(path));
                    outputStream.flush();
                } catch (IOException e) {
                    mErrorCallBack.error(e);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                    }

                }
            }
        }).start();

    }

    static class ProgressInfo {
        public long read = 0;
        public long total = 0;
    }

    public static float div(float v1, float v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
