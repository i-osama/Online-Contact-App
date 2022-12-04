package com.zeeshan_s.firebasecontactapp.AllAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zeeshan_s.firebasecontactapp.AllViewHolder.UserViewHolder;
import com.zeeshan_s.firebasecontactapp.Model.ContactModel;
import com.zeeshan_s.firebasecontactapp.Model.UserModel;
import com.zeeshan_s.firebasecontactapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    Context context;
    List<ContactModel> contactModelList;

    public UserAdapter(Context context, List<ContactModel> contactModelList) {
        this.context = context;
        this.contactModelList = contactModelList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_recycler, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        ContactModel model = contactModelList.get(position);
        holder.userName.setText(model.getName());
        holder.userNumber.setText(model.getPhone());

        Glide.with(context).load(model.getProfileImgUrl()).placeholder(R.drawable.batman).into(holder.profileImg);
        holder.callOption.setOnClickListener(view -> {
            String phone = model.getPhone();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }
}
