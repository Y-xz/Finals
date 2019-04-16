package com.ygz.notes;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ygz.notes.Base.ActivityCollector;
import com.ygz.notes.Base.BaseActivity;
import com.ygz.notes.Base.MyUser;
import com.ygz.notes.Base.Notes;
import com.ygz.notes.Config.Constants;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddNoteActivity extends BaseActivity implements View.OnClickListener {
    Button addNote,Back;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_note);

        addNote = (Button)findViewById(R.id.btn_addsendnote);
        Back = (Button)findViewById(R.id.btn_addnoteback);
        editText = (EditText)findViewById(R.id.et_addnote);

        addNote.setOnClickListener(this);
        Back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addsendnote:
                MyUser UserInfo = BmobUser.getCurrentUser(MyUser.class);
                String userId = UserInfo.getObjectId().toString();
                String userName = UserInfo.getUsername().toString();
                String nickName = UserInfo.getNickname().toString();
                /*需要头像，虽然帖子是加载本地的，但是即时通讯里面的头像传输需要网址*/
                String avatar = UserInfo.getAvatar().toString();
                String gander = UserInfo.getGander().toString();
                String note = editText.getText().toString();
                if (TextUtils.isEmpty(note)){
                    Toast.makeText(AddNoteActivity.this,"不能发送空纸条哦",Toast.LENGTH_SHORT).show();
                    return;
                }
                Notes userNote = new Notes();
                userNote.setUserId(userId);
                userNote.setUserName(userName);
                userNote.setNickName(nickName);
                userNote.setGander(gander);
                userNote.setAvatar(avatar);
                userNote.setNote(note);
                userNote.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            Toast.makeText(AddNoteActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                            /**平滑移动回去
                             * Intent intent = new Intent(AddNoteActivity.this, NotesActivity.class);
                            startActivity(intent);*/
                            /*overridePendingTransition(R.anim.out_right,R.anim.out_left);*/
                            finish();
                        }else {
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

                break;
            case R.id.btn_addnoteback:
                finish();
                break;
            default:
                break;
        }
    }

}
