package com.ahmedabdelmajeedkhozam_8085.quizgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.brown_700));
        }


        ImageView imageView = (ImageView)findViewById(R.id.imageView2);



        Glide.with(this)
                .load(R.drawable.background2).into(imageView);
    }

    public void start(View view) {
        player = MediaPlayer.create(this,R.raw.sound_click2);
        player.start();

        Intent windows_asila = new Intent(this, Game_Activity.class);
        windows_asila.putExtra("rtn",false);
        startActivity(windows_asila);
    }

    public void restart(View view) {
        player= MediaPlayer.create(this,R.raw.sound_click2);
        player.start();

        Intent windows_asila = new Intent(this, Game_Activity.class);
        windows_asila.putExtra("rtn",true);
        startActivity(windows_asila);
    }





    public void about(View view) {
        player = MediaPlayer.create(this,R.raw.sound_click2);
        player.start();

        AlertDialog.Builder builder=new AlertDialog.Builder(this) ;
        builder.setTitle("حول البرنامج");
        builder.setMessage("إصدار البرنامج v1.0   \n  التطبيق معاد تصميمه , وبرمجته بالكامل من قبل احمد عبدالمجيد سالم خزام     \n " +
                "للإقتراحات والتواصل  عن طريق البريد الالكتروني: \n " +
                "ahmed_khozam@yahoo.com");
        builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).setIcon(R.drawable.developer);
        builder.show();
    }


}
