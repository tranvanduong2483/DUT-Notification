package com.duong.anyquestion;
//thaydoi
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.duong.anyquestion.Tool.ToolSupport;
import com.duong.anyquestion.classes.Question;
import com.google.gson.Gson;

public class DetailQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_question);

        ImageView iv_image = findViewById(R.id.iv_image);
        TextView tv_tittle = findViewById(R.id.tv_tittle);
        TextView tv_field = findViewById(R.id.tv_field);
        TextView tv_note = findViewById(R.id.tv_note);
        TextView tv_money = findViewById(R.id.tv_money);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String question_json = bundle.getString("question");
            Gson gson = new Gson();

            Question question = gson.fromJson(question_json, Question.class);
            Bitmap bitmap = ToolSupport.convertStringBase64ToBitmap(question.getImageString());
            iv_image.setImageBitmap(bitmap);
            tv_tittle.setText(question.getTittle());
            tv_field.setText(question.getField_id() + "");
            tv_note.setText(question.getNote());
            tv_money.setText(question.getMoney() + "");
        }

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
