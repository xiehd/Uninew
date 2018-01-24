package com.uninew.car.db.main;

import android.app.Service;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

/**
 * 表结构类
 */
public final class DBMetaData {

    private DBMetaData() {
    }

    public static final String ASC = " asc";

    public static final String OR = " or ";

    public static final String AND = " and ";

    // 默认的排序方法
    public static final String DEFAULT_SORT_ORDER = "_id desc";
    /**
     * contentprovider的authorities,并保证于配置文件中一致
     **/
    public static final String AUTHORITIES = "com.uninew.car.MainContentProvider";

    public static abstract class OrderEntry implements BaseColumns {
        public static final String TABLE_NAME = "order_table";

        public static final String BUSINESS_ID = "businessId";
        public static final String ORDER_STATE = "orderState";
        public static final String BUSINESS_TYPE = "businessType";
        public static final String RECEIVE_TIME = "receiveTime";
        public static final String NEED_TIME = "needTime";
        public static final String PASSENGER_LONGITUDE = "passengerLongitude";
        public static final String PASSENGER_LATITUDE = "passengerLatitude";
        public static final String TARGET_LONGITUDE = "targetLongitude";
        public static final String TARGET_LATITUDE = "targetLatitude";
        public static final String SERVICE_CHARGE = "serviceCharge";
        public static final String PASSENGER_PHONE_NUMBER = "passengerPhoneNumber";
        public static final String BUSINESS_DESCRIPTION = "businessDescription";
        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + BUSINESS_ID + " INTEGER UNIQUE," +
                ORDER_STATE + " INTEGER," + NEED_TIME + " TIMESTAMP," + RECEIVE_TIME +
                " TIMESTAMP," + BUSINESS_TYPE + " INTEGER," + PASSENGER_LONGITUDE
                + " REAL," + PASSENGER_LATITUDE + " REAL," + TARGET_LATITUDE +
                " REAL," + TARGET_LONGITUDE + " REAL," + PASSENGER_PHONE_NUMBER + " TEXT,"
                + BUSINESS_DESCRIPTION + " TEXT," + SERVICE_CHARGE + " REAL" + ");";
    }

    public static abstract class RevenueEntry implements BaseColumns {
        public static final String TABLE_NAME = "revenue";
        /* 空车转重车的位置信息 */
        public static final String  UP_CAR_LOCATION = "upCarLocation";
        /* 重车转空车的位置信息 */
        public static final String DOWN_LOCATION = "downLocation";
        /* 运营ID */
        public static final String REVENUEID = "revenueId";
        /* 评价ID */
        public static final String EVALUATION_ID = "evaluationId";
        /* 评价选项 */
        public static final String EVALUATION = "evaluation";
        /* 评价选项扩展 */
        public static final String EVALUATION_EXTENDED = "evaluationExtended";
        /* 电召订单Id */
        public static final String ORDER_ID = "orderId";
        /* 运营数据*/
        public static final String REVENUE_DATAS = "revenueDatas";
        public static final String TIME = "TIME";
        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ REVENUEID + " INTEGER,"  + EVALUATION_ID
                + " INTEGER,"   + EVALUATION + " INTEGER," + TIME + " TIMESTAMP UNIQUE,"
                + EVALUATION_EXTENDED + " INTEGER," + ORDER_ID + " INTEGER," +UP_CAR_LOCATION
                + " BLOB,"  + DOWN_LOCATION+ " BLOB," + REVENUE_DATAS + " BLOB" + ");";
    }

    public static abstract class MessageEntry implements BaseColumns {
        public static final String TABLE_NAME = "message";
        public static final String TIME = "time";
        public static final String MESSAGE_TYPE = "message_type";
        public static final String ANSWER_STATE = "answer_state";
        public static final String CONTENT = "content";
        public static final String MESSAGE_ID = "messageId";
        public static final String ANSWER_ID = "answerId";
        public static final String READ_STATE = "readState";
        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + TIME + " TIMESTAMP," + MESSAGE_TYPE
                + " INTEGER," + READ_STATE + " INTEGER," + MESSAGE_ID + " INTEGER," + ANSWER_ID + " INTEGER,"
                + ANSWER_STATE + " INTEGER," + CONTENT + " TEXT" + ");";
    }

