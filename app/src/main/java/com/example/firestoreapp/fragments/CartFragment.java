// CartFragment.java
package com.example.firestoreapp.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestoreapp.R;
import com.example.firestoreapp.adapters.AddressAdapter;
import com.example.firestoreapp.adapters.CartAdapter;
import com.example.firestoreapp.models.Address;
import com.example.firestoreapp.models.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartList;
    TextView txtTongGia,txtAddress;
    Button BtnDatHang,btnChooseAdress;
    private float tongTien;
    public static Address addressTemp;
    private ArrayList<Cart> listTemp;
    String _id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartList = new ArrayList<>(); // Initialize with your cart items
        recyclerView = view.findViewById(R.id.recyclerView);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtTongGia = view.findViewById(R.id.txtTongGia);
        btnChooseAdress = view.findViewById(R.id.btnChooseAdress);
        BtnDatHang = view.findViewById(R.id.BtnDatHang);
        tongTien = 0;
        listTemp = new ArrayList<>();
        getListFromFirestore();

        addressTemp = new Address();
        btnChooseAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChooseAdress();
            }
        });
        BtnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order(listTemp);
                txtTongGia.setText("Tổng: ");
                txtAddress.setText("Địa chỉ :");
            }
        });

        return view;
    }

    private void getListFromFirestore() {
        cartList.clear();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyID", getContext().MODE_PRIVATE);
         _id = sharedPreferences.getString("id", "default_id");
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
                        Double _unit_price =  documentSnapshot.getDouble("unit_price");
                        int quantity = _quantity.intValue();
                        float unit_price = _unit_price.floatValue();
                        Cart cartItem = new Cart(IDCart,productRef,name, quantity, unit_price,_id);
                        cartList.add(cartItem);
                    }
                    cartAdapter = new CartAdapter(requireContext(), cartList,listTemp,tongTien,txtTongGia);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    recyclerView.setAdapter(cartAdapter);
                    cartAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void Order(ArrayList<Cart> listCart){
        int TongDon = listCart.size();
        int check =0;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for (Cart cart: listCart) {
            check++;
            db.collection("Order").add(cart).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    db.collection("Order").document(documentReference.getId()).collection("Address").add(addressTemp);
                }
            });
            db.collection("customer").document(_id).collection("Cart").document(cart.getIDCart()).delete();
            if(check == TongDon){
                Toast.makeText(getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
            }
        }
        listTemp.clear();
//        getListFromFirestore();
        cartList.clear();
        FirebaseFirestore _db = FirebaseFirestore.getInstance();
        CollectionReference cartCollection = _db.collection("customer").document(_id).collection("Cart");

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
                        cartList.add(cartItem);
//                        Log.d(">>>>>>>>>>>>>>>CartList", "CartList: "+cartItem);
                    }
                    cartAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void DialogChooseAdress(){
        List<Address> addressList = new ArrayList<>();
//        addressList.add(new Address("123 Main Street", "John Doe", "555-1234"));
//        addressList.add(new Address("456 Oak Avenue", "Jane Smith", "555-5678"));
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_list_adress);
        dialog.show();
        RecyclerView recyclerViewDialogAddress = dialog.findViewById(R.id.recyclerViewDialogAddress);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customer").document(_id).collection("Address")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String address = documentSnapshot.getString("address");
                                String name = documentSnapshot.getString("name");
                                String phone = documentSnapshot.getString("phone");
                                Address _addressItem = new Address(address,name,phone);
                                addressList.add(_addressItem);
                            }
                            AddressAdapter addressAdapter = new AddressAdapter(getContext(),addressList,txtAddress,addressTemp,dialog);
                            recyclerViewDialogAddress.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerViewDialogAddress.setAdapter(addressAdapter);
                        }
                    }
                });


    }
}
