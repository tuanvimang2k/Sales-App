package com.example.firestoreapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestoreapp.R;
import com.example.firestoreapp.adapters.OrderAdapter;
import com.example.firestoreapp.models.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private List<Order> orderList;
    private OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList, getContext());
        recyclerView.setAdapter(orderAdapter);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyID", getContext().MODE_PRIVATE);
        String _id = sharedPreferences.getString("id", "default_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Order");

        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String idCustomer = documentSnapshot.getString("idCustomer");
                    if (idCustomer != null && idCustomer.equalsIgnoreCase(_id)) {
                        String name = documentSnapshot.getString("name");
                        Long _quantity = documentSnapshot.getLong("quantity");
                        int quantity = (_quantity != null) ? _quantity.intValue() : 0;
                        Double unit_price = documentSnapshot.getDouble("unit_price");
                        String productRef = documentSnapshot.getString("productRef");

                        getImageResource(productRef).addOnCompleteListener(imageTask -> {
                            if (imageTask.isSuccessful()) {
                                String imageResource = imageTask.getResult();
                                Order order = new Order(name, quantity, unit_price, imageResource);
                                orderList.add(order);
                                orderAdapter.notifyDataSetChanged();
                            } else {
                                Exception e = imageTask.getException();
                                Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            } else {
                Exception e = task.getException();
                Toast.makeText(getContext(), "Error loading orders", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public Task<String> getImageResource(String productRef) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("Product").document(productRef).get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().getString("product_image");
                    } else {
                        throw task.getException();
                    }
                });
    }
}
