package com.zeeshan_s.firebasecontactapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zeeshan_s.firebasecontactapp.R;
import com.zeeshan_s.firebasecontactapp.databinding.ActivityAddContactBinding;

import java.util.HashMap;

public class AddContactActivity extends AppCompatActivity {

    private ActivityAddContactBinding binding;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    String myUserId, newContactID, profileUrl = "";
    Uri profileImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myUserId = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("Profile");

        binding.personProfileImg.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 102);
        });

        binding.backBtn.setOnClickListener(view -> {
            startActivity(new Intent(AddContactActivity.this, MainActivity.class));
        });

//        -------------- Save Button --------------
        binding.addConSaveOption.setOnClickListener(view -> {
            if (binding.personName.equals("")){binding.personName.setError("Field cannot be empty!");}
            else if (binding.personPhone.equals("")){binding.personPhone.setError("Field cannot be empty!");}
            else {
//                contactOwnerID, contactID, name, phone, email, address, moreInfo, profileImgUrl;
                String name = binding.personName.getText().toString();
                String phone = binding.personPhone.getText().toString();
                String email = binding.personEmail.getText().toString();
                if (email.equals("")){email = "";}
                String address = binding.personAddress.getText().toString();
                if (address.equals("")){address = "";}
                String moreInfo = binding.addContactMoreInfo.getText().toString();
                if (moreInfo.equals("")){moreInfo = "";}

                addContact(myUserId, name, phone, email, address, moreInfo);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK && data != null){
            profileImgUri = data.getData();
            binding.personProfileImg.setImageURI(profileImgUri);
        }

    }

    private void addContact(String myUserId, String name, String phone, String email, String address, String moreInfo) {
        newContactID = databaseReference.push().getKey();

        if (profileImgUri != null) {

            storageReference.child(newContactID).putFile(profileImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()){
                        storageReference.child(newContactID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        profileUrl = String.valueOf(uri);
                                        Glide.with(AddContactActivity.this).load(profileUrl).placeholder(R.drawable.batman).into(binding.personProfileImg);
                                        storeContactData(myUserId, newContactID, name, phone, email, address, moreInfo, profileUrl);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                }
            });
        }
        else {
            profileUrl= "";
            storeContactData(myUserId, newContactID, name, phone, email, address, moreInfo, profileUrl);
        }


    }

    private void storeContactData(String myUserId, String newContactID, String name, String phone, String email, String address, String moreInfo, String profileUrl) {


        //            --------------- Passing Data to the firebase -----------------
        HashMap<String, Object> contacts = new HashMap<>();
        contacts.put("contactOwnerID", myUserId);
        contacts.put("contactID", newContactID);
        contacts.put("name", name);
        contacts.put("phone", phone);
        contacts.put("email", email);
        contacts.put("address", address);
        contacts.put("moreInfo", moreInfo);
        contacts.put("profileImgUrl", profileUrl);
        databaseReference.child("contact").child(newContactID).setValue(contacts).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddContactActivity.this, "New contact added!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(AddContactActivity.this, MainActivity.class));
                        finish();
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


}