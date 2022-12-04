package com.zeeshan_s.firebasecontactapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan_s.firebasecontactapp.AllAdapter.UserAdapter;
import com.zeeshan_s.firebasecontactapp.Model.ContactModel;
import com.zeeshan_s.firebasecontactapp.R;
import com.zeeshan_s.firebasecontactapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    List<ContactModel> contactList;
    DatabaseReference userDatabaseRef;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactList = new ArrayList<>();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference("contact");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ContactModel contact = dataSnapshot.getValue(ContactModel.class);

                    if (!contact.getContactOwnerID().equals(firebaseUser.getUid())) {
                        contactList.add(contact);
                    }
                }
//                ----------------------- Recycler Adapter --------------
                UserAdapter adapter = new UserAdapter(MainActivity.this, contactList);
                binding.mainContactRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        ---------- Header setting --------
        binding.mainCurrentUsrImg.setOnClickListener(view -> {
//            TODO: this will take user to the profile activity
        });

        binding.mainAddContactOption.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddContactActivity.class));
        });

    }

}