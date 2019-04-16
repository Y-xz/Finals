package com.ygz.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.ygz.notes.Base.ActivityCollector;
import com.ygz.notes.Base.BaseActivity;
import com.ygz.notes.Base.MyUser;
import com.ygz.notes.Tabs.AttentionFragment;
import com.ygz.notes.Tabs.ChatFragment;
import com.ygz.notes.Tabs.ContainerView;
import com.ygz.notes.Tabs.FragmentAdapter;
import com.ygz.notes.Tabs.NotesFragment;
import com.ygz.notes.UserDataSet.UserDataSetActivity;
import com.ygz.notes.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃神兽保佑Notes ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 */

public class NotesActivity extends BaseActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{
    private DrawerLayout mDrawerLayout;
    Button addNote,logout,dataSetting,softwareSetting;
    ImageButton avatarButton,sideMenuAvatar;


/*    public static void startNotesActivity(Activity activity,int tab){
        Intent intent = new Intent(activity,NotesActivity.class);
        intent.putExtra("tab",tab);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }*/


    private final int ICONS_RES[][] = {
            {
                R.mipmap.ic_message_normal,
                R.mipmap.ic_message_focus

            },
            {
                R.mipmap.ic_home_normal,
                R.mipmap.ic_home_focus
            },
            {
                R.mipmap.ic_mine_normal,
                R.mipmap.ic_mine_focus
            }
    };
    private final int[] TAB_COLORS = new int[]{
            R.color.main_bottom_tab_textcolor_normal,
            R.color.main_bottom_tab_textcolor_selected
    };
    private Fragment[] fragments = {
            new ChatFragment(),
            new NotesFragment(),
            new AttentionFragment()
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notes);
        initViews();

        /*登录检测 Bmob服务器的状态*/
        LoginTest();
        /*头像的 点击 监听事件*/
        avatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        addNote.setOnClickListener(this);
        logout.setOnClickListener(this);
        dataSetting.setOnClickListener(this);
        softwareSetting.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*添加小纸条*/
            case R.id.btn_addnote:
                Intent intentAdd = new Intent(NotesActivity.this,AddNoteActivity.class);
                startActivity(intentAdd);
                overridePendingTransition(R.anim.in_right,R.anim.in_left);
                break;
/*            case R.id.ib_note_avatar:
                Intent intentSet = new Intent(NotesActivity.this,AddNoteActivity.class);
                startActivity(intentSet);
                break;*/
            /*登出*/
            case R.id.btn_note_logout:
                logOut();
                break;
            /*用户资料设置*/
            case R.id.btn_notes_data_setting:
                Intent intentUserSet = new Intent(NotesActivity.this,UserDataSetActivity.class);
                startActivity(intentUserSet);

