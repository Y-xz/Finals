package com.ygz.notes;

import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import com.ygz.notes.Base.BaseActivity;
import com.ygz.notes.Base.MyUser;

import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                if(myUser != null){
                    startActivity(NotesActivity.class,null,true);
                }else {
                    startActivity(LoginActivity.class,null,true);
                }
            }
        },1000);

    }
}
