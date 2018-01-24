package com.uninew.car.db.contacts;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.uninew.car.db.main.DBMetaData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class ContactLocalDataSource implements ContactLocalSource {

    private static volatile ContactLocalDataSource INSTANCE;
    private WeakReference<Context> wp;//防止context内存泄露的优化
    /**
     * contentprovider连接文本信息的uri
     **/
    private static final Uri CONTENT_URI = Uri.parse("content://" + DBMetaData.AUTHORITIES + "/"
            + DBMetaData.ContactEntry.TABLE_NAME + "/");
    private ContentResolver resolver;//ContentProvider操作类


    private ContactLocalDataSource(Context context) {
        wp = new WeakReference<>(context);
        resolver = wp.get().getContentResolver();
    }

    public static ContactLocalDataSource getInstance(Context context) {
        if (INSTANCE != null) {

        } else {
            synchronized (ContactLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ContactLocalDataSource(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAllDBDatas(@NonNull LoadDBDataCallBack callBack) {
        List<Contact> contacts = new ArrayList<>();
        Cursor c = resolver.query(CONTENT_URI, null, null, null, DBMetaData.ContactEntry.CONTACT_NAME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                Contact contact = new Contact();
                contact.setPhone(c.getString(c.getColumnIndex(DBMetaData.ContactEntry.PHONE)));
                contact.setContact(c.getString(c.getColumnIndex(DBMetaData.ContactEntry.CONTACT_NAME)));
                contact.setId(c.getInt(c.getColumnIndex(DBMetaData.ContactEntry._ID)));
                contact.setTelephone(c.getString(c.getColumnIndex(DBMetaData.ContactEntry.TELEPHONE)));
                contact.setSign(c.getInt(c.getColumnIndex(DBMetaData.ContactEntry.SIGN)));
                contacts.add(contact);
            }
        }
        if (c != null) {
            c.close();
        }
        if (contacts.isEmpty()) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(contacts);
        }
    }

    @Override
    public void getDBData(int _id, @NonNull GetDBDataCallBack callBack) {
        Contact contact = null;
        String selection = DBMetaData.ContactEntry._ID + "=?";
        String[] selectionArgs = {
                _id + ""
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.ContactEntry.CONTACT_NAME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            contact = new Contact();
            contact.setPhone(c.getString(c.getColumnIndex(DBMetaData.ContactEntry.PHONE)));
            contact.setContact(c.getString(c.getColumnIndex(DBMetaData.ContactEntry.CONTACT_NAME)));
            contact.setId(c.getInt(c.getColumnIndex(DBMetaData.ContactEntry._ID)));
            contact.setTelephone(c.getString(c.getColumnIndex(DBMetaData.ContactEntry.TELEPHONE)));
            contact.setSign(c.getInt(c.getColumnIndex(DBMetaData.ContactEntry.SIGN)));
        }
        if (c != null) {
            c.close();
        }
        if (contact == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(contact);
        }
    }

    @Override
    public void saveDBData(@NonNull Contact... contacts) {
        if (contacts != null && contacts.length > 0) {
            for (Contact contact : contacts) {
                final ContentValues values = new ContentValues();
                values.put(DBMetaData.ContactEntry.CONTACT_NAME, contact.getContact());
                values.put(DBMetaData.ContactEntry.PHONE, contact.getPhone());
                values.put(DBMetaData.ContactEntry.TELEPHONE, contact.getTelephone());
                values.put(DBMetaData.ContactEntry.SIGN,contact.getSign());
                getContactByPhone(contact.getPhone(), new GetContactCallBack() {
                    @Override
                    public void onDBBaseDataLoaded(Contact contact) {
                        String selection = DBMetaData.ContactEntry.PHONE + "=?";
                        String[] selectionArgs = {
                                contact.getPhone()
                        };
                        resolver.update(CONTENT_URI,values,selection,selectionArgs);
                    }

                    @Override
                    public void onDataNotAailable() {
                        resolver.insert(CONTENT_URI, values);
                        values.clear();
                    }
                });

            }
        }
    }

    @Override
    public void deleteAllDBDatas() {
        resolver.delete(CONTENT_URI, null, null);
    }

    @Override
    public void deleteDBData(@NonNull int id) {
        String selection = DBMetaData.ContactEntry._ID + "=?";
        String[] selectionArgs = {
                id + ""
        };
        resolver.delete(CONTENT_URI, selection, selectionArgs);
    }

    @Override
    public void getContactByPhone(String phone, GetContactCallBack callBack) {
        Contact contact = null;
        String selection = DBMetaData.ContactEntry.PHONE + "=?";
        String[] selectionArgs = {
              phone
        };
        Cursor c = resolver.query(CONTENT_URI, null, selection, selectionArgs, DBMetaData.ContactEntry.CONTACT_NAME + DBMetaData.ASC);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            contact = new Contact();
            contact.setPhone(c.getString(c.getColumnIndex(DBMetaData.ContactEntry.PHONE)));
            contact.setContact(c.getString(c.getColumnIndex(DBMetaData.ContactEntry.CONTACT_NAME)));
            contact.setId(c.getInt(c.getColumnIndex(DBMetaData.ContactEntry._ID)));
            contact.setTelephone(c.getString(c.getColumnIndex(DBMetaData.ContactEntry.TELEPHONE)));
            contact.setSign(c.getInt(c.getColumnIndex(DBMetaData.ContactEntry.SIGN)));
        }
        if (c != null) {
            c.close();
        }
        if (contact == null) {
            callBack.onDataNotAailable();
        } else {
            callBack.onDBBaseDataLoaded(contact);
        }
    }
}
