package com.lcjiang.im.cjeaseuidemo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText account;
    private EditText psd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        account = findViewById(R.id.account);
        psd = findViewById(R.id.psd);

    }

    //注册
    public void regist(View view) {

        if (TextUtils.isEmpty(account.getText())
                || TextUtils.isEmpty(psd.getText()))
            return;

        showProgressDialog("正在注册...");
        new Thread(() -> {
            try {
                EMClient.getInstance().createAccount(account.getText().toString(),psd.getText().toString());//同步方法
                runOnUiThread(() ->  login());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void showProgressDialog(String str){

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(str);

        progressDialog.show();
    }

    //登录
    public void login(View view) {

        if (TextUtils.isEmpty(account.getText())
                || TextUtils.isEmpty(psd.getText()))
            return;

        login();
    }

    private void login() {
        showProgressDialog("登录中...");
        EMClient.getInstance().login(account.getText().toString(),psd.getText().toString(),new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Random random=new Random();
                int temp = random.nextInt(10);
                String nick =temp+"号小川川"+temp + "小圆圆";
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("main", "登录聊天服务器成功！");
                UserCacheManager.save(account.getText().toString(),
                        nick,
                        "这里放头像的图片地址");
                Intent intent = new Intent(MainActivity.this,ChatListActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                progressDialog.dismiss();
                Log.e("main", "登录聊天服务器失败！");
                Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
