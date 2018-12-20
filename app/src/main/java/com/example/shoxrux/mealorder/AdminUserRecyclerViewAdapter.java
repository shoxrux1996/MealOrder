package com.example.shoxrux.mealorder;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shoxrux on 12/20/18.
 */

public class AdminUserRecyclerViewAdapter extends RecyclerView.Adapter<AdminUserRecyclerViewAdapter.AdminUserItemViewHolder> {

    private Context context;
    private List<Client> clients;

    public AdminUserRecyclerViewAdapter(Context context, List<Client> clients) {
        this.clients = clients;
        this.context = context;
    }

    @Override
    public AdminUserItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.admin_tab1_item_user,parent, false);
       return new AdminUserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminUserItemViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.populate(client);
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    class AdminUserItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.admin_user_image)
        ImageView imageView;
        @BindView(R.id.admin_user_email)
        TextView emailView;
        @BindView(R.id.admin_user_name)
        TextView nameView;

        public AdminUserItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void populate(Client client){
            //Load image from the server (internet) using Ion
            Ion.with(context).load(client.getImage()).withBitmap()
                    .error(R.drawable.placeholder).placeholder(R.drawable.placeholder).intoImageView(imageView);
            emailView.setText(client.getEmail());
            nameView.setText(client.getFirstName()+" "+ client.getLastName());

        }
    }
}
