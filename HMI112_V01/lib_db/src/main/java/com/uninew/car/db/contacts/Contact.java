package com.uninew.car.db.contacts;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/9/8 0008.
 */

public class Contact implements Comparable<Contact> {
    private int id;
    private int sign;//0:可以拨出，1：可接听，2：即可拨出也可接听
    private String contact;
    private String phone;
    private String telephone;

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Contact o) {
        int i = this.getContact().charAt(0) - o.getContact().charAt(0);
        if (i == 0) {
            return this.getId() - o.getId();
        }
        return i;
    }
}
