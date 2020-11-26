package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.myapp.SetsActivity.setIDs;
import static com.example.myapp.SplashActivity.catList;
import static com.example.myapp.SplashActivity.selected_cat_index;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener {
        private TextView qCount,question,qTimer;
        private Button option1,option2,option3,option4;
        private List<Question> questionList;
        private FirebaseFirestore firestore;
        private  int setNo;
        // gg
        private Dialog  loadingDialog;
    private int questionNumber;
    private CountDownTimer countDownTimer;
    private  int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        qCount=findViewById(R.id.qCount);
        question=findViewById(R.id.question);
        qTimer=findViewById(R.id.qTimer);
        option1=findViewById(R.id.option1);
        option2=findViewById(R.id.option2);
        option3=findViewById(R.id.option3);
        option4=findViewById(R.id.option4);


        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        loadingDialog=new Dialog(QuestionsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        questionList=new ArrayList<>();

        setNo=getIntent().getIntExtra("SETNO",1);
        firestore=FirebaseFirestore.getInstance();
        score=0;
        getQuestionsList();

    }
// getting question
    private void getQuestionsList() {
     questionList.clear();
        firestore.collection("QUIZ").document(catList.get(selected_cat_index).getId())
                .collection(setIDs.get(setNo)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String,QueryDocumentSnapshot> docList=new ArrayMap<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            docList.put(doc.getId(),doc);
                        }
                        QueryDocumentSnapshot quesListDoc=docList.get("QUESTIONS_LIST");
                        assert quesListDoc != null;
                        String count=quesListDoc.getString("COUNT");
                        for (int i = 0; i<Integer.parseInt(Objects.requireNonNull(count)); i++){
                            String quesID=quesListDoc.getString("Q"+ (i+1) +"_ID");
                            QueryDocumentSnapshot quesDoc=docList.get(quesID);
                            //assert quesDoc != null;
                            questionList.add(new Question(
                                    quesDoc.getString("QUESTION"),
                                    quesDoc.getString("A"),
                                    quesDoc.getString("B"),
                                    quesDoc.getString("C"),
                                    quesDoc.getString("D"),
                                    Integer.parseInt(Objects.requireNonNull(quesDoc.getString("ANSWER")))
                            ));
                        }

                        setQuestion();
                        loadingDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });

    }

    private void setQuestion() {
        qTimer.setText(String.valueOf(10));
        question.setText(questionList.get(0).getQuestion());
        option1.setText(questionList.get(0).getOptionA());
        option2.setText(questionList.get(0).getOptionB());
        option3.setText(questionList.get(0).getOptionC());
        option4.setText(questionList.get(0).getOptionD());

        qCount.setText(String.valueOf(1)+ "/" + String.valueOf(questionList.size()));
        startTimer();
        questionNumber=0;
    }
    // setting timer
    private void startTimer() {

        countDownTimer=new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished<10000){
                    qTimer.setText(String.valueOf(millisUntilFinished/1000));
                }
            }

            @Override
            public void onFinish() {
            changeQuestion();
            }
        };

       countDownTimer.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int selectedOption=0;
        switch (v.getId()){
            case  R.id.option1:
                selectedOption=1;
                break;
            case R.id.option2:
                selectedOption=2;
                break;
            case R.id.option3:
                selectedOption=3;
                break;
            case R.id.option4:
                selectedOption=4;
                break;
            default:
        }
        countDownTimer.cancel();
        checkAnswer(selectedOption,v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(int selectedOption, View view) {
if (selectedOption==questionList.get(questionNumber).getCorrectAns())
    {
        // right answer
        ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        score++;
    }
        else {
            // wrong answer
    ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
      switch (questionList.get(questionNumber).getCorrectAns()){
          case 1:option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN)); break;
          case 2:option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN)); break;
          case 3:option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN)); break;
          case 4:option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN)); break;
            }
            }
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        },2000);

    }
// hjkk b,b fgfg
    private void changeQuestion() {
        if (questionNumber<questionList.size()-1){
            questionNumber++;
            // changing question option
            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);

            qCount.setText(String.valueOf(questionNumber+1)+"/"+String.valueOf(questionList.size()));
            qTimer.setText(String.valueOf(10));
            startTimer();


        }
        else {
            Intent intent=new Intent(QuestionsActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE",String.valueOf(score)+"/"+String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    private void playAnim(final View view, final  int value, final int viewNum) {
    view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
            .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(value==0){
                        switch (viewNum)
                        {
                            case 0:
                                ((TextView)view).setText(questionList.get(questionNumber).getQuestion());
                                break;
                                case 1:
                                ((Button)view).setText(questionList.get(questionNumber).getOptionA());
                                break;
                                case 2:
                                ((Button)view).setText(questionList.get(questionNumber).getOptionB());
                                break;
                                case 3:
                                ((Button)view).setText(questionList.get(questionNumber).getOptionC());
                                break;
                                case 4:
                                ((Button)view).setText(questionList.get(questionNumber).getOptionD());
                                break;
                        }
                        playAnim(view,1,viewNum);
                        if (viewNum!=0){
                            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E99C03")));
                        }

                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }
}