    public static abstract class AnswerEntry implements BaseColumns {
        public static final String TABLE_NAME = "answer";
        public static final String MSG_ID = "msgId";
        public static final String ANSWER_ID = "answerId";
        public static final String ANSWER_CONTENT = "answerContent";

        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + MSG_ID + " INTEGER," + ANSWER_ID
                + " INTEGER," + ANSWER_CONTENT + " TEXT" + ");";
    }

    public static abstract class AlarmMessageEntry implements BaseColumns {
        public static final String TABLE_NAME = "alarmmessage";
        public static final String ALARM_TYPE = "alarm_type";
        public static final String DATE = "date";
        public static final String START_TIME = "start_time";
        public static final String STOP_TIME = "stop_time";
        public static final String CONTINUED_TIME = "continued_time";
        public static final String CONTENT = "content";

        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + DATE + " TIMESTAMP," + START_TIME +
                " TIMESTAMP," + STOP_TIME + " TIMESTAMP," + ALARM_TYPE + " INTEGER," + CONTINUED_TIME
                + " INTEGER," + CONTENT + " TEXT" + ");";
    }


    public static abstract class SettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "settings";
        public static final String NAME = "name";
        public static final String CONTENT = "content";
        public static final String ATTACH_CONTENT = "attach_content";
        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT," + ATTACH_CONTENT
                + " INTEGER," + CONTENT + " TEXT" + ");";
    }

    public static abstract class AttendanceEntry implements BaseColumns {

        public static final String TABLE_NAME = "attendance";
        public static final String ACTION_TIMES = "action_times";
        public static final String DRIVING_MILEAGE = "driving_mileage";
        public static final String PASSENGER_MILEAGE = "passenger_mileage";
        public static final String URSE_NAME = "urse_name";
        public static final String IMG_AVATAR = "img_avatar";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String ATTENDANCE_STATE = "attendance_state";
        public static final String JOB_NUMBER = "job_number";
        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + ACTION_TIMES + " INTEGER," + DRIVING_MILEAGE
                + " REAL," + PASSENGER_MILEAGE + " REAL," + URSE_NAME + " TEXT," + IMG_AVATAR + " BLOB,"
                + START_TIME + " TIMESTAMP," + END_TIME + " TIMESTAMP," + ATTENDANCE_STATE + " INTEGER,"
                + JOB_NUMBER + " TEXT" + ");";

    }

    public static abstract class DiallerEntry implements BaseColumns {

        public static final String TABLE_NAME = "dialler";
        public static final String CONTACT = "contact";
        public static final String PHONE = "phone";
        public static final String CALL_TIME = "call_time";
        public static final String CALL_DURATION = "call_duration";
        public static final String DIALLER_STATE = "dialler_state";

        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + DIALLER_STATE + " INTEGER," + CALL_DURATION
                + " INTEGER," + CONTACT + " TEXT," + CALL_TIME + " TIMESTAMP,"
                + PHONE + " TEXT" + ");";
    }

    public static abstract class LocationEntry implements BaseColumns {

        public static final String TABLE_NAME = "location";
        public static final String SERIAL_NUMBER = "serialNumber";
        public static final String UPLOAD_TIME = "uploadTime";
        public static final String ALARM = "alarm";
        public static final String STATE = "state";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String SPEED = "speed";
        public static final String DIRECTION = "direction";
        public static final String TIME = "time";
        public static final String ELEVATION = "elevation";
        public static final String ADDITIONAL_INFO = "additionalInfo";


        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + SERIAL_NUMBER + " INTEGER," + UPLOAD_TIME
                + " TIMESTAMP," + ALARM + " INTEGER," + STATE + " INTEGER," + LATITUDE + " REAL,"
                + LONGITUDE + " REAL," + SPEED + " REAL," + DIRECTION + " REAL," + ELEVATION
                + " REAL," + ADDITIONAL_INFO + " BLOB," + TIME + " TIMESTAMP" + ");";
    }

    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String CONTACT_NAME = "contact_name";
        public static final String SIGN = "sign";
        public static final String PHONE = "phone";
        public static final String TELEPHONE = "telephone";


        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTACT_NAME + " TEXT," + PHONE
                + " TEXT,"+ SIGN + " INTEGER," + TELEPHONE + " TEXT" + ");";
    }

    public static abstract class DriverMessageEntry implements BaseColumns {
        public static final String TABLE_NAME = "driverMessage";
        public static final String DRIVER_CERTIFICATE = "driverCertificate";
        public static final String DRIVER_NAME = "driverName";
        public static final String DRIVER_PICTURE = "driverPicture";
        public static final String DRIVER_STAR = "driverStar";

        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + DRIVER_CERTIFICATE + " TEXT UNIQUE," + DRIVER_PICTURE
                + " TEXT," + DRIVER_STAR + " INTEGER," + DRIVER_NAME + " TEXT" + ");";
    }

    public static abstract class PlatformEntry implements BaseColumns {
        public static final String TABLE_NAME = "platform";
        public static final String TCP_ID = "tcp_id";
        public static final String MAIN_IP = "main_ip";
        public static final String MAIN_PORT = "main_port";
        public static final String SPARE_IP = "spare_ip";
        public static final String SPARE_PORT = "spare_port";

        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + TCP_ID + " INTEGER UNIQUE," + MAIN_IP
                + " TEXT," + MAIN_PORT + " INTEGER," + SPARE_IP + " TEXT," + SPARE_PORT + " TEXT" + ");";
    }

    public static abstract class SignsEntry implements BaseColumns {
        public static final String TABLE_NAME = "signs";
        public static final String ALARM = "alarm";
        public static final String STATE = "state";
        public static final String ELEVATION = "elevation";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String SPEED = "speed";
        public static final String DIRECTION = "direction";
        public static final String TIME = "time";
        public static final String SIGN_STATE = "sign_state";
        public static final String PLATE_NUMBER = "plate_number";
        public static final String BUSINESS_LICENSE = "business_license";
        public static final String DRIVER_CERTIFICATE = "driver_certificate";
        public static final String BOOT_TIME = "Boot_time";
        public static final String SHUTDOWN_TIME = "Shutdown_time";
        public static final String RUN_MILEAGE = "run_mileage";
        public static final String ON_DUTY_MILEAGE = "on_duty_mileage";
        public static final String CAR_TIMES = "car_times";
        public static final String TIMING_TIME = "timing_time";
        public static final String METERKVALUE = "meterKValue";
        public static final String AMOUNT = "amount";
        public static final String CASH_CARD_AMOUNT = "cash_card_amount";
        public static final String CARD_TIMES = "card_times";
        public static final String NIGHT_MILEAGE = "night_mileage";
        public static final String ALL_MILEAGE = "all_mileage";
        public static final String REVENUE_ALL_MILEAGE = "revenue_all_mileage";
        public static final String REVENUE_TIMES = "revenue_times";
        public static final String PRICE = "price";
        public static final String SIGN_OUT_TYPE = "sign_out_type";
        public static final String EXTENDED_ATTRIBUTES = "extended_attributes";

        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + ALARM + " INTEGER," + STATE
                + " INTEGER," + LATITUDE + " REAL," + LONGITUDE + " REAL," + ELEVATION + " REAL,"+ SPEED
                + " REAL," + DIRECTION + " REAL," + EXTENDED_ATTRIBUTES + " BLOB,"
                + SIGN_STATE + " INTEGER," + PLATE_NUMBER + " TEXT," + BUSINESS_LICENSE
                + " TEXT," + DRIVER_CERTIFICATE + " TEXT," + BOOT_TIME + " TIMESTAMP," + METERKVALUE + " TIMESTAMP,"
                + SHUTDOWN_TIME + " TIMESTAMP," + RUN_MILEAGE + " REAL," + ON_DUTY_MILEAGE
                + " REAL," + CAR_TIMES + " INTEGER," + TIMING_TIME + " TEXT," + AMOUNT + " REAL,"
                + CASH_CARD_AMOUNT + " REAL," + CARD_TIMES + " INTEGER," + NIGHT_MILEAGE
                + " REAL," + ALL_MILEAGE + " REAL," + REVENUE_ALL_MILEAGE + " REAL,"
                + PRICE + " REAL," + REVENUE_TIMES + " INTEGER," + SIGN_OUT_TYPE + " INTEGER,"
                + TIME + " TIMESTAMP" + ");";
    }

    public static abstract class ParamSetEntry implements BaseColumns{
        public static final String TABLE_NAME = "paramSet";
        public static final String KEY = "key";
        public static final String VALUE = "value";
        public static final String LANGTH = "langth";
        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY
                + " INTEGER UNIQUE,"  + LANGTH + " INTEGER,"+ VALUE + " TEXT" + ");";
    }

    public static abstract class PhotoEntry implements BaseColumns{
        public static final String TABLE_NAME = "photo";
        public static final String ISU_ID = "isu_id";
        public static final String CODING_TYPE = "coding_type";
        public static final String CAR_NAME = "car_name";
        public static final String REVENUE_ID = "revenue_id";
        public static final String REASON = "reason";
        public static final String TIME = "time";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String FILE_ID = "file_id";
        public static final String PHOTO_LENGTH = "photo_length";
        public static final String PHOTO_BUFFERS = "photo_buffers";
        public static final String CAMERA_ID = "camera_id";
        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + TIME
                + " INTEGER UNIQUE,"  + ISU_ID + " INTEGER," + CODING_TYPE + " INTEGER,"
                + REVENUE_ID + " INTEGER," + REASON + " INTEGER," + LATITUDE + " REAL,"
                + LONGITUDE + " REAL,"+ FILE_ID + " INTEGER,"+ PHOTO_LENGTH + " INTEGER,"
                + CAMERA_ID + " INTEGER,"+ PHOTO_BUFFERS + " BLOB,"+ CAR_NAME + " TEXT" + ");";
    }

    public static abstract class AudioEntry implements BaseColumns{
        public static final String TABLE_NAME = "audio";
        public static final String ISU_ID = "isu_id";
        public static final String CODING_TYPE = "coding_type";
        public static final String CAR_NAME = "car_name";
        public static final String REVENUE_ID = "revenue_id";
        public static final String REASON = "reason";
        public static final String START_TIME = "start_time";
        public static final String START_LATITUDE = "start_latitude";
        public static final String START_LONGITUDE = "start_longitude";
        public static final String END_TIME = "end_time";
        public static final String END_LATITUDE = "end_latitude";
        public static final String END_LONGITUDE = "end_longitude";
        public static final String FILE_ID = "file_id";
        public static final String AUDIO_LENGTH = "audio_length";
        public static final String AUDIO_BUFFERS = "audio_buffers";
        public static final String CAMERA_ID = "camera_id";
        /**
         * 创建表的SQL语句
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + START_TIME + " INTEGER UNIQUE,"
                + END_TIME + " INTEGER,"  + ISU_ID + " INTEGER," + CODING_TYPE + " INTEGER,"
                + REVENUE_ID + " INTEGER," + REASON + " INTEGER," + START_LATITUDE + " REAL,"
                + START_LONGITUDE + " REAL,"+ END_LATITUDE + " REAL," + END_LONGITUDE + " REAL,"
                + FILE_ID + " INTEGER,"+ AUDIO_LENGTH + " INTEGER," + CAMERA_ID + " INTEGER,"
                + AUDIO_BUFFERS + " BLOB,"+ CAR_NAME + " TEXT" + ");";
    }
}
