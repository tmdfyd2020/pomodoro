package org.techtown.pomodoro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {


    Button button;
    EditText editText_id;
    EditText editText_password;
    EditText editText_name;
    EditText editText_email;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText_id = findViewById(R.id.id_Register);
        editText_password = findViewById(R.id.lPassword_Register);
        editText_name = findViewById(R.id.name_Register);
        editText_email = findViewById(R.id.email_Register);

        context = this;


        button = (Button) findViewById(R.id.RegisterBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect_Register task = new Connect_Register(Register.this);
                task.execute(editText_id.getText().toString(), editText_password.getText().toString(),
                        editText_name.getText().toString(),editText_email.getText().toString());
            }
        });

        TextView textView = findViewById(R.id.haveAccount);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void registerSuccess()
    {
        finish();
    }
    public void registerFailed()
    {
        Toast.makeText(getApplicationContext(),"Register failed",Toast.LENGTH_LONG).show();
    }

}