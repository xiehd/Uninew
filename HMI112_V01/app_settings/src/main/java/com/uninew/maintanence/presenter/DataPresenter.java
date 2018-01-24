package com.uninew.maintanence.presenter;

import android.content.Context;
import android.nfc.Tag;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.uninew.export.ExportModel;
import com.uninew.export.ImportModel;
import com.uninew.maintanence.interfaces.IDataPresenter;
import com.uninew.maintanence.interfaces.IDataView;
import com.uninew.maintanence.model.DataModel;
import com.uninew.settings.R;
import com.uninew.until.SDCardUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class DataPresenter implements IDataPresenter, ExportModel.ExportCallBack,
        DataModel.CopyCallBack, ImportModel.ImportCallBack {

    private IDataView mView;
    private int type;
    private ExportModel exportModel;
    private Context mContext;
    private DataModel dataModel;
    private ImportModel importModel;
    //    private SDCardUtils sdCardUtils;
    private static  String USB_PATH = "/mnt/media_rw/udisk";
    private static  String SDCard = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private static  String SERVICE_LOG = SDCard + "service_buffer";
    private static  String CRASHINFO_LOG = SDCard + "HMI112CrashInfo";

    public DataPresenter(IDataView view, Context context) {
        this.mView = view;
        this.mContext = context;
    }

    @Override
    public void DataImport(int id) {
        File saveFile = new File(USB_PATH);
        if (saveFile.exists() && saveFile.canWrite() && saveFile.canRead()) {
        if (dataOperateState == DataOperateState.normal) {
            switch (id) {
                /**
                 * 数据导出
                 *
                 * @param id 数据id
                 *            数据id
                 *           0：司机信息
                 *           1：参数导入
                 */
                case 0:
                    if (dataOperateState == DataOperateState.normal) {
                        dataOperateState = DataOperateState.importing;
                        importModel.startImportDriverMsg();
                    }
                    break;
                case 1:
                    if (dataOperateState == DataOperateState.normal) {
                        dataOperateState = DataOperateState.importing;
                        Log.d("ssss", "开始导入");
                        importModel.startImportParam();
                    }
                    break;

            }
        } else {
            if (dataOperateState == DataOperateState.exporting) {
                showToast(R.string.exporting_toast);
            } else if (dataOperateState == DataOperateState.importing) {
                showToast(R.string.importing_toast);
            }
        }
        } else {
            showToast(R.string.usb_not_exist_toast);
        }

    }

    private DataOperateState dataOperateState = DataOperateState.normal;

    @Override
    public void onImportSuccess() {
        dataOperateState = DataOperateState.normal;
        showToast(R.string.import_success_toast);
    }

    @Override
    public void onImportFailure(int erroe) {
        dataOperateState = DataOperateState.normal;
        showToast(R.string.import_failure_toast);
    }

    private enum DataOperateState {
        normal,
        importing,
        exporting
    }

    @Override
    public void DataExport(int id) {
        File saveFile = new File(USB_PATH);
        if (saveFile.exists() && saveFile.canWrite() && saveFile.canRead()) {
            if (dataOperateState == DataOperateState.normal) {
                switch (id) {
                    /**
                     * 数据导出
                     *
                     * @param id 数据id
                     *           1：参数导出
                     *           2：日志导出
                     *           3：运营数据导出
                     *           4：签到签退导出
                     *           5：报警数据导出
                     */
                    case 1:
                        if (dataOperateState == DataOperateState.normal) {
                            dataOperateState = DataOperateState.exporting;
                            exportModel.exportParamSet();
                        }
                        break;
                    case 2:
                        if (dataOperateState == DataOperateState.normal) {
                            dataOperateState = DataOperateState.exporting;
                            exportLog();
                        }
                        break;
                    case 3:
                        if (dataOperateState == DataOperateState.normal) {
                            dataOperateState = DataOperateState.exporting;
                            exportModel.exportRevenue();
                        }
                        break;
                    case 4:
                        if (dataOperateState == DataOperateState.normal) {
                            dataOperateState = DataOperateState.exporting;
                            exportModel.exportSignOrSignOut();
                        }
                        break;
                    case 5:
                        break;

                }
            } else {
                if (dataOperateState == DataOperateState.exporting) {
                    showToast(R.string.exporting_toast);
                } else if (dataOperateState == DataOperateState.importing) {
                    showToast(R.string.importing_toast);
                }
            }
        } else {
            showToast(R.string.usb_not_exist_toast);
        }
    }

    @Override
    public void EmptyDB() {

    }

    @Override
    public void DataRecovery() {

    }

    @Override
    public void stop() {
        importModel.destroy();
        importModel = null;
        exportModel = null;
    }

    @Override
    public void start() {
        exportModel = new ExportModel(mContext);
        importModel = new ImportModel(mContext);
        dataModel = new DataModel();
        dataModel.setCopyCallBack(this);
        exportModel.setExportCallBack(this);
        dataOperateState = DataOperateState.normal;
        importModel.setImportCallBack(this);
    }

    @Override
    public void onSuccess() {
        mView.onSuccess(type);
        dataOperateState = DataOperateState.normal;
        showToast(R.string.export_success_toast);
    }

    @Override
    public void onFailure(int error) {
        mView.onFailure(type, error);
        dataOperateState = DataOperateState.normal;
        if (error == 0) {
            showToast(R.string.db_not_data_toast);
        } else {
            showToast(R.string.export_failure_toast);
        }
    }

    @Override
    public void copyResult(String path, int result) {
        if (result != 1) {
            Toast.makeText(mContext, path + "," + mContext.getString(R.string.export_failure_toast), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void copyFinish() {
        dataOperateState = DataOperateState.normal;
        showToast(R.string.export_success_toast);
    }

    private void exportLog() {
        File saveFile = new File(USB_PATH);
        if (saveFile.exists() && saveFile.canWrite() && saveFile.canRead()) {
            File serviceFile = new File(SERVICE_LOG);
            File errorFile = new File(CRASHINFO_LOG);
            List<File> files = new ArrayList<>();
            if (serviceFile.exists()) {
                files.add(serviceFile);
            }
            if (errorFile.exists()) {
                files.add(errorFile);
            }
            if (!files.isEmpty()) {
                int size = files.size();
                File[] copyFiles = new File[size];
                for (int i = 0; i < size; i++) {
                    copyFiles[i] = files.get(i);
                }
                dataModel.startCopyFiles(saveFile, copyFiles);
            }
        }
    }

    private void showToast(int srtId) {
        Toast.makeText(mContext, srtId, Toast.LENGTH_SHORT).show();
    }
}
