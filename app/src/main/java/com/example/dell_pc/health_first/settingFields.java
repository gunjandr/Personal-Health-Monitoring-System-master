package com.example.dell_pc.health_first;


/**
 * Created by Hamed on 4/25/2017.
 */

public class settingFields {

    int enable;
    String phone;
    String email;
    String method;


    public settingFields()
    {

    }


    public settingFields(int enable,String phone,String email,String method)
    {
        this.enable = enable;
        this.phone=phone;
        this.email = email;
        this.method=method;


    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}

