package com.example.FingerHero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SelectTagActivity extends AppCompatActivity {

    private static LinearLayout selectTag;
    private View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button tag_btn = (Button) v;
                String tag = tag_btn.getText().toString();
                Toast.makeText(getApplicationContext(), tag, Toast.LENGTH_SHORT).show();

                if (User.user_tag_selected.contains(tag)) {
                    User.user_tag_selected.remove(tag);
                    tag_btn.setTextColor(Color.BLACK);
                    Toast.makeText(getApplicationContext(), tag + "뺐습니다", Toast.LENGTH_SHORT).show();
                } else {
                    User.user_tag_selected.add(tag);
                    tag_btn.setTextColor(Color.GRAY);
                    Toast.makeText(getApplicationContext(), tag + "추가 했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        };

        selectTag = (LinearLayout) findViewById(R.id.selectTag);


        for(int i = 0; i < 20; i++){
            addTag("tag" + i);
        }

        Button button=(Button)findViewById(R.id.apply);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(v.getContext(), ArticleActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addTag(String tag) {
        Button tag_btn = new Button(this);
        tag_btn.setText(tag);
        tag_btn.setTextSize(20);

        if (User.user_tag_created.containsKey(tag)) { //  이미 생성한 태그
            if(User.user_tag_selected.contains(tag)){       //  이미 선택한 태그
                tag_btn.setTextColor(Color.GRAY);
            }
        } else {                            //  생성할 태그
            tag_btn.setTextColor(Color.BLACK);

            int color_code;

            do{
                int red = (int) (Math.random() * 255);
                int green = (int) (Math.random() * 255);
                int blue = (int) (Math.random() * 255);

                color_code = Color.rgb(red, green, blue);

            } while(User.user_tag_created.containsValue(color_code));

            User.user_tag_created.put(tag, color_code);
        }

        tag_btn.setBackgroundColor(User.user_tag_created.get(tag));

        tag_btn.setOnClickListener(listener);

        //TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
        // ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //lp.gravity = Gravity.CENTER;
        //tag_btn.setLayoutParams(lp);

        selectTag.addView(tag_btn);
    }
}