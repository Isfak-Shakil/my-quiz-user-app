package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
private TextView score;
private Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        score=findViewById(R.id.score);
        done=findViewById(R.id.done);
        Animation sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        score.setAnimation(sideAnim);
        String score_str;
        if (getIntent().hasExtra("SCORE")){
            score_str=getIntent().getStringExtra("SCORE");

        }else score_str="";
        score.setText(score_str);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ScoreActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}