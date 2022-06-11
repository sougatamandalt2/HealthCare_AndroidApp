package com.example.healthcare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersListActivity extends AppCompatActivity {

    private ImageView iv_back;
    private ArrayList<modelOrder> orderList;
    private RecyclerView rv_orders;
    private AdapterOrder adapterOrder;
    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        isOnline();

        iv_back=findViewById(R.id.iv_back);
        rv_orders=findViewById(R.id.rv_orders);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        loadOrders();




        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrdersListActivity.this,HomepageActivity.class));
            }
        });
    }

    public void onBackPressed()
    {
        this.startActivity(new Intent(OrdersListActivity.this,HomepageActivity.class));
        return;
    }


    private void loadOrders() {

        orderList=new ArrayList<>();
        progressDialog.setMessage("Loading Your Orders");
        progressDialog.show();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    String uid=""+ds.getRef().getKey();

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Orders");
                    reference1.orderByChild("orderby").equalTo(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for(DataSnapshot ds:snapshot.getChildren()){
                                            modelOrder modelOrder=ds.getValue(modelOrder.class);

                                            orderList.add(modelOrder);
                                        }
                                        adapterOrder= new AdapterOrder(OrdersListActivity.this,orderList);
                                        rv_orders.setAdapter(adapterOrder);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(OrdersListActivity.this, "No Internet connection! Turn On Your Internet to Continue", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}