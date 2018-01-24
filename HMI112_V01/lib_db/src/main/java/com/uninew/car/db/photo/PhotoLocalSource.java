package com.uninew.car.db.photo;

import com.uninew.car.db.main.DBBaseDataSource;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public interface PhotoLocalSource extends DBBaseDataSource<PhotoData>{
    interface GetPhotoCallBack extends GetDBDataCallBack<PhotoData>{

    }
    interface LoadPhotosCallBack extends LoadDBDataCallBack<PhotoData>{

    }

    void searchPhotos(int cameraId,int reason,long startTime,long endTime,LoadPhotosCallBack callBack);

    void getPhotosByTime(long time,GetPhotoCallBack callBack);
}
