package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapp.SplashActivity.catList;
import static com.example.myapp.SplashActivity.selected_cat_index;

public class SetsActivity extends AppCompatActivity {
    private GridView setsGridView;
    private ImageButton backBtn;
    private TextView setsTextView,nameTollbar;

    private FirebaseFirestore firestore;
   public  static List<String>  setIDs=new ArrayList<>();
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);
        setsGridView=findViewById(R.id.setsGridView);
        backBtn=findViewById(R.id.backBtn);
        setsTextView=findViewById(R.id.setsTextView);
        nameTollbar=findViewById(R.id.nameTollbar);


        nameTollbar.setText(catList.get(selected_cat_index).getName());

        Typeface typeface= ResourcesCompat.getFont(this,R.font.blacklist);
        setsTextView.setTypeface(typeface);

        Animation sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        setsTextView.setAnimation(sideAnim);
        loadingDialog=new Dialog(SetsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        firestore=FirebaseFirestore.getInstance();
        loadSets();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void loadSets() {
        setIDs.clear();

        firestore.collection("QUIZ").document(catList.get(selected_cat_index).getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long noOfSets=(long)documentSnapshot.get("SETS");
                for (int i=1;i<=noOfSets;i++){
                    setIDs.add(documentSnapshot.getString("SET"+String.valueOf(i)+"_ID"));
                }

                SetsAdapter setsAdapter=new SetsAdapter(setIDs.size());
                      setsGridView.setAdapter(setsAdapter);
                      loadingDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SetsActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        loadingDialog.dismiss();
            }
        });
    }

//   public void loadSets() {
//       firestore.collection("QUIZ").document("CAT"+String.valueOf(category_id))
//               .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//           @Override
//           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//               if (task.isSuccessful()){
//                   DocumentSnapshot doc=task.getResult();
//                   if (doc.exists()){
//                       long sets= (long) doc.get("SETS");
//                       SetsAdapter setsAdapter=new SetsAdapter((int)sets);
//                       setsGridView.setAdapter(setsAdapter);
//                   }else {
//                       Toast.makeText(SetsActivity.this,"No Sets Found",Toast.LENGTH_SHORT).show();
//                       finish();
//                   }
//               }
//               else {
//                   Toast.makeText(SetsActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
//               }
//               loadingDialog.cancel();
//           }
//       });
//
//   }


    }
