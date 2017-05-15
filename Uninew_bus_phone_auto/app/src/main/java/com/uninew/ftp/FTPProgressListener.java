package com.uninew.ftp;

import java.io.File;

/**
 * Created by rong on 2017-04-14.
 */

public interface FTPProgressListener {

    void onUploadProgress(FTPState currentStep, long uploadSize,
                          File file);

    void onDownLoadProgress(FTPState currentStep, long downProcess,
                            File file);
}
