package com.uninew.car.db.paramSet;

/**
 * Created by Administrator on 2017/10/14 0014.
 */

public class ParamSetting {
    private int id;
    private int key;
    private int length;
    private String value;

    public ParamSetting() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "ParamSetting{" +
                "id=" + id +
                ", key=" + key +
                ", length=" + length +
                ", value='" + value + '\'' +
                '}';
    }
}
