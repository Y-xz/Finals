package com.ygz.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ygz.notes.Base.ActivityCollector;
import com.ygz.notes.Base.BaseActivity;
import com.ygz.notes.Base.MyUser;
import com.ygz.notes.Config.Constants;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

//实现界面的右侧平滑移动过来！！！
public class RegisterActivity extends BaseActivity {
    EditText password,cpassword,nickName;
    Button register;
    /*性别 RadioGroup gander;*/
    RadioButton male,female,secrecy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /*Bmob.initialize(this, Constants.Bmob_APPID);*/

        nickName = (EditText)findViewById(R.id.et_r_nickname);
        /*gander = (RadioGroup)findViewById(R.id.rg_r_gander);*/
        male = (RadioButton)findViewById(R.id.rb_r_male);
        female = (RadioButton)findViewById(R.id.rb_r_female);
        secrecy = (RadioButton)findViewById(R.id.rb_r_secrecy);

        password = (EditText)findViewById(R.id.et_r_password);
        cpassword = (EditText)findViewById(R.id.et_r_cpassword);
        register = (Button)findViewById(R.id.btn_r_register);


        register.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    register.setBackgroundResource(R.drawable.login_btn_login_click);
                    register.setTextColor(0Xff9db6e6);
                     /*隐藏软键盘*/
                    hideSoftInputView();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    register.setBackgroundResource(R.drawable.login_btn_login_release);
                    register.setTextColor(0Xff071226);
                }

                return false;
            }
        });



        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String NickName = nickName.getText().toString();
                String Gander = "";
                String userPassword = password.getText().toString().trim();
                //重复密码
                String usercPassword = cpassword.getText().toString().trim();
                if (male.isChecked()){
                    Gander = male.getText().toString();
                }
                if (female.isChecked()){
                    Gander = female.getText().toString();
                }
                if (secrecy.isChecked()){
                    Gander = secrecy.getText().toString();
                }


                if (TextUtils.isEmpty(NickName)){
                    NickName = "匿名人";
                }
                if (TextUtils.isEmpty(Gander)){
                    Toast.makeText(RegisterActivity.this,"请选择你的性别",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userPassword)){
                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(usercPassword)){
                    Toast.makeText(RegisterActivity.this,"请输入确认密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(userPassword.equals(usercPassword))){
                    Toast.makeText(RegisterActivity.this,"两次密码输入不一致，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intentData = getIntent();
                String userPhoneNumber = intentData.getStringExtra("extra_userphonenumber");
                /*String userSmscode = intentData.getStringExtra("extra_usersmscode");*/
                final MyUser reg = new MyUser();
                reg.setNickname(NickName);
                reg.setGander(Gander);
                if (Gander.equals("男")){
                    reg.setAvatar("http://bmob-cdn-16737.b0.upaiyun.com/2018/02/07/4e83623840b686bb801b9b0199303f20.png");
                }else if (Gander.equals("女")){
                    reg.setAvatar("http://bmob-cdn-16737.b0.upaiyun.com/2018/02/07/4df5d6df400107ce8089e520b3025bc8.png");
                }else if (Gander.equals("保密")){
                    reg.setAvatar("http://bmob-cdn-16737.b0.upaiyun.com/2018/05/06/3d3a309940b5e9ab8024d36fb3b0c69b.png");
                }
                reg.setUsername(userPhoneNumber);
                reg.setMobilePhoneNumber(userPhoneNumber);
                reg.setMobilePhoneNumberVerified(true);
                reg.setPassword(userPassword);
                reg.signUp(new SaveListener<MyUser>() {
                    public void done(MyUser myUser, BmobException e){
                        if (e == null){
                            BmobUser.logOut();
                            Intent intentAcivity = new Intent(RegisterActivity.this,LoginActivity.class);
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            startActivity(intentAcivity);
                            ActivityCollector.finishAll();
                        }else {
                            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }






/*监听返回键事件*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);*/
    }
}
