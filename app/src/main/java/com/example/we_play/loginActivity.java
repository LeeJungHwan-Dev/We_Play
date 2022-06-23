package com.example.we_play;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.Toast;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import com.example.we_play.Module.RequestApiTask;

public class loginActivity extends AppCompatActivity {

    OAuthLogin NaverOAuthLoginModule = OAuthLogin.getInstance();
    ImageButton btn_naverLogin, btn_homeLogin , btn_googleLogin;
    Button btn_go_main;

    private OAuthLoginHandler NaverOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = NaverOAuthLoginModule.getAccessToken(getApplicationContext());
                String refreshToken = NaverOAuthLoginModule.getRefreshToken(getApplicationContext());
                long expiresAt = NaverOAuthLoginModule.getExpiresAt(getApplicationContext());
                String tokenType = NaverOAuthLoginModule.getTokenType(getApplicationContext());
                new RequestApiTask(getApplicationContext(), NaverOAuthLoginModule).execute();
            } else {
                String errorCode = NaverOAuthLoginModule.getLastErrorCode(getApplicationContext()).getCode();
                String errorDesc = NaverOAuthLoginModule.getLastErrorDesc(getApplicationContext());
                Toast.makeText(getApplicationContext(), "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_naverLogin = findViewById(R.id.n);
        btn_homeLogin = findViewById(R.id.h);
        btn_googleLogin = findViewById(R.id.g);
        btn_go_main = findViewById(R.id.button5);


        NaverOAuthLoginModule.init(
                loginActivity.this
                ,getString(R.string.naver_client_id)
                ,getString(R.string.naver_client_secret)
                ,getString(R.string.app_name)
        );

        btn_naverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naverLogin();
            }
        });

        btn_homeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
            }
        });

        btn_go_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Main_page.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        });

//        btn_googleLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),googleLogin.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
//            }
//        });
    }


    private void naverLogin() {
        NaverOAuthLoginModule.startOauthLoginActivity(this, NaverOAuthLoginHandler);
    }
}