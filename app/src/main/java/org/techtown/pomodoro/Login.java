package org.techtown.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button button;
    EditText editText_id;
    EditText editText_password;
    public static boolean login= false;
    public static String num_user;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText_id = findViewById(R.id.id);
        editText_password = findViewById(R.id.lPassword);

        context = this;


        button = (Button) findViewById(R.id.loginBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect_Login task = new Connect_Login(Login.this);
                task.execute(editText_id.getText().toString(), editText_password.getText().toString());
            }
        });

        TextView textView = findViewById(R.id.createAccount);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), Register.class);
                startActivity(registerIntent);
            }
        });
    }
    public void loginSuccess()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("num_user", num_user);
        startActivity(intent);
    }
    public void loginFailed()
    {
        Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_LONG).show();
    }
}