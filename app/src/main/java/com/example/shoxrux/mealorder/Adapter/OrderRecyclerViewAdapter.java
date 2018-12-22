package com.example.shoxrux.mealorder.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoxrux.mealorder.Model.Order;
import com.example.shoxrux.mealorder.R;
import com.jackandphantom.circularimageview.RoundedImage;
import com.koushikdutta.ion.Ion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shoxrux on 12/17/18.
 */

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.OrderItemViewHolder> {
    private Context context;
    private List<Order> orders;

    public OrderRecyclerViewAdapter(Context context, List<Order> orders) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_recycler_view_item, parent,false);
        //Create Custom Item View holder
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {
        //Call populate function to set UI of the holder

        holder.populate(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder{
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


        public OrderItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        // function to set UI of the holder

        public void populate(Order order){
            //Load image from the server (internet) using Ion
            Ion.with(context).load(order.getImage()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(roundedImage);
            titleView.setText(order.getTitle());
            priceView.setText(order.getPrice()+" sum");
            amountView.setText(order.getAmount()+"");
            totalPriceView.setText(order.getTotalPrice()+" sum");
            dateView.setText(order.getDate());

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
    }
}
