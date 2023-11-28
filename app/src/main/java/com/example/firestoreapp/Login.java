package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firestoreapp.Activities.HomeActivity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btnLogin,button2;
    EditText edtEmail,edtPass;
    TextView txtRegister;
    private GoogleSignInClient client ;
    private Button btnLogout;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private boolean showOneTapUI = true;
    private ProgressBar progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        txtRegister = findViewById(R.id.txtRegister);
        button2 = findViewById(R.id.button2);
        btnLogout = findViewById(R.id.btnLogout);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();



        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this,options);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = client.getSignInIntent();
                startActivityForResult(i,1000);
                progressBar.setVisibility(View.VISIBLE);


//                oneTapClient.beginSignIn(signInRequest)
//                        .addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
//                            @Override
//                            public void onSuccess(BeginSignInResult result) {
//                                try {
//                                    startIntentSenderForResult(
//                                            result.getPendingIntent().getIntentSender(), 12345,
//                                            null, 0, 0, 0);
//                                } catch (IntentSender.SendIntentException e) {
//                                    Log.e(">>>>>>>>>>>>>>>>>>>>>", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                CheckData();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Login.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            }
        });
        btnLogout.setVisibility(View.INVISIBLE);

    }

    void CheckData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference customersCollection = db.collection("customer");
        customersCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                            if (documentSnapshots.exists()) {
                                String e = edtEmail.getText().toString();
                                String p = edtPass.getText().toString();
                                String email = documentSnapshots.getString("email");
                                String password = documentSnapshots.getString("pass");
                                if(email.equals(e) && password.equals(p)){
                                    startActivity(new Intent(Login.this, HomeActivity.class));
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyID", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("id", documentSnapshots.getId());
                                    editor.putBoolean("check", false);
                                    editor.apply();
                                    progressBar.setVisibility(View.GONE);
                                    finish();
                                    return;
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
//            Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account =   task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                String user = account.getEmail();
                Log.d(">>>>>>>>>>>>>>>>>>> username", "username: "+user);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    CollectionReference customersCollection = db.collection("customer");
                                    customersCollection.get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                                                                if(documentSnapshots.getId().equalsIgnoreCase(firebaseUser.getUid())){
                                                                    Toast.makeText(Login.this, "login success", Toast.LENGTH_SHORT).show();
                                                                    SharedPreferences sharedPreferences = getSharedPreferences("MyID", MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putString("id", firebaseUser.getUid());
                                                                    editor.putBoolean("check", false);
                                                                    editor.apply();
                                                                    startActivity(new Intent(Login.this, HomeActivity.class));
                                                                    progressBar.setVisibility(View.GONE);
                                                                    finish();
                                                                    return;
                                                                }
                                                            }
                                                            Map<String,Object> map = new HashMap<>();
                                                            map.put("email",user);
                                                            db.collection("customer").document(firebaseUser.getUid()).set(map);
                                                            SharedPreferences sharedPreferences = getSharedPreferences("MyID", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("id", firebaseUser.getUid());
                                                            editor.putBoolean("check", false);
                                                            editor.apply();
                                                            startActivity(new Intent(Login.this, HomeActivity.class));
                                                            Toast.makeText(Login.this, "login success", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                            finish();
                                                        }
                                                    });
//                                    Toast.makeText(Login.this, "Đăng nhập thành công"+firebaseUser.getUid(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 12345) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                String username = credential.getId();
                String password = credential.getPassword();

                if (idToken !=  null) {
                    // Got an ID token from Google. Use it to authenticate
                    // with your backend.
                    Log.d(">>>>>>>>>>>>>> username", "username."+username);
                } else if (password != null) {
                    // Got a saved username and password. Use them to authenticate
                    // with your backend.
                    Log.d(">>>>>>>>>>>>>>", "Got password.");
                }
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case CommonStatusCodes.CANCELED:
                        Log.d(">>>>>>>>>>>>>>>", "One-tap dialog was closed.");
                        // Don't re-prompt the user.
                        showOneTapUI = false;
                        break;
                    case CommonStatusCodes.NETWORK_ERROR:
                        Log.d(">>>>>>>>>>>>>>>>", "One-tap encountered a network error.");
                        // Try again or just ignore.
                        break;
                    default:
                        Log.d(">>>>>>>>>>>>>>>>>>>", "Couldn't get credential from result."
                                + e.getLocalizedMessage());
                        break;
                }
            }
        }
    }
}