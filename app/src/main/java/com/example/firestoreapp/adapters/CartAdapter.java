package com.example.firestoreapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestoreapp.R;
import com.example.firestoreapp.models.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Cart> list;
    private ArrayList<Cart> listTemp;
    private float tongTien;
    private TextView txtTongGia;

    String _id;
//    public CartAdapter(Context context, ArrayList<Cart> list) {
//        this.context = context;
//        this.list = list;
//        listTemp = new ArrayList<>();
//        tongTien = 0;
//        SharedPreferences sharedPreferences = context.getSharedPreferences("MyID", context.MODE_PRIVATE);
//         _id = sharedPreferences.getString("id", "default_id");
//    }
    public CartAdapter(Context context, ArrayList<Cart> list, ArrayList<Cart> listTemp, float tongTien,TextView txtTongGia) {
        this.context = context;
        this.list = list;
        this.listTemp = listTemp;
        this.tongTien = tongTien;
        this.txtTongGia = txtTongGia;
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyID", context.MODE_PRIVATE);
        _id = sharedPreferences.getString("id", "default_id");
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
        holder.productLabelTextView.setText( currentItem.getName());
        holder.quantityTextView.setText("Quantity: " + currentItem.getQuantity());
        holder.totalPriceTextView.setText("Total: $" + currentItem.getUnit_price());

        // Set click listener for the delete button (you can handle the click event here)
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa tài liệu này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Thực hiện xóa tài liệu
                        DeleteCartItem(_id,currentItem.getIDCart());
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

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Xử lý khi trạng thái của CheckBox thay đổi
                if (isChecked) {
//                    Toast.makeText(context, "CheckBox Checked", Toast.LENGTH_SHORT).show();
                    listTemp.add(currentItem);
                    tongTien += currentItem.getUnit_price()*currentItem.getQuantity();
                    txtTongGia.setText("Tổng: "+tongTien);

                } else {
                    listTemp.remove(currentItem);
                    tongTien -= currentItem.getUnit_price()*currentItem.getQuantity();
                    txtTongGia.setText("Tổng: "+tongTien);

                }
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
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productLabelTextView = itemView.findViewById(R.id.productLabel);
            quantityTextView = itemView.findViewById(R.id.quantityLabel);
            totalPriceTextView = itemView.findViewById(R.id.totalPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    private void DeleteCartItem(String idCustomer,String idCart){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customer")
                .document(idCustomer)
                .collection("Cart")
                .document(idCart)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getListFromFirestore();
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                })
        ;

    }
    private void getListFromFirestore() {
        list.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartCollection = db.collection("customer").document(_id).collection("Cart");
        cartCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String IDCart = documentSnapshot.getId();
                        String productRef = documentSnapshot.getString("productRef");
                        String name = documentSnapshot.getString("name");
                        Long _quantity = documentSnapshot.getLong("quantity");
                        Double _unit_price =  documentSnapshot.getDouble("quantity");
                        int quantity = _quantity.intValue();
                        float unit_price = _unit_price.floatValue();
                        Cart cartItem = new Cart(IDCart,productRef,name, quantity, unit_price);
                        list.add(cartItem);
//                        Log.d(">>>>>>>>>>>>>>>CartList", "CartList: "+cartItem);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }



}
