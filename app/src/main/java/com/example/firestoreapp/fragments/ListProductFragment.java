package com.example.firestoreapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestoreapp.R;
import com.example.firestoreapp.adapters.RecyclerViewAdapter;
import com.example.firestoreapp.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

public class ListProductFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private ArrayList<Product> list;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private String linkImg, linkImgFirebase;
    private EditText description, product_image, product_category_name,
            product_name, provider_name, stock_quantity, unit_price, warrantly_duration_months;
    private Uri uriAnh;

    // ActivityResultLauncher để chọn ảnh
    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        // There are no request codes
                        Uri uri = result.getData().getData();
                        uriAnh = uri;
                        linkImg = uri.toString();
                        product_image.setText(linkImg);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddProduct();
            }
        });

        GetList();
        linkImg = "Không có ảnh";

        return view;
    }

    // Hàm lấy danh sách sản phẩm
    private void GetList() {
        list.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("Product");
        productsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
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
                        Product product = new Product(id, description, product_category_name, product_image
                                , product_name, provider_name, stock_quantity, unit_price, warrantly_duration_months);
                        list.add(product);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerViewAdapter = new RecyclerViewAdapter(requireContext(), list, someActivityResultLauncher);
                    recyclerView.setAdapter(recyclerViewAdapter);
                } else {
                    Log.d("Lỗi lấy danh sách", "onComplete: ", task.getException());
                }
            }
        });
    }

    // Hàm hiển thị dialog thêm sản phẩm
    private void DialogAddProduct() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_product);
        dialog.show();

        // Ánh xạ các thành phần trong dialog
        description = dialog.findViewById(R.id.description);
        product_category_name = dialog.findViewById(R.id.product_category_name);
        product_image = dialog.findViewById(R.id.product_image);
        product_name = dialog.findViewById(R.id.product_name);
        provider_name = dialog.findViewById(R.id.provider_name);
        stock_quantity = dialog.findViewById(R.id.stock_quantity);
        unit_price = dialog.findViewById(R.id.unit_price);
        warrantly_duration_months = dialog.findViewById(R.id.warrantly_duration_months);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnLayImg = dialog.findViewById(R.id.btnLayImg);

        // Bắt sự kiện khi nhấn nút "Chọn ảnh"
        btnLayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                someActivityResultLauncher.launch(intent);
                product_image.setText(linkImg);
            }
        });

        // Bắt sự kiện khi nhấn nút "Thêm sản phẩm"
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpPhoto(uriAnh);
                dialog.dismiss();
            }
        });

        // Bắt sự kiện khi nhấn nút "Hủy"
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // Hàm thêm sản phẩm
    private void AddProduct(String description, String product_category_name
            , String product_image, String product_name, String provider_name
            , int stock_quantity, float unit_price, String warrantly_duration_months) {
        Product product = new Product(description, product_category_name, product_image
                , product_name, provider_name, stock_quantity, unit_price, warrantly_duration_months);

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

    // Hàm tải ảnh lên Firebase Storage
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
                    Toast.makeText(requireContext(), "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();

                    // Lấy đường link của ảnh sau khi tải lên
                    newImageRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                        String _link = uriResult.toString();
                        linkImgFirebase = _link;
                        // Lúc này muốn up ảnh lên cũng là lúc mình muốn up dữ liệu.
                        // Do sự chênh lệch thời gian giữa Firebase và code Java nên phải ràng buộc thêm
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
                        Toast.makeText(requireContext(), "Lỗi khi lấy đường link", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(exception -> {
                    // Xử lý khi tải ảnh lên thất bại
                    Toast.makeText(requireContext(), "Thêm ảnh thất bại", Toast.LENGTH_SHORT).show();
                });
    }
}