                break;
            /*小纸条软件设置*/
            case R.id.btn_notes_software_setting:
                AlertDialog.Builder softwareSettingDialog = new AlertDialog.Builder(NotesActivity.this);
                softwareSettingDialog.setMessage("[软件设置modular]计划5.15日完成，技术小哥正在熬夜写代码~")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }

    private void initViews(){

        mDrawerLayout = (DrawerLayout)findViewById(R.id.notes_drawer_layout);
        addNote = (Button)findViewById(R.id.btn_addnote);
        logout = (Button)findViewById(R.id.btn_note_logout);
        avatarButton = (ImageButton) findViewById(R.id.btn_notes_avatar);
        sideMenuAvatar = (ImageButton)findViewById(R.id.ib_note_rl_avatar);
        /*侧滑菜单*/
        dataSetting = (Button)findViewById(R.id.btn_notes_data_setting);
        softwareSetting = (Button)findViewById(R.id.btn_notes_software_setting);


        FragmentAdapter mAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        ViewPager mPager = findViewById(R.id.tab_pager);

        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mAdapter);

        ContainerView mTabLayout = findViewById(R.id.tab_container);
        mTabLayout.setOnPageChangeListener(this);

        mTabLayout.initContainer(getResources().getStringArray(R.array.tab_notes_title),ICONS_RES,/*TAB_COLORS,*/true);

        int width =  getResources().getDimensionPixelSize(R.dimen.tab_icon_width);
        int height = getResources().getDimensionPixelSize(R.dimen.tab_icon_height);
        mTabLayout.setContainerLayout(R.layout.tab_container_view,R.id.view_tab_icon,width,height);
        /**/
        /**/
        mTabLayout.setViewPager(mPager);
        mPager.setCurrentItem(getIntent().getIntExtra("tab",0));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int index = 0,len = fragments.length;index < len;index++){
            fragments[index].onHiddenChanged(index != position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    //将下载的图片显示在的ImageView中
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 100){
                Bitmap bitmap = (Bitmap)message.obj;
                avatarButton.setImageBitmap(bitmap);
            }
            return false;
        }
    });

    /*Bmob 相关*/

    private void LoginTest(){
        final MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        /*if(myUser == null){
            Intent intent = new Intent(NotesActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
        final String avatarUrl = (String) myUser.getObjectByKey("avatar");
/*        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    handler.obtainMessage(100,getImageFromNet(avatarUrl)).sendToTarget();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();*/
        /*显示 侧面菜单 和 主界面右上角 的头像*/
        if (myUser.getGander().equals("男")){
            sideMenuAvatar.setBackgroundResource(R.drawable.m0);
            avatarButton.setBackgroundResource(R.drawable.m0);
        }else if (myUser.getGander().equals("女")){
            sideMenuAvatar.setBackgroundResource(R.drawable.w0);
            avatarButton.setBackgroundResource(R.drawable.w0);
        }else if (myUser.getGander().equals("保密")){
            sideMenuAvatar.setBackgroundResource(R.drawable.s0);
            avatarButton.setBackgroundResource(R.drawable.s0);
        }else{
            sideMenuAvatar.setBackgroundColor(Color.WHITE);
            avatarButton.setBackgroundResource(Color.WHITE);
        }
        IMConnect();

    }

    private void IMConnect(){
        final MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        if (!TextUtils.isEmpty(myUser.getObjectId())
                && BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()){
            BmobIM.connect(myUser.getObjectId(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        EventBus.getDefault().post(new RefreshEvent());
                        BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(myUser.getObjectId(),
                                myUser.getNickname(),myUser.getAvatar()));
                        Toast.makeText(NotesActivity.this,"IM服务器连接成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(NotesActivity.this,"IM服务器连接error"+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }
            });
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus connectionStatus) {
                    Toast.makeText(NotesActivity.this,connectionStatus.getMsg(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    /*获取头像*/
    private Bitmap getImageFromNet(String Url)throws IOException{
        Bitmap bitmap = null;
        URL url = new URL(Url);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        if (conn.getResponseCode() == 200){
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            conn.disconnect();
            is.close();
        }
        return bitmap;
    }
    /*退出账号*/
    private void logOut(){
        //重复监听事件 造成了双击
                AlertDialog.Builder dialog = new AlertDialog.Builder(NotesActivity.this);
                dialog.setMessage("确定要退出当前账号吗")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BmobIM.getInstance().disConnect();
                                BmobUser.logOut();
                                Intent intent = new Intent(NotesActivity.this,LoginActivity.class);
                                startActivity(intent);
                                ActivityCollector.finishAll();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
            }

    @Override
    protected void onResume() {
        super.onResume();
        IMConnect();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();

        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);

        if (userInfo.getGander().equals("男")){
            sideMenuAvatar.setBackgroundResource(R.drawable.m0);
            avatarButton.setBackgroundResource(R.drawable.m0);
        }else if (userInfo.getGander().equals("女")){
            sideMenuAvatar.setBackgroundResource(R.drawable.w0);
            avatarButton.setBackgroundResource(R.drawable.w0);
        }else if (userInfo.getGander().equals("保密")){
            sideMenuAvatar.setBackgroundResource(R.drawable.s0);
            avatarButton.setBackgroundResource(R.drawable.s0);
        }else{
            sideMenuAvatar.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BmobIM.getInstance().clear();
    }
}