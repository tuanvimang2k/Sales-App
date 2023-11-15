// CartFragment.java
package com.example.firestoreapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestoreapp.R;
import com.example.firestoreapp.adapters.CartAdapter;
import com.example.firestoreapp.models.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartList = new ArrayList<>(); // Initialize with your cart items
        recyclerView = view.findViewById(R.id.recyclerView);
        getListFromFirestore();
        return view;
    }

    private void getListFromFirestore() {
        cartList.clear();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyID", getContext().MODE_PRIVATE);
        String _id = sharedPreferences.getString("id", "default_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartCollection = db.collection("customer").document(_id).collection("Cart");

        cartCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String productRef = documentSnapshot.getString("productRef");
                        Long _quantity = documentSnapshot.getLong("quantity");
                        Double _unit_price =  documentSnapshot.getDouble("quantity");
                        int quantity = _quantity.intValue();
                        float unit_price = _unit_price.floatValue();
                        Cart cartItem = new Cart(productRef, quantity, unit_price);
                        cartList.add(cartItem);
//                        Log.d(">>>>>>>>>>>>>>>CartList", "CartList: "+cartItem);
                    }
                    cartAdapter = new CartAdapter(requireContext(), cartList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    recyclerView.setAdapter(cartAdapter);
                    cartAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
