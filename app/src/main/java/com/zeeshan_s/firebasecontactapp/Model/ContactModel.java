package com.zeeshan_s.firebasecontactapp.Model;

public class ContactModel {
    String contactOwnerID, contactID, name, phone, email, address, moreInfo, profileImgUrl;

    public ContactModel() {
    }

    public ContactModel(String contactOwnerID, String contactID, String name, String phone, String email, String address, String moreInfo, String profileImgUrl) {
        this.contactOwnerID = contactOwnerID;
        this.contactID = contactID;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.moreInfo = moreInfo;
        this.profileImgUrl = profileImgUrl;
    }

    public String getContactOwnerID() {
        return contactOwnerID;
    }

    public String getContactID() {
        return contactID;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }
}
