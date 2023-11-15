package com.example.firestoreapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestoreapp.R;
import com.example.firestoreapp.models.Cart;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Cart> list;

    public CartAdapter(Context context, ArrayList<Cart> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart currentItem = list.get(position);

        // Set data to views
        holder.productLabelTextView.setText("Product: " + currentItem.getProductRef());
        holder.quantityTextView.setText("Quantity: " + currentItem.getQuantity());
        holder.totalPriceTextView.setText("Total: $" + currentItem.getUnit_price());

        // Set click listener for the delete button (you can handle the click event here)
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle delete button click
                // You might want to notify the activity or fragment to handle the delete action
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView productLabelTextView, quantityTextView, totalPriceTextView;
        AppCompatButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productLabelTextView = itemView.findViewById(R.id.productLabel);
            quantityTextView = itemView.findViewById(R.id.quantityLabel);
            totalPriceTextView = itemView.findViewById(R.id.totalPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

}
