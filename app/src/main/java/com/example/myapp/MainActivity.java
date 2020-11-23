package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView title,title2;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=findViewById(R.id.title);
        title2=findViewById(R.id.title2);
        button=findViewById(R.id.button);

        Typeface typeface= ResourcesCompat.getFont(this,R.font.blacklist);
        title.setTypeface(typeface);
        title2.setTypeface(typeface);

        Animation side_anim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        Animation bottom_anim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        title.setAnimation(side_anim);
        title2.setAnimation(bottom_anim);


       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            startActivity(new Intent(MainActivity.this,CategoryActivity.class));
           }
       });
    }
}