package com.example.firestoreapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firestoreapp.R;
import com.example.firestoreapp.models.Product;

public class DetailProductActivity extends AppCompatActivity {
    Product product;
    private ImageView imgProduct;
    private TextView txtDescription;
    private TextView txtPrice;
    private TextView txtProductName;
    private Button btnBuy;
    private RecyclerView recyclerViewRelatedProducts;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle bundle = getIntent().getExtras();
        product = new Product();
        if (bundle != null) {
            // Kiểm tra xem có đối tượng trong Bundle không
            if (bundle.containsKey("product")) {
                // Lấy đối tượng từ Bundle
                Product _product = (Product) bundle.getSerializable("product");
                product = _product;
                Log.d(">>>>>>>>>>>>>>>>>>>>>>>> Product", "Product: "+_product);
                // Bây giờ bạn có thể sử dụng đối tượng Product như bình thường
            }
        }

        imgProduct = findViewById(R.id.imgProduct);
        txtProductName = findViewById(R.id.txtProductName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        btnBuy = findViewById(R.id.btnBuy);
        recyclerViewRelatedProducts = findViewById(R.id.recyclerViewRelatedProducts);

        Glide.with(DetailProductActivity.this)
                .load(product.getProduct_image())
                .into(imgProduct);
        txtProductName.setText(product.getProduct_name());
        txtDescription.setText(product.getDescription());
        txtPrice.setText(String.valueOf(product.getUnit_price()));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}