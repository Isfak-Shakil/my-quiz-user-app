package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private TextView logoTv,poweredTv;
    private ImageView splashIcon;
    public static List<CategoryModel> catList=new ArrayList<>();
    public  static  int selected_cat_index=0;

    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoTv=findViewById(R.id.logoTv);
        poweredTv=findViewById(R.id.poweredTv);
        splashIcon=findViewById(R.id.splashIcon);
        Typeface typeface= ResourcesCompat.getFont(this,R.font.blacklist);
        logoTv.setTypeface(typeface);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.myanim);
        Animation side_anim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        Animation bottom_anim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        logoTv.setAnimation(myanim);
        splashIcon.setAnimation(bottom_anim);
        poweredTv.setAnimation(side_anim);

        firestore=FirebaseFirestore.getInstance();



        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();

            }
        },1000);
    }

    private void loadData() {
    catList.clear();
    firestore.collection("QUIZ").document("Categories")
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()){
                DocumentSnapshot doc=task.getResult();
                if (doc.exists()){
                    long count= (long) doc.get("COUNT");
                    for (int i=1;i<=count;i++){
                        String catName=doc.getString("CAT"+String.valueOf(i)+"_NAME");
                        String catID=doc.getString("CAT"+String.valueOf(i)+"_ID");
                        catList.add(new CategoryModel(catID,catName));
                    }
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(SplashActivity.this,"No Category Found",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            else {
                Toast.makeText(SplashActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    });

    }
}