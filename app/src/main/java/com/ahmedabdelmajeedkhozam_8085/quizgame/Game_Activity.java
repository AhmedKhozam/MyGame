package com.ahmedabdelmajeedkhozam_8085.quizgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;


public class Game_Activity extends AppCompatActivity {

    static Random random = new Random();
    //views
    Button b, bb, bbb, bbbb, bbbbb, bbbbbb;
    String MsgEnd;
    TextView txtFalse, txtTrue;

    private int rnd, id, sizeData,  count_b, point = 3;



    private List<item> mDataList;



    private databaseClass mdata;
    private MediaPlayer media_false;
    private MediaPlayer media_true;
    private MediaPlayer song;
    private CheckBox checkBox_vol;
    Button check_music;

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.brown_700));
        }






        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //===================================================================================================================
        b = (Button) findViewById(R.id.Qust);
        bb = (Button) findViewById(R.id.ans1);
        bbb = (Button) findViewById(R.id.ans2);
        bbbb = (Button) findViewById(R.id.ans3);
        bbbbb = (Button) findViewById(R.id.ans4);
        bbbbbb = (Button) findViewById(R.id.TotalQust);





        //============================================Right - Wrong =======================================================================
        txtFalse = (TextView) findViewById(R.id.txtFalse);
        txtTrue = (TextView) findViewById(R.id.txtTrue);

        //===================================== Media ==============================================================================

        media_true = MediaPlayer.create(this, R.raw.sound_true);
        media_false = MediaPlayer.create(this, R.raw.sound_false);
        song = MediaPlayer.create(this, R.raw.music);

        //===================================================================================================================

        checkBox_vol = (CheckBox) findViewById(R.id.box_vol);
        check_music = (Button) findViewById(R.id.box_music);


        //===================================================================================================================

        mdata = new databaseClass(this);

        //===================================================================================================================

        ///////////////////////"جلب قيم من زر return  "//////
        Bundle b = getIntent().getExtras();
        boolean rtn = b.getBoolean("rtn");

        //===========================================DataBase========================================================================

        //// نسخ قاعدة البينات الى البرنامج

        File database = getApplicationContext().getDatabasePath(databaseClass.DBNAME);
        if (!database.exists()) {
            mdata.getReadableDatabase();
            //Copy db
            if (copyDatabase(this)) {
                Toast.makeText(this, "نسخ قاعدة البيانات", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "تعدر نسخ قاعدة البيانات", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //===================================================================================================================
        ////// جلب معلومات قاعدة الببانات الى الليست

        mDataList = mdata.getListProduct();
        sizeData = mDataList.size();
        if (rtn) {
            clear_savechange();
        } else {
            LoadSating();
        }
        if (mDataList.size() <= 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage
                    ("مبروووك  لقد أنهيت جميع المراحل بنجاح \n ا" +
                            "نتظر الاصدار القادم " +
                            "\n أوإلعب من جديد");

            builder.setPositiveButton("إعادة المحاولة", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    restarteGame();
                }
            });
            builder.setNegativeButton("أرسل التطبيق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();
            bbbbbb.setText(sizeData + "/" + sizeData);

        } else {
            table();
            bbbbbb.setText(txtTrue.getText().toString() + "/" + sizeData);

        }
    }


    public void table() {


        rnd = random.nextInt(mDataList.size());
        id = mDataList.get(rnd).ID;
        b.setText(mDataList.get(rnd).Question);
        bb.setText(mDataList.get(rnd).Answer_1);
        bbb.setText(mDataList.get(rnd).Answer_2);
        bbbb.setText(mDataList.get(rnd).Answer_3);
        bbbbb.setText(mDataList.get(rnd).Answer_4);
        count_b = mDataList.get(rnd).ID_answer;


    }

    private boolean copyDatabase(Context context) {

        try {
            InputStream inputStream = context.getAssets().open(databaseClass.DBNAME);
            String outFileName = databaseClass.myPath + databaseClass.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity", "DB copied");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void ans1(View view) {
        if (count_b == 1) {
            BtnTrue();
        } else {
            BtnFalse();
        }
    }

    public void ans2(View view) {
        if (count_b == 2) {
            BtnTrue();
        } else {
            BtnFalse();
        }
    }

    public void ans3(View view) {
        if (count_b == 3) {
            BtnTrue();
        } else {
            BtnFalse();
        }
    }

    public void ans4(View view) {
        if (count_b == 4) {
            BtnTrue();
        } else {
            BtnFalse();
        }
    }

    // ==================&&=================== إذا كان الجواب صحيح طبق هذه الدالة

    public void BtnTrue() {

        LayoutInflater inflater = getLayoutInflater();
        View Toastview = inflater.inflate(R.layout.my_true_toast, (ViewGroup) findViewById(R.id.cusum_layout));
        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 90);
        toast.setView(Toastview);
        toast.show();

        if (!checkBox_vol.isChecked()) {
            media_true.start(); //mute sound
        }


        int m = Integer.valueOf(txtTrue.getText().toString()) + 1;
        txtTrue.setText("" + m);
        bbbbbb.setText(m + "/" + sizeData);

        if (mDataList.size() <= 1) {
            finalMessage();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(MsgEnd +
                    "عدد الاجوبة الصحيحة: " + txtTrue.getText().toString() + "\n" +
                    "عدد الأجوبة الخاطئة: " + txtFalse.getText().toString());
            builder.setPositiveButton("إعادة المحاولة", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    restarteGame();
                }
            });
            builder.setNegativeButton("أرسل التطبيق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.show();

        } else {
            SaveSating(); ////"حفظ التغييرات ////////
            mDataList.remove(rnd);
            table();
        }
    }

    // ==================&&===================  إإذا كان الجواب خطأ طبق هذه الدالة


    public void BtnFalse() {
        LayoutInflater inflater = getLayoutInflater();
        View Toastview2 = inflater.inflate(R.layout.my_wrong_toast, (ViewGroup) findViewById(R.id.cutoum_layout2));
        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 90);
        toast.setView(Toastview2);
        toast.show();

        if (!checkBox_vol.isChecked()) {
            media_false.start();
        }
        int m = Integer.valueOf(txtFalse.getText().toString()) + 1;
        txtFalse.setText("" + m);

        if (mDataList.size() <= 1) {
            finalMessage();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("الجوب الصحيح هو: " + count_b + "\n" + MsgEnd +
                    "عدد الاجوبة الصحيحة: " + txtTrue.getText().toString() + "\n" +
                    "عدد الأجوبة الخاطئة: " + txtFalse.getText().toString());
            builder.setPositiveButton("إعادة المحاولة", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    restarteGame();
                }
            });
            builder.setNegativeButton("أرسل التطبيق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();

        } else {
            id = -1; /////// هذا الرقم لكي لا يتم تخزين قيمة موجودة لان الجواب خطأ ////////
            SaveSating(); ////"حفظ التغييرات ////////
            table();
        }

    }

    // دالة الرجوع للبداية بعد انتهاء المراحل          ==================&&===================

    public void restarteGame() {
        Intent mainactivity = new Intent(this, MainActivity.class);
        startActivity(mainactivity);
    }

    // الرسالة التي تظهر بعض انتهاء المراحل          ==================&&===================

    public void finalMessage() {
        int txttrue = Integer.valueOf(txtTrue.getText().toString());
        int txtfalse = Integer.valueOf(txtFalse.getText().toString());

        if (txttrue > txtfalse) {
            MsgEnd = "رائع لقد أنهيت المراحل بشكل جيد. \n ";
        }
        if (txttrue <= txtfalse) {
            MsgEnd = "كنت ضعيف في الإجابة.\n ";
        }
    }



    /// حفظ التغييرات بالبرنامج           ==================&&===================
    public void SaveSating() {
        SharedPreferences savechange = this.getSharedPreferences("savechange", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savechange.edit();

        editor.putString("txtTrue", txtTrue.getText().toString());
        editor.putString("txtFalse", txtFalse.getText().toString());
        editor.putString("bbbbbb", bbbbbb.getText().toString());

        editor.putInt("Point", point);
        editor.putInt("list" + id, id);
        editor.apply();
    }

    // جلب التغييرات السابقة للبرنامج     ==================&&===================

    public void LoadSating() {
        SharedPreferences savechange = this.getSharedPreferences("savechange", Context.MODE_PRIVATE);

        String txttrue = savechange.getString("txtTrue", "0");
        txtTrue.setText(txttrue);
        String txtfalse = savechange.getString("txtFalse", "0");

        txtFalse.setText(txtfalse);
        String butn6 = savechange.getString("bbbbbb", sizeData + "/" + sizeData);
        bbbbbb.setText(butn6);

        int Point = savechange.getInt("Point", point);
        this.point = Point;

        int i = 0;
        int data = mDataList.size();
        while (i < data) {
            try {
                int ii = 0;
                while (ii < data) {
                    int x = mDataList.get(ii).ID;
                    int listvale = savechange.getInt("list" + x, -1);
                    if (listvale == x) {
                        mDataList.remove(ii);
                    }
                    ii++;
                }
            } catch (Exception e) {
            }
            i++;
        }
    }


    public void clear_savechange() {
        SharedPreferences savechange = this.getSharedPreferences("savechange", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savechange.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // if (mInterstitialAd.isLoaded()) {
         //   mInterstitialAd.show();
            song.pause();
        //}

    }

    @Override
    protected void onResume() {
        super.onResume();
        table();

/////////////////////// استرجاع النقط المضافة /////////////////////

        SharedPreferences savechange = this.getSharedPreferences("savechange", Context.MODE_PRIVATE);
        int Point = savechange.getInt("Point", point);
        this.point = Point;

    }


    public void music(View view) {
        try {

            if (song.isPlaying()) {
                song.pause();
                    check_music.setBackgroundResource(R.drawable.music_icon);
                song.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();

                    }
                });
                song.prepareAsync();

            } else {
                song.start();
                song.setLooping(true);
                check_music.setBackgroundResource(R.drawable.music_icon);

            }


        }catch (Exception e){

        }



    }



}