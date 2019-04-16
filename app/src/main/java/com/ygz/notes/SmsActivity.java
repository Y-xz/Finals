package com.ygz.notes;



import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;


public class SmsActivity extends BaseActivity{
    Button getsms,next;
    EditText phonenumber,testnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        getsms = (Button)findViewById(R.id.btn_smsget);
        next = (Button)findViewById(R.id.btn_smsnext);
        phonenumber = (EditText)findViewById(R.id.et_smsphonenumber);
        testnumber = (EditText)findViewById(R.id.et_smstest);

        /*获取验证码*/
        getsms.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final String userPhoneNumber = phonenumber.getText().toString().trim();

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    getsms.setBackgroundResource(R.drawable.password_btn_getsms_click);
                    getsms.setTextColor(0Xff9db6e6);
                     /*隐藏软键盘*/
                    hideSoftInputView();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    getsms.setBackgroundResource(R.drawable.password_btn_getsms_release);
                    getsms.setTextColor(0Xff071226);

                    if (userPhoneNumber.length() != 11){
                        toast("请输入正确的手机号码");
                        /*Toast.makeText(this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();*/
                    }else {
                    /*查询手机号是否重复，提醒用户已经注册过*/
                        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                        query.addWhereEqualTo("username",userPhoneNumber);
                        query.findObjects(new FindListener<MyUser>() {
                            @Override
                            public void done(List<MyUser> object, cn.bmob.v3.exception.BmobException e) {
                                if (e == null){
                                    String str = "";
                                    for (MyUser javabean:object){
                                        str = javabean.getMobilePhoneNumber();
                                    }
                                    if (str.equals("")){
                                        //未找到重复手机号发送短信
                                            BmobSMS.requestSMSCode(SmsActivity.this, userPhoneNumber, "Notes", new RequestSMSCodeListener() {
                                                @Override
                                                public void done(Integer integer, BmobException e) {
                                                    if (e == null){
                                                        getsms.setClickable(false);
                                                        getsms.setBackgroundResource(R.drawable.password_btn_getsms_click);
                                                        getsms.setTextColor(0Xff9db6e6);
                                                        Toast.makeText(SmsActivity.this,"验证码发送成功，请在十分钟内使用", Toast.LENGTH_SHORT).show();
                                                        new CountDownTimer(60000,1000){
                                                            @Override
                                                            public void onTick(long millisUntilFinished) {
                                                                getsms.setText(millisUntilFinished/1000 + "秒后重新发送");
                                                            }
                                                            @Override
                                                            public void onFinish() {
                                                                getsms.setClickable(true);
                                                                getsms.setText("重新发送");
                                                            }
                                                        }.start();
                                                    }else {
                                                        Toast.makeText(SmsActivity.this,"验证码发送失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    }else {
                                    /*取消Toast，做一个 美美哒 按钮（类似于网络监听）（简洁醒目的背景）显示，平常为隐藏状态*/
                                        Toast.makeText(SmsActivity.this,"该手机号已被注册，请返回直接登录或找回密码",Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(SmsActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }

                return false;
            }
        });

        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final String userPhoneNumber = phonenumber.getText().toString().trim();
                String userTestNumber = testnumber.getText().toString().trim();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    next.setBackgroundResource(R.drawable.password_btn_getsms_click);
                    next.setTextColor(0Xff9db6e6);
                     /*隐藏软键盘*/
                    hideSoftInputView();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    next.setBackgroundResource(R.drawable.password_btn_getsms_release);
                    next.setTextColor(0Xff071226);

                    BmobSMS.verifySmsCode(SmsActivity.this, userPhoneNumber, userTestNumber, new VerifySMSCodeListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                String smscode = testnumber.getText().toString().trim();
                                Intent intent = new Intent(SmsActivity.this,RegisterActivity.class);
                                intent.putExtra("extra_userphonenumber",userPhoneNumber);//将手机号传递给下一个活动
                                intent.putExtra("extra_usersmscode",smscode);
                                startActivity(intent);
                                overridePendingTransition(R.anim.in_right,R.anim.in_left);
                                overridePendingTransition(R.anim.in_right,R.anim.in_left);
                                Toast.makeText(SmsActivity.this,"验证成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(SmsActivity.this,"验证码不匹配,验证失败",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                    });

                }


                return false;
            }
        });

    }

}
