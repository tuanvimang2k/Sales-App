package com.example.firestoreapp.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firestoreapp.ListProduct;
import com.example.firestoreapp.R;
import com.example.firestoreapp.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<Product> list;
    String linkImg,linkImgFirebase;
    String id ;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    EditText description, product_image, product_category_name
            , product_name, provider_name, stock_quantity
            , unit_price, warrantly_duration_months;

    public RecyclerViewAdapter(Context context, ArrayList<Product> list,ActivityResultLauncher<Intent> someActivityResultLauncher) {
        this.context = context;
        this.list = list;
        this.someActivityResultLauncher = someActivityResultLauncher;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String _id = list.get(holder.getAdapterPosition()).getId();
        holder.txtTenSanPham.setText(list.get(holder.getAdapterPosition()).getProduct_name());
        holder.txtGiaSanPham.setText(String.valueOf(list.get(holder.getAdapterPosition()).getUnit_price()));
        holder.linearParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+_id, Toast.LENGTH_SHORT).show();
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product p = list.get(holder.getAdapterPosition());
                id = _id;
                DialogAddProduct();
                FillDataInDialog(list.get(holder.getAdapterPosition()));

            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa tài liệu này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Thực hiện xóa tài liệu
                        DeleteProduct(_id);

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

        Glide.with(context)
                .load(list.get(holder.getAdapterPosition()).getProduct_image())
                .into(holder.imgProduct);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearParent;
        ImageView imgProduct, imgEdit, imgDelete;
        TextView txtTenSanPham;
        TextView txtGiaSanPham;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);
            txtGiaSanPham = itemView.findViewById(R.id.txtGiaSanPham);
            linearParent = itemView.findViewById(R.id.linearParent);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    private void DialogAddProduct() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_product);
        dialog.show();
        //ánh xạ
        description = (EditText) dialog.findViewById(R.id.description);
        product_category_name = (EditText) dialog.findViewById(R.id.product_category_name);
        product_image = (EditText) dialog.findViewById(R.id.product_image);
        product_name = (EditText) dialog.findViewById(R.id.product_name);
        provider_name = (EditText) dialog.findViewById(R.id.provider_name);
        stock_quantity = (EditText) dialog.findViewById(R.id.stock_quantity);
        unit_price = (EditText) dialog.findViewById(R.id.unit_price);
        warrantly_duration_months = (EditText) dialog.findViewById(R.id.warrantly_duration_months);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        Button btnLayImg = (Button) dialog.findViewById(R.id.btnLayImg);
        btnLayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // Xác định loại tệp bạn muốn chọn (ảnh)
                intent.setType("image/*");

                // Mở album ảnh
                someActivityResultLauncher.launch(intent);
                product_image.setText(linkImg);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _description = description.getText().toString();
                String _product_category_name = product_category_name.getText().toString();
                String _product_name = product_name.getText().toString();
                String _provider_name = provider_name.getText().toString();
                String _stock_quantityString = stock_quantity.getText().toString();
                String _product_image = product_image.getText().toString();
                int _stock_quantity = Integer.parseInt(_stock_quantityString);
                String unit_priceString = unit_price.getText().toString();
                float _unit_price = Float.parseFloat(unit_priceString);
                String _warrantly_duration_months = warrantly_duration_months.getText().toString();
                Product _product = new Product(_description,_product_category_name,_product_image,_product_name
                        ,_provider_name,_stock_quantity,_unit_price,_warrantly_duration_months);
                UpdateProduct(_product,id);
                GetList();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    //    description, product_image,product_category_name
//    ,product_name,provider_name,stock_quantity,unit_price ,warrantly_duration_months;
    private void FillDataInDialog(Product p) {
        description.setText(p.getDescription());
        product_name.setText(p.getProduct_name());
        product_category_name.setText(p.getProduct_category_name());
        product_image.setText(p.getProduct_image());
        provider_name.setText(p.getProvider_name());
        stock_quantity.setText(String.valueOf(p.getStock_quantity()));
        unit_price.setText(String.valueOf(p.getUnit_price()));
        warrantly_duration_months.setText(String.valueOf(p.getWarrantly_duration_months()));
    }

    private  void DeleteProduct(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Product").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        GetList();
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void UpdateProduct(Product product,String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Product").document(id).set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "update thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void UpPhoto(Uri uri ) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");

        // Tạo tên tệp ảnh duy nhất (ví dụ: timestamp)
        String timestamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "image_" + UUID.randomUUID().toString() + ".jpg";

        // Tạo tham chiếu mới đến tệp ảnh với tên mới
        StorageReference newImageRef = imagesRef.child(imageFileName);

        // Tải ảnh lên Firebase Storage trong child "images" với tên mới
        newImageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Xử lý khi tải ảnh lên thành công


                    // Lấy đường link của ảnh sau khi tải lên
                    newImageRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                        String _link = uriResult.toString();
                        linkImgFirebase = _link;
                        // lúc này muốn up ảnh lên cũng là lúc mình muốn up dữ liệu.
                        // do sự chênh lệnh thời gian giữa firebase và code java nên phải ràng buộc thêm
                        // sản phẩm trong này để đảm bảo tính đúng đắn của dữ liệu


                    }).addOnFailureListener(e -> {
                        // Xử lý khi không thể lấy được đường link
                        Toast.makeText(context, "Lỗi khi lấy đường link", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(exception -> {
                    // Xử lý khi tải ảnh lên thất bại
                    Toast.makeText(context, "Thêm ảnh thất bại", Toast.LENGTH_SHORT).show();
                });

    }

    private void GetList(){
        list.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("Product");
        productsCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                        Product product = new Product(id,description, product_category_name, product_image
                                , product_name, provider_name, stock_quantity, unit_price, warrantly_duration_months);
                        list.add(product);
                    }
                    notifyDataSetChanged();
                } else {
                    Log.d("Lỗi lấy danh sách", "onComplete: ", task.getException());
                }
            }
        });
    }


}
