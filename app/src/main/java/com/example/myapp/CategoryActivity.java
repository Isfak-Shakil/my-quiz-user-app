package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapp.SplashActivity.catList;

public class CategoryActivity extends AppCompatActivity {
    private ImageButton backBtn;
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        backBtn=findViewById(R.id.backBtn);
        gridView=findViewById(R.id.gridView);


        CateGoryAdapter cateGoryAdapter=new CateGoryAdapter(catList);
        gridView.setAdapter(cateGoryAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}