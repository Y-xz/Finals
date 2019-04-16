package com.ygz.notes;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ygz.notes.Base.BaseActivity;
import com.ygz.notes.Base.MyUser;
import com.ygz.notes.Config.Constants;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity{
    Boolean notWorkState = true;
    private IntentFilter intentFilter;//意图过滤器
    private NetworkChangeReceiver networkChangeReceiver;//NwtWork 监听广播

    EditText account,password;
    Button login,register,fpassword,networkchange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);
        account = (EditText)findViewById(R.id.et_account);
        password = (EditText)findViewById(R.id.et_password);
        login = (Button)findViewById(R.id.btn_login);
        register = (Button)findViewById(R.id.btn_register);
        //忘记密码
        fpassword = (Button)findViewById(R.id.btn_fpassword);
        networkchange = (Button)findViewById(R.id.btn_networkchange);
/**************************************Login*Button*监听事件******************************************************************************************/
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /*按下操作*/
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    login.setBackgroundResource(R.drawable.login_btn_login_click);
                    login.setTextColor(0Xff9db6e6);
                     /*隐藏软键盘*/
                    hideSoftInputView();
                }
                /*抬起操作*/
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    login.setBackgroundResource(R.drawable.login_btn_login_release);
                    login.setTextColor(0Xff071226);
                    String userPhoneNumber = account.getText().toString();
                    String userPassword = password.getText().toString();

                    MyUser user = new MyUser();
                    user.setUsername(userPhoneNumber);
                    user.setPassword(userPassword);

                    if (!notWorkState) {
                        Toast.makeText(LoginActivity.this, "请检查您的网络设置", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (TextUtils.isEmpty(userPhoneNumber) || TextUtils.isEmpty(userPassword)) {
                        Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    }else{
                        user.login(new SaveListener<MyUser>() {
                            public void done(MyUser myUser, BmobException e) {
                        /*登录ing提示框 dialog*/
                                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                                progressDialog.setMessage("登录ing……");
                                progressDialog.setCancelable(true);
                                if (myUser != null) {
                                    progressDialog.show();
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, NotesActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.in_right,R.anim.in_left);
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


                }
                /*移动*/

                return false;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SmsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_right,R.anim.in_left);
                //在注册页 监听返回键 启动LoginActivity
                 /*隐藏软键盘*/
                hideSoftInputView();
            }
        });
        fpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,PasswordActivity.class);
                startActivity(intent);
                /*优化成正常了逻辑*/
                /*overridePendingTransition(R.anim.in_right,R.anim.in_left);*/
                 /*隐藏软键盘*/
                hideSoftInputView();
            }
        });
/**************************************Login*Button*监听事件******************************************************************************************/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    private class NetworkChangeReceiver extends BroadcastReceiver{
        public void onReceive(Context context,Intent intent){
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()){
                notWorkState = true;
                networkchange.setVisibility(View.GONE);
              //  Toast.makeText(context,"您打开了网络开关",Toast.LENGTH_SHORT).show();
            }else {
                notWorkState = false;
                Toast.makeText(context,"您关闭了网络开关",Toast.LENGTH_SHORT).show();
                networkchange.setVisibility(View.VISIBLE);
            }
        }
    }
}
