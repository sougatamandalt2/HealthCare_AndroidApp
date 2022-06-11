package com.example.healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.Holder> {

    private ArrayList<modelOrder> orderList;
    private Context context;

    public AdapterOrder(Context context, ArrayList<modelOrder> orderList) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_order,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        modelOrder modelOrder=orderList.get(position);

        String orderID=modelOrder.getOrderid();
        String orderStatus=modelOrder.getStatus();
        String orderCost=modelOrder.getPrice();
        String patientname=modelOrder.getPatientname();
        String relation=modelOrder.getRelation();
        String orderType=modelOrder.getIssue();
        String date=modelOrder.getDate();

        holder.tv_ordersID.setText("OrderID         : "+orderID);
        holder.tv_orderDate.setText(date);
        holder.tv_orderStatus.setText(orderStatus);
        holder.tv_orderAmount.setText("Rs   :   "+orderCost);
        holder.tv_orderType.setText(orderType);
        holder.tv_relation.setText("Booking For :  "+relation);
        holder.tv_pname.setText("Name             : "+patientname);

        if(orderStatus.equals("Order Received")){
            holder.tv_orderStatus.setTextColor(context.getResources().getColor(R.color.green));
        }
//        else if(orderStatus.equals("Completed")){
//            holder.tv_orderStatus.setTextColor(context.getResources().getColor(R.color.teal_200));
//        }
//        else if(orderStatus.equals("Cancelled")){
//            holder.tv_orderStatus.setTextColor(context.getResources().getColor(R.color.red));
//        }

    }
//
//    private void loadShopInfo(modelOrder modelOrderUser, Holder holder) {
//
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
//        reference.child(modelOrderUser.getOrder_id()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String type=""+snapshot.child("orderTitle").getValue();
//                holder.tv_orderType.setText(type);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        private TextView tv_relation,tv_pname;
        private TextView tv_ordersID,tv_orderDate,tv_orderType,tv_orderAmount,tv_orderStatus;

        public Holder(@NonNull View itemView) {
            super(itemView);

            tv_ordersID=itemView.findViewById(R.id.tv_OrderID);
            tv_orderDate=itemView.findViewById(R.id.tv_orderDate);
            tv_orderType=itemView.findViewById(R.id.tv_orderType);
            tv_orderAmount=itemView.findViewById(R.id.tv_orderAmount);
            tv_orderStatus=itemView.findViewById(R.id.tv_orderStatus);
            tv_pname=itemView.findViewById(R.id.tv_pname);
            tv_relation=itemView.findViewById(R.id.tv_relation);

        }
    }

}

