package com.zeeshan_s.firebasecontactapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Person;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zeeshan_s.firebasecontactapp.Model.ContactModel;
import com.zeeshan_s.firebasecontactapp.R;
import com.zeeshan_s.firebasecontactapp.databinding.ActivityPersonDetailsBinding;

import java.util.HashMap;

public class PersonDetailsActivity extends AppCompatActivity {

    private ActivityPersonDetailsBinding binding;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    String contactId, ownerID, profileUrl, phoneNumber, mailAddress;
    Uri profileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        contactId = intent.getStringExtra("contactID");
        edtTxtSetup(false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference("Profile");

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

//        ------ Edit Option
        binding.editOption.setOnClickListener(view -> {
            edtTxtSetup(true);
            binding.personOptionBucket.setVisibility(View.GONE);
            binding.editOption.setVisibility(View.GONE);
            binding.deleteOption.setVisibility(View.GONE);
            binding.saveOption.setVisibility(View.VISIBLE);

        });

//        ----- Save Option
        binding.saveOption.setOnClickListener(view -> {
            saveEditedFields();
        });

        binding.deleteOption.setOnClickListener(view -> {
//            TODO: this sector is not complete

            AlertDialog.Builder builder = new AlertDialog.Builder(PersonDetailsActivity.this);
            builder.setIcon(R.drawable.warning_icon);
            builder.setMessage("Do you want to delete this contact?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    databaseReference.child("contact").child(contactId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(PersonDetailsActivity.this, "Contact deleted!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PersonDetailsActivity.this, MainActivity.class));
                            }
                        }
                    });
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

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

//        ************ getting Image **********
        binding.personProfileImg.setOnClickListener(view -> {
            Intent imgIntent = new Intent(Intent.ACTION_GET_CONTENT);
            imgIntent.setType("image/*");
            startActivityForResult(imgIntent, 101);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null){
            profileUri = data.getData();
            binding.personProfileImg.setImageURI(profileUri);
        }
    }

    private void saveEditedFields() {

        edtTxtSetup(false);
        binding.personOptionBucket.setVisibility(View.VISIBLE);
        binding.editOption.setVisibility(View.VISIBLE);
        binding.deleteOption.setVisibility(View.VISIBLE);
        binding.saveOption.setVisibility(View.GONE);

        if (binding.personName.getText().toString().equals("")){
            binding.personName.setError("Name cannot be blank");
        }else if (binding.personPhone.getText().toString().equals("")){
            binding.personPhone.setError("Phone number cannot be null");
        }else{
            if (profileUri != null){

                storageReference.child(contactId).putFile(profileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                            storageReference.child(contactId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            profileUrl = String.valueOf(uri);
                                            Glide.with(PersonDetailsActivity.this).load(profileUrl).placeholder(R.drawable.batman).into(binding.personProfileImg);
                                            updateAllData();
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
            else{
                profileUrl= "";
                updateAllData();
            }

        }

    }

    private void updateAllData() {

        String name = binding.personName.getText().toString();
        String phone = binding.personPhone.getText().toString();
        String email = binding.personEmail.getText().toString();
        String address = binding.personAddress.getText().toString();
        String moreInfo = binding.personMoreInfo.getText().toString();


//            Log.i("TAG", "---------"+profileUrl);
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
//                edtTxtSetup(false);
//                binding.personOptionBucket.setVisibility(View.VISIBLE);
//                binding.editOption.setVisibility(View.VISIBLE);
//                binding.deleteOption.setVisibility(View.VISIBLE);
//                binding.saveOption.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    private void edtTxtSetup(boolean b) {
        binding.personName.setEnabled(b);
        binding.personPhone.setEnabled(b);
        binding.personEmail.setEnabled(b);
        binding.personAddress.setEnabled(b);
        binding.personMoreInfo.setEnabled(b);

        binding.personProfileImg.setEnabled(b);
    }

}