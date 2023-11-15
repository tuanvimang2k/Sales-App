package com.example.firestoreapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firestoreapp.adapters.RecyclerViewAdapter;
import com.example.firestoreapp.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;
import java.util.zip.Inflater;

public class ListProduct extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    ArrayList<Product> list;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    String linkImg,linkImgFirebase;
    EditText description, product_image,product_category_name
            ,product_name,provider_name,stock_quantity,unit_price ,warrantly_duration_months;
    Uri uriAnh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddProduct();
            }
        });

        GetList();
        Log.d("<<<<<<<<<<<<<< onCreate get list child", "GetList: "+list);
        linkImg = "Không có ảnh";
    }

    // lấy danh sách sản phẩm
    ArrayList<Product> GetListProduct() {
        ArrayList<Product> listChild = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("Product");
        productsCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots) {
                            if (documentSnapshots.exists()) {
                                String description = documentSnapshots.getString("description");
                                String product_category_name = documentSnapshots.getString("product_category_name");
                                String product_image = documentSnapshots.getString("description");
                                String product_name = documentSnapshots.getString("description");
                                String provider_name = documentSnapshots.getString("description");
                                int stock_quantity = Integer.parseInt(String.valueOf(documentSnapshots.getLong("stock_quantity")));
                                float unit_price = Float.parseFloat(String.valueOf(documentSnapshots.getLong("unit_price")));
                                String warrantly_duration_months = documentSnapshots.getString("warrantly_duration_months");
                                Product product = new Product(description, product_category_name, product_image
                                        , product_name, provider_name, stock_quantity, unit_price, warrantly_duration_months);
                                listChild.add(product);
//                                kiểm tra lấy được kiểu dữ liệu number chưa.
//                                Toast.makeText(ListProduct.this, ""+stock_quantity, Toast.LENGTH_SHORT).show();
                            }
                        }
//                        kiểm tra đã lấy được list product chưa
//                        for (Product p:listChild) {
//                            Toast.makeText(ListProduct.this, ""+p.getDescription(), Toast.LENGTH_SHORT).show();
////                            Log.d(">>>>>>>>>>>>>> danh sách sản phẩm ", "list "+list);
//                        }
//                        Log.d(">>>>>>>>>>>>>> danh sách sản phẩm ", "list "+list);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return listChild;
    }

    ArrayList<Product> GetList() {
        list.clear();
        ArrayList<Product> listChild = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("Product");
        productsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String id = documentSnapshot.getId();
                        String description = documentSnapshot.getString("description");
                        String product_category_name = documentSnapshot.getString("product_category_name");
                        String product_image = documentSnapshot.getString("product_image");
                        String product_name = documentSnapshot.getString("product_name");
                        String provider_name = documentSnapshot.getString("provider_name");
                        int stock_quantity = Integer.parseInt(String.valueOf(documentSnapshot.getLong("stock_quantity")));
                        float unit_price = Float.parseFloat(String.valueOf(documentSnapshot.getLong("unit_price")));
                        String warrantly_duration_months = documentSnapshot.getString("warrantly_duration_months");
                        Product product = new Product(id,description, product_category_name, product_image
                                , product_name, provider_name, stock_quantity, unit_price, warrantly_duration_months);
                        list.add(product);


                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListProduct.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerViewAdapter = new RecyclerViewAdapter(ListProduct.this, list,someActivityResultLauncher);
                    recyclerView.setAdapter(recyclerViewAdapter);
                } else {
                    Log.d("Lỗi lấy danh sách", "onComplete: ", task.getException());
                }
            }
        });
//        Log.d("<<<<<<<<<<<<<< get list child", "GetListChild: "+listChild);
        return listChild;

    }

    private void DialogAddProduct() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_product);
        dialog.show();
        //ánh xạ
        description = (EditText) dialog.findViewById(R.id.description);
        product_category_name = (EditText) dialog.findViewById(R.id.product_category_name);
        product_image = (EditText) dialog.findViewById(R.id.product_image);
        product_name = (EditText) dialog.findViewById(R.id.product_name);
        provider_name = (EditText) dialog.findViewById(R.id.provider_name);
        stock_quantity = (EditText) dialog.findViewById(R.id.stock_quantity);
        unit_price = (EditText) dialog.findViewById(R.id.unit_price);
        warrantly_duration_months = (EditText) dialog.findViewById(R.id.warrantly_duration_months);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        Button btnLayImg = (Button) dialog.findViewById(R.id.btnLayImg);
        btnLayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    // Xác định loại tệp bạn muốn chọn (ảnh)
                    intent.setType("image/*");

                    // Mở album ảnh
                    someActivityResultLauncher.launch(intent);
                    product_image.setText(linkImg);

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpPhoto(uriAnh);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void AddProduct(String description, String product_category_name
            , String product_image, String product_name, String provider_name
            , int stock_quantity, float unit_pricem, String warrantly_duration_months) {
        Product product = new Product(description, product_category_name, product_image
                , product_name, provider_name, stock_quantity, unit_pricem, warrantly_duration_months);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Product")
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(">>>>>>>>>>>>>> Thêm sản phẩm thành công", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(">>>>>>>>>>>>> Thêm sản phẩm thất bại", "Error adding document", e);
                    }
                });
    }
    // nhận kết quả trả về

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Uri uri = result.getData().getData();
                        uriAnh = uri;
                        linkImg = uri.toString();
                        product_image.setText(linkImg);
                    }
                }
            });
    private void UpPhoto(Uri uri ) {

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
                        String _link = uriResult.toString();
                        linkImgFirebase = _link;
                        // lúc này muốn up ảnh lên cũng là lúc mình muốn up dữ liệu.
                        // do sự chênh lệnh thời gian giữa firebase và code java nên phải ràng buộc thêm
                        // sản phẩm trong này để đảm bảo tính đúng đắn của dữ liệu
                        String _description = description.getText().toString();
                        String _product_category_name = product_category_name.getText().toString();
                        String _product_name = product_name.getText().toString();
                        String _provider_name = provider_name.getText().toString();
                        String stock_quantityString = stock_quantity.getText().toString();
                        int _stock_quantity = Integer.parseInt(stock_quantityString);
                        String unit_priceString = unit_price.getText().toString();
                        float _unit_price = Float.parseFloat(unit_priceString);
                        String _warrantly_duration_months = warrantly_duration_months.getText().toString();
                        AddProduct(_description, _product_category_name, linkImgFirebase, _product_name, _provider_name
                                , _stock_quantity, _unit_price, _warrantly_duration_months);
                        GetList();
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