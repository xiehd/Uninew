package com.uninew.ftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.uninew.ftp.FTP.UploadProgressListener;
import com.uninew.json.JsonFileContants;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ProgressInputStream extends InputStream {

    private static final int TEN_KILOBYTES = 1024 * 10;  //每上传10K返回一次

    private InputStream inputStream;

    private long progress;
    private long lastUpdate;

    private boolean closed;
    
    private UploadProgressListener listener;
    private File localFile;
    private long step;
    private int myprogress = 0;

    public ProgressInputStream(InputStream inputStream,UploadProgressListener listener,File localFile) {
        this.inputStream = inputStream;
        this.progress = 0;
        this.lastUpdate = 0;
        this.listener = listener;
        this.localFile = localFile;
        this.closed = false;
        step = localFile.length() /100;
    }

    @Override
    public int read() throws IOException {
    	int count = inputStream.read();
        return incrementCounterAndUpdateDisplay(count);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = inputStream.read(b, off, len);
        return incrementCounterAndUpdateDisplay(count);
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (closed)
            throw new IOException("already closed");
        closed = true;
    }

    private int incrementCounterAndUpdateDisplay(int count) {
        if (count > 0)
            progress++;
        lastUpdate = maybeUpdateDisplay(progress, lastUpdate);
        return count;
    }

    private long maybeUpdateDisplay(long progress, long lastUpdate) {
    	if(step > 0){
        if ((progress/step)!= myprogress) {
            lastUpdate = progress;
            myprogress = (int) (progress/step);
            this.listener.onUploadProgress(JsonFileContants.FTP_UPLOAD_LOADING, myprogress, this.localFile);
        }
        }
        return lastUpdate;
    }
    
  
    
}
