package com.example.firestoreapp;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firestoreapp.adapters.TestRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.grpc.Context;

public class MainActivity extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;
    LinearProgressIndicator progressIndicator;
    Uri image;
    Button btnLayAnh, btnTaiAnh, btnDangAnh;
    ImageView img;
    EditText edtLink;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLayAnh = findViewById(R.id.btnLayAnh);
        btnTaiAnh = findViewById(R.id.btnTaiAnh);
        btnDangAnh = findViewById(R.id.btnDangAnh);
        img = findViewById(R.id.img);
        edtLink = findViewById(R.id.edtLink);

        btnLayAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent để mở album ảnh
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Xác định loại tệp bạn muốn chọn (ảnh)
                intent.setType("image/*");

                // Mở album ảnh
                someActivityResultLauncher.launch(intent);

            }
        });

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Uri uri = result.getData().getData();
                        UpPhoto(uri);
                    }
                }
            });

    private void UpPhoto(Uri uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");

        // Tạo tên tệp ảnh duy nhất (ví dụ: timestamp)
        String timestamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "image_" + UUID.randomUUID().toString() + ".jpg";

        // Tạo tham chiếu mới đến tệp ảnh với tên mới
        StorageReference newImageRef = imagesRef.child(imageFileName);

        // Tải ảnh lên Firebase Storage trong child "images" với tên mới
        newImageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Xử lý khi tải ảnh lên thành công
                    Toast.makeText(this, "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();

                    // Lấy đường link của ảnh sau khi tải lên
                    newImageRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                        String link = uriResult.toString();
                        edtLink.setText(link);
                    }).addOnFailureListener(e -> {
                        // Xử lý khi không thể lấy được đường link
                        Toast.makeText(this, "Lỗi khi lấy đường link", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(exception -> {
                    // Xử lý khi tải ảnh lên thất bại
                    Toast.makeText(this, "Thêm ảnh thất bại", Toast.LENGTH_SHORT).show();
                });
    }
}