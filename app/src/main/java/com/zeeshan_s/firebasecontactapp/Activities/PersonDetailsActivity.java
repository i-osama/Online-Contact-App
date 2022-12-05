package com.zeeshan_s.firebasecontactapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan_s.firebasecontactapp.Model.ContactModel;
import com.zeeshan_s.firebasecontactapp.R;
import com.zeeshan_s.firebasecontactapp.databinding.ActivityPersonDetailsBinding;

import java.util.HashMap;

public class PersonDetailsActivity extends AppCompatActivity {

    private ActivityPersonDetailsBinding binding;
    DatabaseReference databaseReference;

    String contactId, ownerID, profileUrl, phoneNumber, mailAddress;
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
                ownerID = model.getContactOwnerID();
                profileUrl = model.getProfileImgUrl();

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

        binding.editOption.setOnClickListener(view -> {
            edtTxtSetup(true);
            binding.personOptionBucket.setVisibility(View.GONE);
            binding.editOption.setVisibility(View.GONE);
            binding.saveOption.setVisibility(View.VISIBLE);

            saveEditedFields();
        });

        binding.deleteOption.setOnClickListener(view -> {
//            TODO: this sector is not complete
        });

//        ********* Dialing Number ********
        binding.personCallOption.setOnClickListener(view -> {
//            String phone = binding.personPhone.getText().toString();
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

    private void saveEditedFields() {
        String name = binding.personName.getText().toString();
        String phone = binding.personPhone.getText().toString();
        String email = binding.personEmail.getText().toString();
        String address = binding.personAddress.getText().toString();
        String moreInfo = binding.personMoreInfo.getText().toString();

        if (name.equals("")){
            binding.personName.setError("Name cannot be blank");
        }else if (phone.equals("")){
            binding.personPhone.setError("Phone number cannot be null");
        }else{
            if (profileUrl==null){
                profileUrl= "";
            }
            HashMap<String, Object> updateContact = new HashMap<>();
            updateContact.put("contactOwnerID", ownerID);
            updateContact.put("contactID", contactId);
            updateContact.put("name", name);
            updateContact.put("phone", phone);
            updateContact.put("email", email);
            updateContact.put("address", address);
            updateContact.put("moreInfo", moreInfo);
            updateContact.put("profileImgUrl", profileUrl);

//            ------------------- Update contact -------------------
            databaseReference.child("contact").child(contactId).updateChildren(updateContact).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(PersonDetailsActivity.this, "Contact updated successfully!", Toast.LENGTH_SHORT).show();
                    edtTxtSetup(false);
                    binding.personOptionBucket.setVisibility(View.VISIBLE);
                    binding.editOption.setVisibility(View.VISIBLE);
                    binding.saveOption.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }

    private void edtTxtSetup(boolean b) {
        binding.personName.setEnabled(b);
        binding.personPhone.setEnabled(b);
        binding.personEmail.setEnabled(b);
        binding.personAddress.setEnabled(b);
        binding.personMoreInfo.setEnabled(b);
    }

}