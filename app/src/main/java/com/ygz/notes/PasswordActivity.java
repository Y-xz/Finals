package com.ygz.notes;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ygz.notes.Base.BaseActivity;
import com.ygz.notes.Base.MyUser;
import com.ygz.notes.Config.Constants;

import java.util.List;


import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.Bmob;

import cn.bmob.v3.BmobQuery;


import cn.bmob.v3.BmobUser;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class PasswordActivity extends BaseActivity implements View.OnClickListener {
    EditText PhoneNumber,Test, PassWord,PassCWord;
    Button Get,Change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);


        PhoneNumber = (EditText)findViewById(R.id.et_passphonenumber);
        Test = (EditText)findViewById(R.id.et_passtest);//验证码
        Get = (Button)findViewById(R.id.btn_passsmsget);
        Change = (Button)findViewById(R.id.btn_passchange);
        PassWord = (EditText)findViewById(R.id.et_passuserpassword);//密码
        PassCWord = (EditText)findViewById(R.id.et_passusercpassword);//确认密码

        Get.setOnClickListener(this);
        Change.setOnClickListener(this);

        Change.setVisibility(View.GONE);
        Test.setVisibility(View.GONE);
        PassWord.setVisibility(View.GONE);
        PassCWord.setVisibility(View.GONE);

        Get.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final String userphonenumber = PhoneNumber.getText().toString().trim();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Get.setBackgroundResource(R.drawable.password_btn_getsms_click);
                    Get.setTextColor(0Xff9db6e6);
                     /*隐藏软键盘*/
                    hideSoftInputView();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    Get.setBackgroundResource(R.drawable.password_btn_getsms_release);
                    Get.setTextColor(0Xff071226);

                    if (userphonenumber.length() != 11) {
                        Toast.makeText(PasswordActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(PasswordActivity.this, "努力发送中", Toast.LENGTH_SHORT).show();
                        //*查询手机号是否注册，提醒用户未注册*//*
                        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                        query.addWhereEqualTo("username",userphonenumber);
                        query.findObjects(new FindListener<MyUser>() {
                            @Override
                            public void done(List<MyUser> object, cn.bmob.v3.exception.BmobException e) {
                                if (e == null){
                                    String str = "";
                                    for (MyUser javabean:object){
                                        str = javabean.getMobilePhoneNumber();
                                    }
                                    if (str.equals("")){
                                        //未找到，提醒用户未注册
                                        Toast.makeText(PasswordActivity.this,"该手机号未注册",Toast.LENGTH_SHORT).show();
                                    }else {
                                        BmobSMS.requestSMSCode(PasswordActivity.this, userphonenumber, "Notes", new RequestSMSCodeListener() {
                                            @Override
                                            public void done(Integer integer, BmobException e) {
                                                /*********************************if 判断短信是否发送成功*******************/
                                                if (e == null) {
                                                    Get.setClickable(false);
                                                    Get.setBackgroundColor(Color.GRAY);
                                                    Toast.makeText(PasswordActivity.this, "验证码发送成功，请在十分钟内使用", Toast.LENGTH_SHORT).show();
                                                    Change.setVisibility(View.VISIBLE);
                                                    Test.setVisibility(View.VISIBLE);
                                                    PassWord.setVisibility(View.VISIBLE);
                                                    PassCWord.setVisibility(View.VISIBLE);
                                                    /**************************************设置获取验证码Button 倒计时*************************************/
                                                    new CountDownTimer(60000, 1000) {
                                                        @Override
                                                        public void onTick(long millisUntilFinished) {
                                                            Get.setText(millisUntilFinished / 1000 + "秒后重新发送");
                                                            Get.setBackgroundResource(R.drawable.password_btn_getsms_click);
                                                            Get.setTextColor(0Xff9db6e6);
                                                        }
                                                        @Override
                                                        public void onFinish() {
                                                            Get.setBackgroundResource(R.drawable.password_btn_getsms_release);
                                                            Get.setTextColor(0Xff071226);
                                                            Get.setClickable(true);
                                                            Get.setBackgroundColor(getResources().getColor(R.color.button_c));
                                                            Get.setText("重新发送");
                                                        }
                                                    }.start();
                                                    /**************************************设置获取验证码Button 倒计时*************************************/
                                                } else {
                                                    Toast.makeText(PasswordActivity.this, "验证码发送失败"+e.getMessage()+"+"+e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }else {
                                    Toast.makeText(PasswordActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });

                    }

                }

                return false;
            }
        });


        Change.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Change.setBackgroundResource(R.drawable.password_btn_passchange_click);
                    Change.setTextColor(0Xff9db6e6);
                     /*隐藏软键盘*/
                    hideSoftInputView();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Change.setBackgroundResource(R.drawable.password_btn_passchange_release);
                    Change.setTextColor(0Xff071226);

                    String smscode = Test.getText().toString();
                    String password = PassWord.getText().toString().trim();
                    String passcword = PassCWord.getText().toString().trim();

                    if (TextUtils.isEmpty(password)){
                        Toast.makeText(PasswordActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (TextUtils.isEmpty(passcword)){
                        Toast.makeText(PasswordActivity.this,"输入确认密码不能为空",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (!(password.equals(passcword))){
                        Toast.makeText(PasswordActivity.this,"两次密码输入不一致，请重新输入",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    /**************************************验证短信验证码并更密码*************************************/
                    BmobUser.resetPasswordBySMSCode(smscode, password, new UpdateListener() {
                        @Override
                        public void done(cn.bmob.v3.exception.BmobException ex) {
                            if (ex == null){
                                Toast.makeText(PasswordActivity.this,"密码更改成功",Toast.LENGTH_SHORT).show();
                            /*Intent intent = new Intent(PasswordActivity.this,LoginActivity.class);
                            startActivity(intent);*/
                                finish();
                            }else {
                                Toast.makeText(PasswordActivity.this,"密码更改失败",Toast.LENGTH_SHORT).show();
                                Log.i("smile", "重置失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                            }
                        }
                    });

                }


                return false;
            }
        });


    }







    @Override
    public void onClick(View v) {
        final String userphonenumber = PhoneNumber.getText().toString().trim();

        switch (v.getId()){
            case R.id.btn_passsmsget:

                /*if (userphonenumber.length() != 11) {
                    Toast.makeText(PasswordActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(PasswordActivity.this, "努力发送中", Toast.LENGTH_SHORT).show();
                    /*//*查询手机号是否注册，提醒用户未注册*//**//*
                    BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                    query.addWhereEqualTo("username",userphonenumber);
                    query.findObjects(new FindListener<MyUser>() {
                        @Override
                        public void done(List<MyUser> object, cn.bmob.v3.exception.BmobException e) {
                            if (e == null){
                                String str = "";
                                for (MyUser javabean:object){
                                    str = javabean.getMobilePhoneNumber();
                                }
                                if (str.equals("")){
                                    //未找到，提醒用户未注册
                                    Toast.makeText(PasswordActivity.this,"该手机号未注册",Toast.LENGTH_SHORT).show();
                                }else {
                                    BmobSMS.requestSMSCode(PasswordActivity.this, userphonenumber, "Notes", new RequestSMSCodeListener() {
                                        @Override
                                        public void done(Integer integer, BmobException e) {
                                            *//*********************************if 判断短信是否发送成功*******************//*
                                            if (e == null) {
                                                Get.setClickable(false);
                                                Get.setBackgroundColor(Color.GRAY);
                                                Toast.makeText(PasswordActivity.this, "验证码发送成功，请在十分钟内使用", Toast.LENGTH_SHORT).show();
                                                Change.setVisibility(View.VISIBLE);
                                                Test.setVisibility(View.VISIBLE);
                                                PassWord.setVisibility(View.VISIBLE);
                                                PassCWord.setVisibility(View.VISIBLE);
                                                *//**************************************设置获取验证码Button 倒计时*************************************//*
                                                new CountDownTimer(60000, 1000) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        Get.setText(millisUntilFinished / 1000 + "秒后重新发送");
                                                    }
                                                    @Override
                                                    public void onFinish() {
                                                        Get.setClickable(true);
                                                        Get.setBackgroundColor(getResources().getColor(R.color.button_c));
                                                        Get.setText("重新发送");
                                                    }
                                                }.start();
                                                *//**************************************设置获取验证码Button 倒计时*************************************//*
                                            } else {
                                                Toast.makeText(PasswordActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }else {
                                Toast.makeText(PasswordActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                }*/
                break;
            /**更改密码按钮点击事件**/
            case R.id.btn_passchange:

              /*  String smscode = Test.getText().toString();
                String password = PassWord.getText().toString().trim();
                String passcword = PassCWord.getText().toString().trim();

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(PasswordActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passcword)){
                    Toast.makeText(PasswordActivity.this,"输入确认密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(password.equals(passcword))){
                    Toast.makeText(PasswordActivity.this,"两次密码输入不一致，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                *//**************************************验证短信验证码并更密码*************************************//*
                BmobUser.resetPasswordBySMSCode(smscode, password, new UpdateListener() {
                    @Override
                    public void done(cn.bmob.v3.exception.BmobException ex) {
                        if (ex == null){
                            Toast.makeText(PasswordActivity.this,"密码更改成功",Toast.LENGTH_SHORT).show();
                            *//*Intent intent = new Intent(PasswordActivity.this,LoginActivity.class);
                            startActivity(intent);*//*
                            finish();
                        }else {
                            Toast.makeText(PasswordActivity.this,"密码更改失败",Toast.LENGTH_SHORT).show();
                            Log.i("smile", "重置失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                        }
                    }
                });*/
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
/*        Intent intent = new Intent(PasswordActivity.this,LoginActivity.class);
        startActivity(intent);*/
    }
}

