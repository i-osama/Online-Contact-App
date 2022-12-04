package com.zeeshan_s.firebasecontactapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zeeshan_s.firebasecontactapp.R;
import com.zeeshan_s.firebasecontactapp.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference userDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("user");

        binding.regiAcLoginOption.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

//        ------------ Registration button setting ---------------
        binding.regiAcRegisterBtn.setOnClickListener(view -> {

            String name = binding.regiName.getText().toString().trim();
            String email = binding.regiEmail.getText().toString().trim();
            String phone = binding.regiPhone.getText().toString().trim();

            String password = binding.regiPassword.getText().toString();
            String rePassword = binding.regiRePassword.getText().toString();

            if (name.equals("")){
                binding.regiName.setError("Name cannot be empty!");
            } else if (email.equals("")){binding.regiEmail.setError("Email cannot be empty!");}
            else if (phone.equals("")){binding.regiPhone.setError("Phone cannot be empty!");}
            else if (password.equals("")){binding.regiPassword.setError("Please enter a password!");}
            else if (rePassword.equals("")){binding.regiRePassword.setError("Please re-enter the password!");}
            else{

                if (password.equals(rePassword)){
                    registerUser(name, email, phone, password);
                }
                else{
                    binding.regiRePassword.setError("Password did not matched!");
                }
            }
        });
    }

    private void registerUser(String name, String email, String phone, String password) {

        binding.regiAcRegisterBtn.setVisibility(View.INVISIBLE);
        binding.regiAcProgressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

//                ----------- Storing data to the firebase -------------
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Map<String, Object> userMap = new HashMap<>();

                userMap.put("user_id", firebaseUser.getUid());
                userMap.put("user_profile_img", "");
//                userMap.put("user_cover_img", "");
                userMap.put("user_name", name);
                userMap.put("user_email", email);
                userMap.put("user_phone_number", phone);
                userMap.put("user_password", password);

                userDatabaseReference.child(firebaseUser.getUid()).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            Toast.makeText(RegisterActivity.this, "Data added successfully!!", Toast.LENGTH_SHORT).show();
                            binding.regiAcRegisterBtn.setVisibility(View.VISIBLE);
                            binding.regiAcProgressBar.setVisibility(View.GONE);

                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                        else{
                            showAlert("Problem!", task.getException().getLocalizedMessage());
                            binding.regiAcRegisterBtn.setVisibility(View.VISIBLE);
                            binding.regiAcProgressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showAlert("Failed", e.getLocalizedMessage());
                binding.regiAcRegisterBtn.setVisibility(View.VISIBLE);
                binding.regiAcProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlert(String title, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setIcon(R.drawable.warning_icon);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}