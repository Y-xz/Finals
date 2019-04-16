package com.ygz.notes.UserDataSet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.ygz.notes.Base.BaseActivity;
import com.ygz.notes.Base.MyUser;
import com.ygz.notes.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class NickNameSetActivity extends BaseActivity {
    EditText nickName;
    Button remove,back,submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nick_name_set);
        init();
        touch();
    }

    private void init(){
        nickName = (EditText)findViewById(R.id.et_set_nick_name);
        remove = (Button)findViewById(R.id.btn_set_nick_name_remove);
        back = (Button)findViewById(R.id.btn_nick_name_set_back);
        submit = (Button)findViewById(R.id.btn_nick_name_set_submit);

        Intent intentData = getIntent();
        String nn = intentData.getStringExtra("NN");
        nickName.setText(nn);
        nickName.setSelection(nickName.getText().length());
    }
    private void touch(){
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
                    String newNN = nickName.getText().toString().trim();
                    MyUser myUser = new MyUser();
                    myUser.setNickname(newNN);
                    MyUser userID = MyUser.getCurrentUser(MyUser.class);
                    myUser.update(userID.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                toast("昵称更改成功");
                                finish();
                            }else {
                                toast("更改失败"+e.getMessage());
                            }
                        }
                    });

                }
                return false;
            }
        });
        /*清空*/
        remove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    nickName.setText("");
                }
                return false;
            }
        });
    }

}
