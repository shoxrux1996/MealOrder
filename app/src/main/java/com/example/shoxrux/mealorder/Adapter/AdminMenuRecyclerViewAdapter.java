package com.example.shoxrux.mealorder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shoxrux.mealorder.Activity.AdminActivity;
import com.example.shoxrux.mealorder.Activity.AdminMenuInfoActivity;
import com.example.shoxrux.mealorder.ItemClickListener;
import com.example.shoxrux.mealorder.Model.Menu;
import com.example.shoxrux.mealorder.R;
import com.jackandphantom.circularimageview.RoundedImage;
import com.koushikdutta.ion.Ion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shoxrux on 12/20/18.
 */

public class AdminMenuRecyclerViewAdapter extends RecyclerView.Adapter<AdminMenuRecyclerViewAdapter.AdminMenuItemViewHolder> {

    private  Context context;
    private List<Menu> menus;
    public AdminMenuRecyclerViewAdapter(Context context, List<Menu> menus) {
        this.menus = menus;
        this.context = context;
    }

    @Override
    public AdminMenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_tab2_item_menu, parent, false);
        //Create Custom Item View holder
        return new AdminMenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminMenuItemViewHolder holder, int position) {
        //Get menu from list by position
        Menu menu = menus.get(position);
        //Call populate function to set UI of the holder
        holder.populate(menu);
        //If view holder clicks, we will create AdminMenuInfoActivity for menu change
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Intent menuInfo = new Intent(context, AdminMenuInfoActivity.class);
                menuInfo.putExtra("menuInfo", menus.get(position));
                AdminActivity adminActivity = (AdminActivity) context;
                adminActivity.startActivityForResult(menuInfo, AdminActivity.MENU_CHANGED);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    class AdminMenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.menu_rounded_image)
        RoundedImage imageView;
        @BindView(R.id.menu_title)
        TextView titleView;
        @BindView(R.id.menu_price)
        TextView priceView;

        private ItemClickListener itemClickListener;
        public AdminMenuItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //bind the listener
            itemView.setOnClickListener(this);
        }
        //Set Item Click listener interface
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        // function to set UI of the holder
        void populate(Menu menu){
            //Load image from the server (internet) using Ion
            Ion.with(context).load(menu.getImageURL()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
            titleView.setText(menu.getTitle());
            priceView.setText(menu.getPrice()+"");
        }

        @Override
        public void onClick(View view) {
            itemClickListener.OnClick(view, getAdapterPosition());
        }
    }
}
