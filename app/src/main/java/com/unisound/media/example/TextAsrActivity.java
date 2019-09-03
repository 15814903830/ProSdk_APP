package com.unisound.media.example;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.unisound.media.example.listvoice.ListTextAsrActivity;
import com.unisound.media.example.musice.MusiceActivity;
import com.unisound.media.example.okhttp.HttpCallBack;
import com.unisound.media.example.okhttp.OkHttpUtils;
import com.unisound.media.example.okhttp.ToolsCode;
import com.unisound.sdk.tts.TtsOption;
import com.unisound.sdk.tts.UnisoundTtsEngine;
import com.unisound.sdk.tts.audiotrack.AndroidAudioTrack;
import com.unisound.sdk.tts.param.UnisoundTtsPlayMode;
import com.unisound.sdk.utils.AssetsUtils;
import com.unisound.watchassist.R;
import com.unisound.sdk.asr.AsrEvent;
import com.unisound.sdk.asr.AsrOption;
import com.unisound.sdk.asr.UnisoundAsrEngine;
import com.unisound.sdk.asr.impl.IAsrResultListener;
import com.unisound.sdk.asr.param.UnisoundAsrInitMode;
import com.unisound.sdk.asr.param.UnisoundAsrMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.unisound.media.example.timeutils.CountDownTimerService.countDownTimerService;

public class TextAsrActivity extends BasicMainActivity implements IAsrResultListener, HttpCallBack {
  private LinearLayout llFrontModel;
  private LinearLayout llBackModel;
  private LinearLayout llVoiceName;

  private TextView tvPlayModel;
  private TextView tvFrontModel;
  private TextView tvBackModel;
  private TextView tvVoiceName;

  private SeekBar sbSpeed;
  private SeekBar sbVolume;
  private SeekBar sbBright;

  private EditText etBackSil;
  private EditText etFrontSil;
  private EditText etDefaultName;
  private EditText etSampleRate;
  private ImageView returntext;
   private Context context;

  private HttpCallBack mHttpCallBack;
  private static final String TAG = "TextAsrActivity";
  private UnisoundAsrEngine unisoundAsrEngine;
  private UnisoundTtsEngine unisoundTtsEngine;
  private TextView textResult;
  private int playModeChoice = 0;
  private boolean onetext=false;
  private ImageView butStartAsr;
  private List<String> frontModel = new ArrayList<>();
  private List<String> backModel = new ArrayList<>();
  private boolean aBooleanstartforcancel=true;
  private String url="http://106.14.226.237:8080/service/iss-test?platform=&screen=&text=" ;
  private String url2= "&appkey=3dcddlnx7ddlb2xatjxtbtxha6xah7iogajzkqie&scenario=child&filterName=search&ver=3.0&udid=d1cee3ae6ff46&returnType=json&appsig=A6702357B7904B43907E7803665FBD5FE08C57A5&appver=1.0.1&city=%E6%B7%B1%E5%9C%B3&history=&time=&voiceid=8AAAD808EDF04697AF3C74C22DC4CF0E&gps=&method=iss.getTalk&dpi=&viewId=";
  private MediaPlayer mediaPlayer;
    private ObjectAnimator mObjectAnimator,mObjectAnimator2,mObjectAnimator3;
    private AnimatorSet animationSet;

