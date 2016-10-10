package com.pingtung.ccstudio.notepadcloud;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {
    //UI variables
    private Button btn_ok, btn_cancel;
    private EditText etTitle, etContent;

    //intent from caller
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        //find UId
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etContent = (EditText)findViewById(R.id.etContent);

        //intent from caller
        intent = getIntent();

        //若是EDIT_NOTE action，則顯示原本的title及content內容
        if(intent.getAction().equals("com.pingtung.ccstudio.notepadcloud.EDIT_NOTE")){
            String title,content;
            title = intent.getStringExtra("TITLE");
            content = intent.getStringExtra("CONTENT");
            etTitle.setText(title);
            etContent.setText(content);
        }

        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.btn_ok){
                    String title,content;
                    title = etTitle.getText().toString();
                    content = etContent.getText().toString();

                    if(title.equals("") || content.equals("")){
                        Toast.makeText(NoteActivity.this,"請輸入title及content",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    intent.putExtra("TITLE",title);
                    intent.putExtra("CONTENT",content);
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }
        };

        btn_ok.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);

    }
}
