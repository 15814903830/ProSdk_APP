package com.unisound.media.example.musice;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unisound.media.example.base.MusiceBase;
import com.unisound.media.example.okhttp.AudioUtil;
import com.unisound.media.example.timeutils.CountDownTimerListener;
import com.unisound.media.example.timeutils.CountDownTimerService;
import com.unisound.watchassist.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.unisound.media.example.timeutils.CountDownTimerService.countDownTimerService;


public class MusiceActivity extends AppCompatActivity {
    TextView textView, tvname;
    int[] imageId = new int[]{R.drawable.volume0, R.drawable.volume1, R.drawable.volume2, R.drawable.volume3,
            R.drawable.volume4, R.drawable.volume5, R.drawable.volume6};
    private LinearLayout lladd;
    private LinearLayout llsubtract;
    private ImageView ivvolume, iv_start_pause, ivshan, ivxia;
    private int image = 0;
    private boolean startforpause = true;
    private MediaPlayer mediaPlayer;
    private int muscie = 0;
    private ProgressBar pb_jdt;
    List<MusiceBase> list = new ArrayList<>();
    int maxVolume;
    int currentVolume;
    private AudioManager mAudioManager;
    private ImageView iv_musicimg;
    private ImageView iv_return;
    private ObjectAnimator objectAnimatorRotate;

    private long timer_unit = 1000;
    private  int playtime=0;
    private long service_distination_total;
    private CountDownTimerService countDownTimerService;
    private int handtime;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    handtime= playtime-(int) (countDownTimerService.getCountingTime())/1000;
                    Log.e("time",""+handtime);
                    pb_jdt.setProgress(handtime);
                    break;
                case 1:
                    countDownTimerService.pauseCountDown();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musice);
        intiview();
        inttvolume();//音量
        getdatejson();//解析json数据,播放第一首歌曲

    }
    private class MyCountDownLisener implements CountDownTimerListener {

        @Override
        public void onChange() {
            if (startforpause){
                mHandler.sendEmptyMessage(2);
            }
        }
    }

    private void intiview() {
        textView = findViewById(R.id.textsss);
        lladd = findViewById(R.id.ll_add);
        llsubtract = findViewById(R.id.ll_subtract);
        ivvolume = findViewById(R.id.iv_volume);
        iv_start_pause = findViewById(R.id.iv_start_pause);
        ivshan = findViewById(R.id.iv_shan);
        ivxia = findViewById(R.id.iv_xia);
        tvname = findViewById(R.id.tv_name);
        pb_jdt = findViewById(R.id.pb_jdt);
        iv_musicimg = findViewById(R.id.iv_music);
        iv_return=findViewById(R.id.iv_return);
        objectAnimatorRotate = ObjectAnimator.ofFloat(iv_musicimg, "rotation", 0f, 360f,  720f, 1080f,1440f,1800f,2160f,2520f,2880f,3240f);
        objectAnimatorRotate.setInterpolator(new AccelerateInterpolator());//动画插值
        objectAnimatorRotate.setDuration(50000);//动画时间
        objectAnimatorRotate.setRepeatCount(Animation.INFINITE);
        objectAnimatorRotate.start();
        //下一曲
        ivxia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > muscie + 1) {
                    if (mediaPlayer != null)
                        mediaPlayer.release();
                    playmusice(++muscie);
                    textView.setText(list.get(muscie).getDisplayName());
                    tvname.setText(list.get(muscie).getArtist());
                    countDownTimerService.stopCountDown();

                    startforpause = true;
                    iv_start_pause.setSelected(false);
                }
                handtime=0;
            }

        });
        //上一曲
        ivshan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 < muscie) {
                    if (mediaPlayer != null)
                        mediaPlayer.release();
                    playmusice(--muscie);
                    textView.setText(list.get(muscie).getDisplayName());
                    tvname.setText(list.get(muscie).getArtist());
                    handtime=0;
                    countDownTimerService.stopCountDown();

                    startforpause = true;
                    iv_start_pause.setSelected(false);
                }
            }
        });

        lladd.setOnClickListener(new View.OnClickListener() {//音量++
            @Override
            public void onClick(View v) {
                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (maxVolume > currentVolume)
                    AudioUtil.soundUP(MusiceActivity.this);

                if (image < 6) {
                    image++;
                    ivvolume.setImageResource(imageId[image]);
                }

            }
        });

        llsubtract.setOnClickListener(new View.OnClickListener() {//音量--
            @Override
            public void onClick(View v) {
                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (0 < currentVolume)
                    AudioUtil.soundDOWN(MusiceActivity.this);
                if (image > 0) {
                    image--;
                    ivvolume.setImageResource(imageId[image]);
                }
            }
        });

        iv_start_pause.setOnClickListener(new View.OnClickListener() {//播放和暂停
            @Override
            public void onClick(View v) {
                if (startforpause) {//暂停
                    countDownTimerService.pauseCountDown();
                    startforpause = false;
                    iv_start_pause.setSelected(true);
                    mediaPlayer.pause();
                } else {//继续播放
                    countDownTimerService.startCountDown();
                    mediaPlayer.start();
                    startforpause = true;
                    iv_start_pause.setSelected(false);
                }

            }
        });
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();

        if (objectAnimatorRotate != null)
            objectAnimatorRotate.clone();
        objectAnimatorRotate = null;

    }


    /**
     * 计时器
     * */

    /**
     * 解析json数据,播放第一首歌曲
     */
    private void getdatejson() {
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("response"));
            String string = jsonObject.getString("data");
            JSONObject jsonObject2 = new JSONObject(string);
            String string2 = jsonObject2.getString("result");
            JSONObject jsonObject3 = new JSONObject(string2);
            String string3 = jsonObject3.getString("audioList");

            JSONArray jsonArray = new JSONArray(string3);
            for (int i = 0; i <= jsonArray.length(); i++) {
                MusiceBase musiceBase = new MusiceBase();
                JSONObject Stringlistbase = jsonArray.getJSONObject(i);
                musiceBase.setDisplayName(Stringlistbase.optString("displayName").equals("") ? Stringlistbase.optString("title") : Stringlistbase.optString("displayName"));
                musiceBase.setArtist(Stringlistbase.optString("artist").equals("") ? Stringlistbase.optString("dataOriginName") : Stringlistbase.optString("artist"));
                musiceBase.setUrl(Stringlistbase.optString("url"));
                list.add(musiceBase);
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        tvname.setText(list.get(0).getArtist());
        if (list.size() != 0) {
            playmusice(muscie);
        }
    }

    /**
     * 音量
     */
    private void inttvolume() {
        //获取系统的Audio管理者
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }


    /**
     * 播放音乐
     */
    private void playmusice(int i) {
        MediaPlayer md = new MediaPlayer();
        try {
            md.setDataSource(list.get(i).getUrl());//获取音频时长
            md.prepare();
            service_distination_total=timer_unit*( md.getDuration()/1000);
            playtime=md.getDuration()/1000;
            pb_jdt.setMax(playtime);
            countDownTimerService=null;
            countDownTimerService = CountDownTimerService.getInstance(new MyCountDownLisener()
                    ,service_distination_total);
            countDownTimerService.startCountDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(list.get(i).getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (list.size() > muscie + 1) {
                        if (mediaPlayer != null)
                            mediaPlayer.release();
                        playmusice(++muscie);
                        textView.setText(list.get(muscie).getDisplayName());
                        tvname.setText(list.get(muscie).getArtist());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
