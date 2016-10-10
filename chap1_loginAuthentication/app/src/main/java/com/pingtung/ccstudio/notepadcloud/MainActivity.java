package com.pingtung.ccstudio.notepadcloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //UI variables
    private TextView tvUser;

    //Firebase instance variables
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find UId
        tvUser = (TextView)findViewById(R.id.tvUser);

        //取得auth實體(只有一個實體app內皆能共用)
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //若無登入帳戶，則getCurrentUser()傳回null
        //開啟LoginActivity
        if(user==null){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else{
            tvUser.setText(user.getEmail());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                //將auth登出
                auth.signOut();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
