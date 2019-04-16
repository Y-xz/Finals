package com.ygz.notes.UserDataSet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ygz.notes.Base.BaseActivity;
import com.ygz.notes.Base.MyUser;
import com.ygz.notes.R;

import cn.bmob.v3.BmobUser;

public class UserDataSetActivity extends BaseActivity {
    Button back;
    TextView nickName,gender,phoneNumber;
    ImageButton avatar;
    RelativeLayout RL_nickName,RL_Gender,RL_phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_data_set);
        init();
        touch();

    }
    /*控件初始化*/
    protected void init(){
        back = (Button)findViewById(R.id.btn_user_data_set_back);

        avatar = (ImageButton)findViewById(R.id.ib_user_data_set_avatar);
        nickName = (TextView)findViewById(R.id.tv_user_data_set_nick_name);
        gender = (TextView)findViewById(R.id.tv_user_data_set_gender);
        phoneNumber = (TextView)findViewById(R.id.tv_user_data_set_phone_number);

        RL_nickName = (RelativeLayout)findViewById(R.id.rl_user_data_set_nick_name);
        RL_Gender = (RelativeLayout)findViewById(R.id.rl_user_data_set_gender);
        RL_phoneNumber = (RelativeLayout)findViewById(R.id.rl_user_data_set_phone_number);

        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        String nickNameInfo = userInfo.getNickname().toString();
        String genderInfo = userInfo.getGander().toString();
        String phoneNumberInfo = userInfo.getMobilePhoneNumber().toString();

        nickName.setText(nickNameInfo);
        gender.setText(genderInfo);
        phoneNumber.setText(phoneNumberInfo);
        if (genderInfo.equals("男")){
            avatar.setBackgroundResource(R.drawable.m0);
        }else if (genderInfo.equals("女")){
            avatar.setBackgroundResource(R.drawable.w0);
        }else if (genderInfo.equals("保密")){
            avatar.setBackgroundResource(R.drawable.s0);
        }
    }

    /*控件的Touch监听操作*/
    protected void touch(){
        /*返回键*/
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    finish();
                }

                return false;
            }
        });
        /*昵称*/
        RL_nickName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    RL_nickName.setBackgroundColor(0Xff000000);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    RL_nickName.setBackgroundColor(0Xffffffff);
                    String nn = nickName.getText().toString();
                    Intent intent = new Intent(UserDataSetActivity.this, NickNameSetActivity.class);
                    intent.putExtra("NN",nn);
                    startActivity(intent);


                }

                return false;
            }
        });
        /*性别*/
        RL_Gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    RL_Gender.setBackgroundColor(0Xff000000);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    RL_Gender.setBackgroundColor(0Xffffffff);
                    String Gender = gender.getText().toString();
                    Intent intent = new Intent(UserDataSetActivity.this, GenderSetActivity.class);
                    intent.putExtra("GENDER",Gender);
                    startActivity(intent);

                }

                return false;
            }
        });

        RL_phoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    RL_phoneNumber.setBackgroundColor(0Xff000000);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    RL_phoneNumber.setBackgroundColor(0Xffffffff);

                    Intent intent = new Intent(UserDataSetActivity.this, PhoneNumberSetActivity.class);
                    startActivity(intent);

                }

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        String nickNameInfo = userInfo.getNickname().toString();
        String genderInfo = userInfo.getGander().toString();
        String phoneNumberInfo = userInfo.getMobilePhoneNumber().toString();

        nickName.setText(nickNameInfo);
        gender.setText(genderInfo);
        phoneNumber.setText(phoneNumberInfo);
        if (genderInfo.equals("男")){
            avatar.setBackgroundResource(R.drawable.m0);
        }else if (genderInfo.equals("女")){
            avatar.setBackgroundResource(R.drawable.w0);
        }else if (genderInfo.equals("保密")){
            avatar.setBackgroundResource(R.drawable.s0);
        }
    }
}
