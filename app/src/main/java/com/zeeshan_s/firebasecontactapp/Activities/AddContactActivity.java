package com.zeeshan_s.firebasecontactapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zeeshan_s.firebasecontactapp.R;
import com.zeeshan_s.firebasecontactapp.databinding.ActivityAddContactBinding;

import java.util.HashMap;

public class AddContactActivity extends AppCompatActivity {

    private ActivityAddContactBinding binding;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String myUserId, newContactID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myUserId = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        binding.addConSaveOption.setOnClickListener(view -> {
            if (binding.personName.equals("")){binding.personName.setError("Field cannot be empty!");}
            else if (binding.personPhone.equals("")){binding.personPhone.setError("Field cannot be empty!");}
            else if (binding.personEmail.equals("")){binding.personEmail.setError("Field cannot be empty!");}
            else if (binding.personAddress.equals("")){binding.personAddress.setError("Field cannot be empty!");}
            else if (binding.addContactMoreInfo.equals("")){binding.addContactMoreInfo.setError("Field cannot be empty!");}
            else {
//                contactOwnerID, contactID, name, phone, email, address, moreInfo, profileImgUrl;
                String name = binding.personName.getText().toString();
                String phone = binding.personPhone.getText().toString();
                String email = binding.personEmail.getText().toString();
                String address = binding.personAddress.getText().toString();
                String moreInfo = binding.addContactMoreInfo.getText().toString();

                addContact(myUserId, name, phone, email, address, moreInfo);
            }
        });
    }

    private void addContact(String myUserId, String name, String phone, String email, String address, String moreInfo) {
        newContactID = databaseReference.push().getKey();

        //            --------------- Passing Data to the firebase -----------------
        HashMap<String, Object> contacts = new HashMap<>();
        contacts.put("contactOwnerID", myUserId);
        contacts.put("contactID", newContactID);
        contacts.put("name", name);
        contacts.put("phone", phone);
        contacts.put("email", email);
        contacts.put("address", address);
        contacts.put("moreInfo", moreInfo);
        contacts.put("profileImgUrl", "");
        databaseReference.child("contact").child(newContactID).setValue(contacts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddContactActivity.this, "New contact added!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(AddContactActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}