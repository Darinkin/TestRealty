package com.example.darina.testrealty;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

public class LoginActivity extends AppCompatActivity {

    Button Enter;
    TextView Login;
    TextView Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        int is_online = DBHelper.getInstance(LoginActivity.this).is_online();
        if (is_online > -1) {
            Intent intObj = new Intent(LoginActivity.this, ObjectsListActivity.class);
            startActivity(intObj);
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Авторизация");

        Login = (TextView) findViewById(R.id.editText3);
        Password = (TextView) findViewById(R.id.editText4);
        Enter = (Button) findViewById(R.id.button);

        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите хотя бы логин",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else if (Password.length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "А пароль кто вводить будет?",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                String acc_Login = String.valueOf(Login.getText());
                String acc_Password = String.valueOf(Password.getText());
                boolean success = DBHelper.getInstance(LoginActivity.this).auth(acc_Login, acc_Password);
                if (success) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Аутентификация успешна.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    Intent intObj = new Intent(LoginActivity.this, ObjectsListActivity.class);
                    intObj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intObj);
                    return;
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Пара логин-пароль не верна!",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

            }
        });

    }
}