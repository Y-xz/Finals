package com.ygz.notes.UserDataSet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ygz.notes.Base.BaseActivity;
import com.ygz.notes.Base.MyUser;
import com.ygz.notes.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class GenderSetActivity extends BaseActivity {
    Button back,submit;
    RadioButton male,female,secrecy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gender_set);
        init();
        touch();
    }

    private void init(){

        back = (Button)findViewById(R.id.btn_gender_set_back);
        submit = (Button)findViewById(R.id.btn_gender_set_submit);
        male = (RadioButton)findViewById(R.id.rb_set_male);
        female = (RadioButton)findViewById(R.id.rb_set_female);
        secrecy = (RadioButton)findViewById(R.id.rb_set_secrecy);

        Intent intentData = getIntent();
        String Gender = intentData.getStringExtra("GENDER");
        if (Gender.equals("男")){
            male.setChecked(true);
        }
        if (Gender.equals("女")){
            female.setChecked(true);
        }
        if (Gender.equals("保密")){
            secrecy.setChecked(true);
        }

    }
    private void touch() {
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
        /*提交*/
        submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    /*提交操作*/
                    String newGander = "";
                    String newAvatar = "";

                    if (male.isChecked()){
                        newGander = male.getText().toString();
                        newAvatar = "http://bmob-cdn-16737.b0.upaiyun.com/2018/02/07/4e83623840b686bb801b9b0199303f20.png";
                    }
                    if (female.isChecked()){
                        newGander = female.getText().toString();
                        newAvatar = "http://bmob-cdn-16737.b0.upaiyun.com/2018/02/07/4df5d6df400107ce8089e520b3025bc8.png";
                    }
                    if (secrecy.isChecked()){
                        newGander = secrecy.getText().toString();
                        newAvatar = "http://bmob-cdn-16737.b0.upaiyun.com/2018/05/06/3d3a309940b5e9ab8024d36fb3b0c69b.png";
                    }
                    MyUser myUser = new MyUser();
                    myUser.setGander(newGander);
                    myUser.setAvatar(newAvatar);
                    MyUser userID = MyUser.getCurrentUser(MyUser.class);
                    myUser.update(userID.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                toast("性别更改成功");
                                finish();
                            }else {
                                toast("性别更改成功"+e.getMessage());
                            }
                        }
                    });
                }
                return false;
            }
        });
    }
}