  @SuppressLint("WrongConstant")
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_text_asr);
    initView();
    context=this;
    mHttpCallBack=this;
    textResult = findViewById(R.id.textResult);
    returntext=findViewById(R.id.iv_return);
      butStartAsr=findViewById(R.id.butStartAsr);

    returntext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(context, ListTextAsrActivity.class));
      }
    });


      mObjectAnimator = ObjectAnimator.ofFloat(butStartAsr, "scaleY", 1f,0.8f, 0.7f,1f);
    mObjectAnimator.setRepeatCount(500);
    mObjectAnimator.setRepeatMode(ValueAnimator.INFINITE);

      mObjectAnimator2 = ObjectAnimator.ofFloat(butStartAsr, "scaleX", 1f,0.8f, 0.7f,1f);
    mObjectAnimator2.setRepeatCount(500);
    mObjectAnimator2.setRepeatMode(ValueAnimator.INFINITE);
      animationSet = new AnimatorSet();
      animationSet.setDuration(2000L);
      animationSet.playTogether(mObjectAnimator,mObjectAnimator2);
    animationSet.start();


    unisoundAsrEngine = VoicePresenter.getInstance().getUnisoundAsrEngine();
    VoicePresenter.getInstance().setAsrListener(this);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_SERVER_VAD_ENABLE, false);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_NLU_ENABLE, false);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_INIT_MODE, UnisoundAsrInitMode.ONLINE);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_USE_PUNCTUATED, true);
    unisoundTtsEngine = VoicePresenter.getInstance().getUnisoundTtsEngine();
  }

  private void initView() {
    llVoiceName = findViewById(R.id.ll_voice_name);
    llFrontModel = findViewById(R.id.ll_front_model);
    llBackModel = findViewById(R.id.ll_back_model);
    tvPlayModel = findViewById(R.id.tv_play_mode);
    tvFrontModel = findViewById(R.id.tv_front_model);
    tvBackModel = findViewById(R.id.tv_back_model);
    tvVoiceName = findViewById(R.id.tv_voice_name);
    sbSpeed = findViewById(R.id.sb_speed);
    sbVolume = findViewById(R.id.sb_volume);
    sbBright = findViewById(R.id.sb_bright);
    etFrontSil = findViewById(R.id.et_front_sil);
    etBackSil = findViewById(R.id.et_back_sil);
    etDefaultName = findViewById(R.id.et_default_text);
    etSampleRate = findViewById(R.id.et_sample_rate);
    copyAssetsModel();
    llFrontModel.setVisibility(View.GONE);
    llBackModel.setVisibility(View.GONE);
    if (frontModel.size() > 0) {
      tvFrontModel.setText(frontModel.get(0));
    }
    if (backModel.size() > 1) {
      tvBackModel.setText(backModel.get(1));
    }
  }



  public void onStartAsr(View v) {
    if (aBooleanstartforcancel){//第一次清空文本
      animationSet.end();
      textResult.setText("");
      unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_ASR_MODE, UnisoundAsrMode.ONLINE);
      unisoundAsrEngine.startAsr(false);
      aBooleanstartforcancel=false;
    }else {
      animationSet.start();
      unisoundAsrEngine.cancel();
      aBooleanstartforcancel=true;
    }

  }

  public void onExit(View v) {
    unisoundAsrEngine.exitMultiRoundDialogue();
  }

  @Override public void onResult(int event, String result) {
    Log.d(TAG, "onResult: "+result);
    if (event == AsrEvent.ASR_EVENT_ASR_RESULT) {
      try {
        JSONObject jsonObject = new JSONObject(result);
        String text = jsonObject.getString("asr_recongize");
        textResult.setText(text);
      } catch (Exception e) {
      }
    }


  }

  @Override public void onEvent(int event) {
    Log.d(TAG, "onEvent:" + event);
    //3028结束
    //结束回调播放语音
    if (onetext){
    if (event==3208){
      if (textResult.getText().toString().equals("")){
        //输入为空
      }else {
        getsemantic();
      }
    }
    }
    onetext=true;
  }

  @Override public void onError(int error) {
    Log.d(TAG, "onError:" + error);
  }

  protected void onDestroy() {
    VoicePresenter.getInstance().release();
    super.onDestroy();
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_NLU_ENABLE, true);
    unisoundAsrEngine.cancel();
    animationSet.clone();
  }

  public void startTts() {
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_PRINT_DEBUG_LOG, true);
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_PLAY_MODE,
            playModeChoice == 0 ? UnisoundTtsPlayMode.ONLINE : UnisoundTtsPlayMode.OFFLINE);
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_SPEED, sbSpeed.getProgress());
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_VOLUME, sbVolume.getProgress());
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_BRIGHT, sbBright.getProgress());
    if (etSampleRate.getText() != null && etSampleRate.getText().length() > 0) {
      String sample = etSampleRate.getText().toString();
      unisoundTtsEngine.setTtsOption(TtsOption.TTS_SAMPLE_RATE, Integer.parseInt(sample));
      unisoundTtsEngine.setAudioTrack(new AndroidAudioTrack(Integer.parseInt(sample)));
    }
    if (etFrontSil.getText() != null && etFrontSil.getText().length() > 0) {
      unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_FRONT_SIL,
              Integer.parseInt(etFrontSil.getText().toString()));
    }
    if (etBackSil.getText() != null && etBackSil.getText().length() > 0) {
      unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_BACK_SIL,
              Integer.parseInt(etBackSil.getText().toString()));
    }
    unisoundTtsEngine.playTts(textResult.getText().toString());
  }

  private void copyAssetsModel() {
    AssetManager assetManager = this.getAssets();
    try {
      String[] files = assetManager.list("");
      for (String file : files) {
        if (file.startsWith("frontend")) {
          frontModel.add(file);
          if (!(new File(Config.TTS_PATH + file).exists())) {
            AssetsUtils.copyAssetsFile(this, file, Config.TTS_PATH + file, false);
          }
        } else if (file.startsWith("backend")) {
          backModel.add(file);
          if (!(new File(Config.TTS_PATH + file).exists())) {
            AssetsUtils.copyAssetsFile(this, file, Config.TTS_PATH + file, false);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onResponse(String response, int requestId) {
    Message message = mHandler.obtainMessage();
    message.what = requestId;
    message.obj = response;
    mHandler.sendMessage(message);
  }

  @Override
  public void onHandlerMessageCallback(String response, int requestId) {
    try {
        Log.d(TAG, response );
      JSONObject  jsonObject = new JSONObject(response);
      ToolsCode.CODE=jsonObject.getString("service");
      switch (ToolsCode.CODE){
        case "cn.yunzhisheng.greeting":
          JSONObject  ANSWER = new JSONObject(jsonObject.getString("general"));
          textResult.setText(ANSWER.getString("text"));
          if (!ANSWER.optString("audio").equals("")){
            try {
              mediaPlayer = new MediaPlayer();
              mediaPlayer.setDataSource(ANSWER.optString("audio"));
              mediaPlayer.prepare();
              mediaPlayer.start();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }else {
            startTts();
          }
          break;
        case "cn.yunzhisheng.chat"://对答
          JSONObject  SETTING_EXEC = new JSONObject(jsonObject.getString("general"));
          textResult.setText(SETTING_EXEC.getString("text"));
          if (!SETTING_EXEC.optString("audio").equals("")){
            try {
              mediaPlayer = new MediaPlayer();
              mediaPlayer.setDataSource(SETTING_EXEC.optString("audio"));
              mediaPlayer.prepare();
              mediaPlayer.start();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }else {
            startTts();
          }
          break;
        case "cn.yunzhisheng.music":// SEARCH_SONG  搜索歌曲
          Intent intent=new Intent(this,MusiceActivity.class);
          intent.putExtra("response",response);
          startActivity(intent);
          break;
        case "cn.yunzhisheng.audio":// SEARCH_ALBUM  专辑
          Intent intent2=new Intent(this,MusiceActivity.class);
          intent2.putExtra("response",response);
          startActivity(intent2);
          break;
        case "cn.yunzhisheng.poem":// SEARCH  诗歌
          Intent intent3=new Intent(this,MusiceActivity.class);
          intent3.putExtra("response",response);
          startActivity(intent3);
          break;

      }


    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @SuppressLint("HandlerLeak")
  private Handler mHandler =  new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      int requestId = msg.what;
      String response = (String) msg.obj;
      onHandlerMessageCallback(response, requestId);
    }
  };

  private void getsemantic() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        OkHttpUtils.doGet(url+textResult.getText().toString()+url2,mHttpCallBack,0);
        Log.e("codeurl",url+textResult+url2);
      }
    }).start();
  }

}