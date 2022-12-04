package com.zeeshan_s.firebasecontactapp.Model;

public class UserModel {
    String user_id, user_name, user_email, user_phone_number, user_password, user_profile_img;

    public UserModel() {
    }

    public UserModel(String user_id, String user_name, String user_email, String user_phone_number, String user_password, String user_profile_img) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone_number = user_phone_number;
        this.user_password = user_password;
        this.user_profile_img = user_profile_img;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_profile_img() {
        return user_profile_img;
    }
}
