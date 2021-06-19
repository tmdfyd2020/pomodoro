package org.techtown.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Weekly extends AppCompatActivity {

    Button ReturnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);

        ReturnBtn = findViewById(R.id.ReturnBtn);
        ReturnBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();  // 여기서 돌아갈 때 처리 다시 해 줘야 함
            }
        });
    }
}