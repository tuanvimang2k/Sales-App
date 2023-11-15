package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firestoreapp.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtEmail,edtPass,edtPass2;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        edtPass2 = findViewById(R.id.edtPass2);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();
                String pass2 = edtPass2.getText().toString();
                User user = new User();
                if(pass.equals(pass2) && !pass2.equals("") && !pass.equals("") ){
                    user.setEmail(email);
                    user.setPass(pass);
                    user.setGender(true);
                    user.setFirst_name(null);
                    user.setLast_name(null);
                    user.setPhone_number(0);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("customer")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    edtEmail.setText("");
                                    edtPass.setText("");
                                    edtPass2.setText("");
                                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Register Fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    Toast.makeText(RegisterActivity.this, "Please, check your password!", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

}