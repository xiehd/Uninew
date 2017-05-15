package com.uninew.ftp;

import java.io.File;

/**
 * Created by rong on 2017-04-15.
 */

public class UpdateFile {
    private File file;
    private String remotePath;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public UpdateFile(File file, String remotePath) {
        this.file = file;
        this.remotePath = remotePath;
    }

    public UpdateFile(){
    }
}
