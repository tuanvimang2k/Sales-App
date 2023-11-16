package com.example.firestoreapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestoreapp.R;
import com.example.firestoreapp.fragments.CartFragment;
import com.example.firestoreapp.models.Address;
import com.example.firestoreapp.models.Cart;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private Context context;
    private List<Address> addressList;
    private TextView txtAddress;
    private Address addressTemp;
    private Dialog dialog;

    public AddressAdapter(Context context, List<Address> addressList, TextView txtAddress, Address addressTemp, Dialog dialog) {
        this.context = context;
        this.addressList = addressList;
        this.txtAddress = txtAddress;
        this.addressTemp = addressTemp;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adress, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address addressItem = addressList.get(position);

        // Đặt dữ liệu cho ViewHolder từ đối tượng AddressItem
        holder.txtAddress.setText(addressItem.getAddress());
        holder.txtPhoneNumber.setText(addressItem.getPhone());
        holder.txtName.setText(addressItem.getName());

        // Xử lý sự kiện khi nút "Chọn" được nhấn
        holder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressTemp = addressItem;
                txtAddress.setText(addressItem.getAddress());
                dialog.dismiss();
                CartFragment.addressTemp = addressItem;
                Log.d(">>>>>>>>>>>>>>>>>>>>>>>>", " "+addressTemp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        public TextView txtAddress;
        public TextView txtPhoneNumber;
        public TextView txtName;
        public Button btnAction;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ các view từ layout item
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPhoneNumber = itemView.findViewById(R.id.txtPhoneNumber);
            txtName = itemView.findViewById(R.id.txtName);
            btnAction = itemView.findViewById(R.id.btnAction);
        }


    }
}

