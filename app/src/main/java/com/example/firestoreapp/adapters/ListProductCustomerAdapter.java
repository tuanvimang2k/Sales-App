package com.example.firestoreapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firestoreapp.Activities.DetailProductActivity;
import com.example.firestoreapp.R;
import com.example.firestoreapp.models.Cart;
import com.example.firestoreapp.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.List;

public class ListProductCustomerAdapter extends RecyclerView.Adapter<ListProductCustomerAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ListProductCustomerAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textProductName, textProductPrice;
        Button btnAddProduct;
        LinearLayout ItemParentLinear;

        public ProductViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            textProductPrice = itemView.findViewById(R.id.textProductPrice);
            btnAddProduct = itemView.findViewById(R.id.btnAddProduct);
            ItemParentLinear = itemView.findViewById(R.id.ItemParentLinear);

            btnAddProduct.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ProductViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Gán dữ liệu cho ViewHolder
        holder.textProductName.setText(product.getProduct_name());
        holder.textProductPrice.setText("Price: $" + product.getUnit_price());
        // TODO: Set ảnh sản phẩm vào ImageView (sử dụng thư viện Picasso/Glide)
        Glide.with(context)
                .load(product.getProduct_image())
                .into(holder.imageProduct);
        holder.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận thêm");
                builder.setMessage("Bạn có muốn thêm vào giỏ hàng?");
                builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MyID",context.MODE_PRIVATE);
                        String _id = sharedPreferences.getString("id","default_id");
                        Float price = product.getUnit_price();
                        String productRef = product.getId();
                        String name = product.getProduct_name();
//                        String productRef, String name, int quantity, float unit_price, String idCustomer
                        Cart cart = new Cart(productRef,name,1,price,_id);
                        AddToCart(cart,_id);
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Hủy bỏ xóa, không làm gì cả
                        dialog.dismiss();
                    }
                });
                builder.show();


            }
        });
        holder.ItemParentLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", (Serializable) product);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    private void AddToCart(Cart cart, String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customer").document(id).collection("Cart")
                .add(cart)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
