package com.example.firestoreapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firestoreapp.R;
import com.example.firestoreapp.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> orderItemList;
    private Context context;

    public OrderAdapter(List<Order> orderItemList, Context context) {
        this.orderItemList = orderItemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orderItemList.get(position);

        holder.txtName.setText(order.getName());
        holder.txtQuantity.setText("Số lượng: " + order.getQuantity());
        holder.txtPrice.setText("Giá: " + order.getPrice());
        Glide.with(context)
                .load(order.getImageResource())
                .into(holder.imgProduct);
        // Bạn có thể thêm xử lý sự kiện cho Button ở đây
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý khi Button được click
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtQuantity, txtPrice;
        public ImageView imgProduct;
        public Button btnComment;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnComment = itemView.findViewById(R.id.btnComment);
        }
    }
}
