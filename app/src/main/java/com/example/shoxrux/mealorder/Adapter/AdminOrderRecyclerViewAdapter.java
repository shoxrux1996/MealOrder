package com.example.shoxrux.mealorder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoxrux.mealorder.Activity.AdminActivity;
import com.example.shoxrux.mealorder.Activity.AdminOrderInfoActivity;
import com.example.shoxrux.mealorder.ItemClickListener;
import com.example.shoxrux.mealorder.Model.Order;
import com.example.shoxrux.mealorder.R;
import com.jackandphantom.circularimageview.RoundedImage;
import com.koushikdutta.ion.Ion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shoxrux on 12/20/18.
 */

public class AdminOrderRecyclerViewAdapter extends RecyclerView.Adapter<AdminOrderRecyclerViewAdapter.AdminOrderViewItemHolder>{

    private List<Order> orders;
    private Context context;

    public AdminOrderRecyclerViewAdapter(Context context, List<Order> orders) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public AdminOrderViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_tab3_item_order, parent,false);
        //Create Custom Item View holder
        return new AdminOrderViewItemHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminOrderViewItemHolder holder, int position) {
        //Call populate function to set UI of the holder with order
        holder.populate(orders.get(position));

        //If view holder clicks, we will create AdminMenuInfoActivity for menu change
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Intent orderInfo = new Intent(context, AdminOrderInfoActivity.class);
                orderInfo.putExtra("orderInfo", orders.get(position));
                AdminActivity adminActivity = (AdminActivity) context;
                adminActivity.startActivityForResult(orderInfo, AdminActivity.ORDER_REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class AdminOrderViewItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.order_rounded_image)
        RoundedImage roundedImage;
        @BindView(R.id.order_title)
        TextView titleView;
        @BindView(R.id.order_price)
        TextView priceView;
        @BindView(R.id.order_amount)
        TextView amountView;
        @BindView(R.id.order_total_price)
        TextView totalPriceView;
        @BindView(R.id.order_date)
        TextView dateView;
        @BindView(R.id.order_status_image)
        ImageView statusView;

        private ItemClickListener itemClickListener;

        public AdminOrderViewItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //bind the listener
            itemView.setOnClickListener(this);
        }
        //Set Item Click listener interface
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        //to set UI of the holder
        public void populate(Order order){
            //Load image from the server (internet) using Ion
            Ion.with(context).load(order.getImage()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(roundedImage);
            titleView.setText(order.getTitle());
            priceView.setText(order.getPrice()+" sum");
            amountView.setText(order.getAmount()+"");
            totalPriceView.setText(order.getTotalPrice()+" sum");
            dateView.setText(order.getDate());
            //Set Triangle Image Status with Order status
            switch (order.getStatus()){
                case Order.STATE_WAITING:
                    statusView.setImageResource(R.drawable.triangle_yellow);
                    break;
                case Order.STATE_APPROVED:
                    statusView.setImageResource(R.drawable.triangle_green);
                    break;
                case Order.STATE_REJECTED:
                    statusView.setImageResource(R.drawable.triangle_red);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            itemClickListener.OnClick(view, getAdapterPosition());
        }
    }
}
