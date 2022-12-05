package com.zeeshan_s.firebasecontactapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan_s.firebasecontactapp.Model.ContactModel;
import com.zeeshan_s.firebasecontactapp.R;
import com.zeeshan_s.firebasecontactapp.databinding.ActivityPersonDetailsBinding;

public class PersonDetailsActivity extends AppCompatActivity {

    private ActivityPersonDetailsBinding binding;
    DatabaseReference databaseReference;

    String contactId, phoneNumber, mailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        contactId = intent.getStringExtra("contactID");
        edtTxtSetup(false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("contact").child(contactId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ContactModel model = snapshot.getValue(ContactModel.class);
                binding.personName.setText(model.getName());

                phoneNumber = model.getPhone();
                binding.personPhone.setText(phoneNumber);

                mailAddress = model.getEmail();
                binding.personEmail.setText(mailAddress);
                binding.personAddress.setText(model.getAddress());
                binding.personMoreInfo.setText(model.getMoreInfo());

                Glide.with(PersonDetailsActivity.this).load(model.getProfileImgUrl()).placeholder(R.drawable.batman).into(binding.personProfileImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //        ------------ Button Settings -----------
        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(PersonDetailsActivity.this, MainActivity.class));
        });

//        ********* Dialing Number ********
        binding.personCallOption.setOnClickListener(view -> {
//            String phone = binding.personPhone.getText().toString();
//            String phone = phoneNumber;
//            Log.i("TAG", "phone:: "+phone);
            Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(intent2);
        });


//        ********* Send SMS ********
        binding.personMessageOption.setOnClickListener(view -> {
            String phone = binding.personPhone.getText().toString();
            Intent intent2 =  new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null));
            startActivity(intent2);
        });


//        ********* Send Mail ********
        binding.personMailOption.setOnClickListener(view -> {
//            String phone = binding.personEmail.getText().toString();
            Intent intent2 =  new Intent(Intent.ACTION_VIEW, Uri.fromParts("mailto", mailAddress, null));
            startActivity(intent2);
        });



    }

    private void edtTxtSetup(boolean b) {
        binding.personName.setEnabled(b);
        binding.personPhone.setEnabled(b);
        binding.personEmail.setEnabled(b);
        binding.personAddress.setEnabled(b);
        binding.personMoreInfo.setEnabled(b);
    }

}