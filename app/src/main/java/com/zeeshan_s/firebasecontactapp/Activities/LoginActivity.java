package com.zeeshan_s.firebasecontactapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeeshan_s.firebasecontactapp.R;
import com.zeeshan_s.firebasecontactapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        firebaseAuth = FirebaseAuth.getInstance();

        //            ---------------- Login button -------------
        binding.logAcLoginBtn.setOnClickListener(view -> {

            binding.logProgressBar.setVisibility(View.VISIBLE);
            binding.logAcLoginBtn.setVisibility(View.INVISIBLE);

            String email = binding.logEmailEt.getText().toString().trim();
            String password = binding.logPassEt.getText().toString();


            if (email.equals("")){binding.logEmailEt.setError("Email address is needed");}
            else if (password.equals("")){binding.logPassEt.setError("Please enter the password!");}
            else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            binding.logProgressBar.setVisibility(View.GONE);
                            binding.logAcLoginBtn.setVisibility(View.VISIBLE);

                            Toast.makeText(LoginActivity.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            showAlert("Error!!", task.getException().getLocalizedMessage());

                            binding.logProgressBar.setVisibility(View.GONE);
                            binding.logAcLoginBtn.setVisibility(View.VISIBLE);

                        }
                    }
                });
            }
        });

        binding.registerOption.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void showAlert(String title, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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