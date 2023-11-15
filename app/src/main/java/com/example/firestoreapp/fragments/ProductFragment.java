package com.example.firestoreapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.firestoreapp.R;
import com.example.firestoreapp.adapters.ListProductCustomerAdapter;
import com.example.firestoreapp.models.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListProductCustomerAdapter recyclerViewAdapter;
    private List<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProduct);
        productList = new ArrayList<>();

        // Khởi tạo RecyclerViewAdapter và thiết lập cho RecyclerView
        recyclerViewAdapter = new ListProductCustomerAdapter(requireContext(), productList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));

        // Lấy danh sách sản phẩm từ Firestore
        getListFromFirestore();

        return view;
    }

    private void getListFromFirestore() {
        productList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("Product");

        productsCollection.get().addOnCompleteListener(task -> {
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

                    Product product = new Product(id, description, product_category_name, product_image,
                            product_name, provider_name, stock_quantity, unit_price, warrantly_duration_months);

                    productList.add(product);
                }

                // Cập nhật RecyclerView khi có dữ liệu
                recyclerViewAdapter.notifyDataSetChanged();
            } else {
                // Xử lý khi không lấy được dữ liệu từ Firestore
            }
        });
    }
}
