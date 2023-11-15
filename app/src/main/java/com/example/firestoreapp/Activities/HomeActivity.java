package com.example.firestoreapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firestoreapp.R;
import com.example.firestoreapp.fragments.BlankFragment;
import com.example.firestoreapp.fragments.ListProductFragment;
import com.example.firestoreapp.fragments.ProductFragment;
import com.example.firestoreapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private static final int FRAGMENT_NEWS = 0;
    private int choseFragment = FRAGMENT_NEWS;
    private DrawerLayout drawer_layout;
    private BottomNavigationView bottom_navigation;
    private NavigationView navigation_view;
    private User user;
    TextView txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = new User();
        bottom_navigation = findViewById(R.id.bottom_navigation);

        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home){
                    ReplaceFragment(new ListProductFragment());
                }else if (item.getItemId() == R.id.nav_logout){
                    ReplaceFragment(new ProductFragment());
                }

                return true;
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LifeTech");
        drawer_layout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer_layout
                ,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);

        // ánh xạ layout header
//        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        View headerView = navigation_view.getHeaderView(0);
        ImageView imgProfile = headerView.findViewById(R.id.imgProfile);
        txtEmail = headerView.findViewById(R.id.txtEmail);
        TextView txtName = headerView.findViewById(R.id.txtName);
        TextView txtRole = headerView.findViewById(R.id.txtRole);


        SharedPreferences sharedPreferences = getSharedPreferences("MyID",MODE_PRIVATE);
        String _id = sharedPreferences.getString("id","default_id");
        Toast.makeText(this, ""+_id, Toast.LENGTH_SHORT).show();
        GetUser(_id);



//// chuyển thành fragment new khi tạo màn hình.
//        ReplaceFragment(new NewsFragment());
//        navigation_view.setCheckedItem(R.id.nav_new);
//        bottom_navigation.getMenu().findItem(R.id.nav_new).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home){
            ReplaceFragment(new ListProductFragment());
        }else if (id == R.id.nav_logout){
            ReplaceFragment(new BlankFragment());
        }


        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    private void ReplaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,fragment);
        fragmentTransaction.commit();
    }

    private void GetUser(String id){
        User _user = new User();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("customer").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", "DocumentSnapshot data: " + documentSnapshot.getData());
                        String email = documentSnapshot.getString("email");
                        String first_name = documentSnapshot.getString("first_name");
                        _user.setEmail(email);
                        _user.setFirst_name(first_name);
                        user = _user;
                        txtEmail.setText(user.getEmail());
                        Log.d(">>>>>>>>>>>>>>>>USER", "USER: " + _user);

                    } else {
                        Log.d("Lỗi không tìm thấy user", "No such document");
                    }
                } else {
                    Log.d("lỗi lấy user", "get failed with ", task.getException());
                }
            }
        });

    }

}