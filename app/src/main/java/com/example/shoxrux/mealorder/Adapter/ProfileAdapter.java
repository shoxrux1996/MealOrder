package com.example.shoxrux.mealorder.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoxrux.mealorder.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shoxrux on 12/16/18.
 */


public class ProfileAdapter extends ArrayAdapter {


    private List<String> profileInfos;
    private List<String> icons = Arrays.asList("phone","message","internet","list" );
    public ProfileAdapter(Context context, List<String> profileInfos) {
        super(context, R.layout.profile_list_item);
        this.profileInfos = profileInfos;
    }

    @Override
    public int getCount() {
        return profileInfos.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProfileViewHolder holder = new ProfileViewHolder();
       String text = profileInfos.get(position);
       String icon = icons.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_list_item, parent, false);
        }
        holder.imageView = convertView.findViewById(R.id.profile_item_icon);
        holder.textView = convertView.findViewById(R.id.profile_item_text);
        holder.setData(icon, text);
        return convertView;

    }

    private class ProfileViewHolder{
        ImageView imageView;
        TextView textView;

        void setData(String imageSrc, String text){
            this.imageView.setImageResource( getContext().getResources().getIdentifier(imageSrc, "drawable", getContext().getPackageName()));
            this.textView.setText(text);
        }
    }
}
