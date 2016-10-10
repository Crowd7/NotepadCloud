package com.pingtung.ccstudio.notepadcloud;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //UI variables
    private TextView tvUser;
    private ListView listView;
    private EditText etTitle, etContent;

    //餵給listView的相關變數
    private ItemPlusAdapter itemPlusAdapter;
    private List<ItemPlus> itemPlusList;

    //Firebase instance variables
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //local variable
        String userUid="";

        //find UId
        tvUser = (TextView)findViewById(R.id.tvUser);
        listView = (ListView)findViewById(R.id.listView);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etContent = (EditText)findViewById(R.id.etContent);

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
            userUid = user.getUid();
        }

        //實體化餵給listView的相關變數: arrayList, adapter
        itemPlusList = new ArrayList<>();
        itemPlusAdapter = new ItemPlusAdapter(MainActivity.this,R.layout.single_line,itemPlusList);
        listView.setAdapter(itemPlusAdapter);

        //取得firebase根目錄下"/users/$uid"的reference
        //不同登入的使用者有不同的Uid，無法存取其他人的資料
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("users/"+userUid);

        //對使用者根目錄(/users/$uid)註冊一子目錄變更監聽器
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Log.d("android","onChildAdded: "+ key);
                Item item = dataSnapshot.getValue(Item.class);
                ItemPlus itemPlus = new ItemPlus(item, key);
                itemPlusList.add(itemPlus);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                Log.d("android", "onChildRemoved: " + key);

                ItemPlus tmp=null;
                for(ItemPlus ip:itemPlusList){
                    if(ip.getKey().equals(key)){
                        tmp = ip;
                        break;
                    }
                }
                if(itemPlusList.contains(tmp))
                    itemPlusList.remove(tmp);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addChildEventListener(childEventListener);

        //註冊使用者根目錄監聽器
        //只要子目錄值一有變更，便會收到通知
        //通知adapter資料有變更，刷新listView顯示
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("android","onDataChange");
                itemPlusAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addValueEventListener(valueEventListener);

        //註冊listView長按事件
        //詢問是否刪除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final ItemPlus itemPlus = itemPlusList.get(position);

                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);

                ab.setTitle(itemPlus.getTitle())
                        .setMessage("是否刪除此筆紀錄?")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(true);
                ab.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRef.child(itemPlus.getKey()).removeValue();
                    }
                });
                ab.setNegativeButton("取消", null);
                ab.show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add:
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();

                if(title.equals("") || content.equals("")){
                    Toast.makeText(MainActivity.this,"請輸入title及content",Toast.LENGTH_SHORT).show();
                }else{
                    //使用push()讓系統自動產生key值
                    //key值依timestamp產生，不會重複且不斷向上遞增
                    mRef.push().setValue(new Item(title,content));
                    etTitle.setText("");
                    etContent.setText("");
                }
                break;
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
