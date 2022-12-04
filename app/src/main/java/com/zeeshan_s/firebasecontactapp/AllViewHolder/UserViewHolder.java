package com.zeeshan_s.firebasecontactapp.AllViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeshan_s.firebasecontactapp.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public TextView userName, userNumber;
    public ImageView profileImg, callOption;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.userName);
        userNumber = itemView.findViewById(R.id.userPhoneNumber);
        profileImg = itemView.findViewById(R.id.userProfileImg);
        callOption = itemView.findViewById(R.id.mainCallOption);
    }
}
