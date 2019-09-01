package com.unisound.media.example.musice;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.unisound.media.example.base.MusiceBase;
import com.unisound.media.example.okhttp.AudioUtil;
import com.unisound.watchassist.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musice);
        intiview();
        inttvolume();//音量
        getdatejson();//解析json数据,播放第一首歌曲


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
                }
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

                }
            }
        });

        lladd.setOnClickListener(new View.OnClickListener() {//音量++
            @Override
            public void onClick(View v) {
                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (maxVolume>currentVolume)
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
                if (0<currentVolume)
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
                    startforpause = false;
                    iv_start_pause.setSelected(true);
                    mediaPlayer.pause();
                } else {//继续播放
                    mediaPlayer.start();
                    startforpause = true;
                    iv_start_pause.setSelected(false);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
    }


    /**
     * 计时器
     * */

    /**
     * 解析json数据,播放第一首歌曲
     * */
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
     * */
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
     * */
    private void playmusice(int i) {
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
