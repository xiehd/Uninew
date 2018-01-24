package com.uninew.car.phone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.uninew.car.R;
import com.uninew.car.audio.TtsUtil;
import com.uninew.car.db.contacts.Contact;
import com.uninew.car.db.contacts.ContactLocalDataSource;
import com.uninew.car.db.contacts.ContactLocalSource;
import com.uninew.car.db.dialler.Dialer;
import com.uninew.car.db.dialler.DialerLocalDataSource;
import com.uninew.car.db.dialler.DialerLocalSource;
import com.uninew.car.dialog.PromptDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class RecordPresenter implements RecordContract.Presenter,
        DialerLocalSource.LoadDialersCallback {

    private RecordContract.View mView;
    private Context mContext;
    //    private DialerLocalSource mDBDialers;
    private ContentResolver resolver;

    private static final int CALLS_WHAT = 0x01;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what){
                case CALLS_WHAT:
                    List<Dialer> dialers= (List<Dialer>) msg.obj;
                    mView.showDailers(dialers);
                    break;
            }
        }
    };

    public RecordPresenter(RecordContract.View view, Context context) {
        this.mContext = context;
        this.mView = view;
//        mDBDialers = DialerLocalDataSource.getInstance(mContext);
        mView.setPresenter(this);
        resolver = context.getContentResolver();
    }

    @Override
    public void start() {
//        mDBDialers.getAllDBDatas(this);
        new Thread(new ReadCallsRunnable()).start();
    }

    @Override
    public void stop() {

    }

    //    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
//    int type = cursor.getInt(cursor.getColumnIndex("type"));//通话类型，1 来电 .INCOMING_TYPE；2 已拨 .OUTGOING_；3 未接 .MISSED_
//    String number = cursor.getString(cursor.getColumnIndex("number"));// 电话号码
//    String name = cursor.getString(cursor.getColumnIndex("name"));//联系人
//    long date = cursor.getLong(cursor.getColumnIndex("date"));//通话时间，即可以用getString接收，也可以用getLong接收
//    String formatDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date(date));
//    int duration = cursor.getInt(cursor.getColumnIndex("duration"));//通话时长，单位：秒
//    String msgObj = "\nID：" + _id + "\n类型：" + type + "\n号码：" + number + "\n名称：" + name + "\n时间：" + formatDate + "\n时长：" + duration;
    private Uri uri = CallLog.Calls.CONTENT_URI;//等价于【Uri.parse("content://call_log/calls")】

    private class ReadCallsRunnable implements Runnable {

        @Override
        public void run() {
//            resolver.unregisterContentObserver(this);//注意：增删改通话记录后由于数据库发生变化，所以系统会在修改后再发一条广播，这时会重新回调onChange方法
            //最终导致的结果就是：一次来电后删除了多条甚至全部通话记录。为防止这种循环启发，必须在更改前就取消注册！事实上，注册的代码应该放在广播接收者中。
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            List<Dialer> dialers = new ArrayList<>();
            Cursor cursor = resolver.query(uri, null, null, null, "_id desc limit 1");//按_id倒序排序后取第一个，即：查询结果按_id从大到小排序，然后取最上面一个（最近的通话记录）
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Dialer dialer = new Dialer();
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));//通话类型，1 来电 .INCOMING_TYPE；2 已拨 .OUTGOING_；3 未接 .MISSED_
                    String number = cursor.getString(cursor.getColumnIndex("number"));// 电话号码
                    String name = cursor.getString(cursor.getColumnIndex("name"));//联系人
                    long date = cursor.getLong(cursor.getColumnIndex("date"));//通话时间，即可以用getString接收，也可以用getLong接收
                    String formatDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date(date));
                    int duration = cursor.getInt(cursor.getColumnIndex("duration"));//通话时长，单位：秒
                    dialer.setCallDate(formatDate);
                    dialer.setCallTime(duration);
                    dialer.setContact(name);
                    dialer.setPhone(number);
                    dialer.setId(_id);
                    dialer.setDiallerState(type);
                    dialers.add(dialer);
                }
            }
            if(cursor != null){
                cursor.close();
            }

            if(dialers.isEmpty()){

            }else {
                Message msg = Message.obtain();
                msg.what = CALLS_WHAT;
                msg.obj = dialers;
                mHandler.sendMessage(msg);
            }
        }
    }

//    private class CallsContentObserver extends ContentObserver {
//        /**
//         * Creates a content observer.
//         *
//         * @param handler The handler to run {@link #onChange} on, or null if none.
//         */
//        public CallsContentObserver(Handler handler) {
//            super(handler);
//        }
//    }

    @Override
    public void delectDailer(int id) {
//        mDBDialers.deleteDBData(id);
//        mDBDialers.getAllDBDatas(this);
    }

    @Override
    public void call(@NonNull String phone) {
        if(TextUtils.isEmpty(phone)){
            PromptDialog dialog = new PromptDialog(mContext);
            dialog.showToast(R.string.phone_null, 3000);
            dialog.show();
            return;
        }
        ContactLocalDataSource.getInstance(mContext).getContactByPhone(phone, new ContactLocalSource.GetContactCallBack() {
            @Override
            public void onDBBaseDataLoaded(final Contact contact) {
                if (contact.getSign() == 0 || contact.getSign() == 2) {
                    PromptDialog dialog = new PromptDialog(mContext);
                    dialog.isSystemAlert();
                    dialog.show();
                    dialog.setContent(R.string.phone_call_advisory);
                    dialog.setOnDialogClickListener(R.string.confirm, R.string.back, new PromptDialog.OnDialogClickListener() {
                        @Override
                        public void onLeft(PromptDialog dialog) {
                            dialog.cancel();
                        }

                        @Override
                        public void onRight(PromptDialog dialog) {
                            PhoneModel.callNumber(contact.getPhone(),mContext);
                            dialog.cancel();
                        }
                    });
                } else {
                    PromptDialog dialog = new PromptDialog(mContext);
                    dialog.isSystemAlert();
                    TtsUtil.getInstance(mContext).speak(
                            mContext.getString(R.string.phone_permissions_not));
                    dialog.show();
                    dialog.showToast(R.string.phone_permissions_not, 3000);
                }
            }

            @Override
            public void onDataNotAailable() {
                PromptDialog dialog = new PromptDialog(mContext);
                dialog.isSystemAlert();
                TtsUtil.getInstance(mContext).speak(
                        mContext.getString(R.string.phone_permissions_not));
                dialog.show();
                dialog.showToast(R.string.phone_permissions_not, 3000);
            }
        });
    }

    @Override
    public void onDBBaseDataLoaded(List<Dialer> buffers) {
        mView.showDailers(buffers);
    }

    @Override
    public void onDataNotAailable() {
        mView.showDailers(null);
    }
}
