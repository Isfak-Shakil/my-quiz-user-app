package com.example.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import static com.example.myapp.SplashActivity.selected_cat_index;

public class CateGoryAdapter extends BaseAdapter {
    private List<CategoryModel> catList;

    public CateGoryAdapter(List<CategoryModel> catList) {
        this.catList= catList;
    }

    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if (convertView==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
        }
        else {
            view=convertView;
        }
        ((TextView)view.findViewById(R.id.categoryText)).setText(catList.get(position).getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_cat_index=position;
                Intent intent=new Intent(parent.getContext(),SetsActivity.class);
                parent.getContext().startActivity(intent);
            }
        });
        // generating random color
//        Random random=new Random();
//        int color= Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255));
//        view.setBackgroundColor(color);

        return view;
    }
}
