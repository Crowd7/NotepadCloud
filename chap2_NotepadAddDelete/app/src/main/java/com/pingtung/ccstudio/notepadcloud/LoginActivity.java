package com.pingtung.ccstudio.notepadcloud;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    //UI variables
    private EditText etMail;
    private EditText etPwd;
    private Button btnLogin;
    private RadioGroup radioGroup;
    private RadioButton rbUser1, rbTest1;

    //Firebase instance variables
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etMail = (EditText)findViewById(R.id.etMail);
        etPwd = (EditText)findViewById(R.id.etPwd);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        rbUser1 = (RadioButton)findViewById(R.id.rbUser1);
        rbTest1 = (RadioButton)findViewById(R.id.rbTest1);

        //取得auth實體(只有一個實體app內皆能共用)
        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etMail.getText().toString();
                String password = etPwd.getText().toString();

                //使用email, password作登入
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //登入成功
                            Log.d("android", "signInWithEmail:onComplete:" + task.isSuccessful());
                        }
                        else {
                            //登入失敗
                            Log.w("android", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "請檢查登入帳密是否正確輸入",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId){
                    case R.id.rbUser1:
                        etMail.setText("user1@example.com");
                        etPwd.setText("abc123");
                        break;
                    case R.id.rbTest1:
                        etMail.setText("test1@example.com");
                        etPwd.setText("xyz123");
                        break;
                }
            }
        });
        rbUser1.setChecked(true);

        //宣告auth狀態變更的監聽器
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();

                if (user!=null) {
                    Log.d("android", "已登入: UserUid="+user.getUid());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    Log.d("android", "已登出");
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        //加入auth監聽器
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //移除auth監聽器
        auth.removeAuthStateListener(authListener);
    }
}
