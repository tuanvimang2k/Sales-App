package com.example.firestoreapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firestoreapp.R;
import com.example.firestoreapp.models.Product;

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

        public ProductViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            textProductPrice = itemView.findViewById(R.id.textProductPrice);
            btnAddProduct = itemView.findViewById(R.id.btnAddProduct);

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
        // Xử lý sự kiện click
        // Nếu bạn không muốn sử dụng ViewHolder để xử lý sự kiện click, bạn có thể di chuyển phần xử lý này vào onBindViewHolder.
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
