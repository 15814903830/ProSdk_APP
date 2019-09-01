package com.unisound.media.example;

import android.graphics.Color;
import android.media.AudioFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.unisound.watchassist.R;
import com.unisound.sdk.asr.AsrEvent;
import com.unisound.sdk.asr.AsrOption;
import com.unisound.sdk.asr.UnisoundAsrEngine;
import com.unisound.sdk.asr.audiosource.AndroidRecordAudioSource;
import com.unisound.sdk.asr.audiosource.IAudioSource;
import com.unisound.sdk.asr.impl.IAsrResultListener;
import com.unisound.sdk.asr.param.UnisoundAsrInitMode;
import com.unisound.sdk.tts.TtsEvent;
import com.unisound.sdk.tts.TtsOption;
import com.unisound.sdk.tts.UnisoundTtsEngine;
import com.unisound.sdk.tts.audiotrack.AndroidAudioTrack;
import com.unisound.sdk.tts.impl.ITtsEventListener;
import com.unisound.sdk.tts.param.UnisoundTtsInitMode;
import com.unisound.sdk.tts.param.UnisoundTtsPlayMode;
import com.unisound.sdk.utils.AssetsUtils;
import com.unisound.sdk.utils.SdkLogMgr;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements IAsrResultListener {

  private static final String TAG = "MainActivityUnisoundUsc";

  private int[] color = { Color.RED, Color.GREEN };
  private int position = 0;
  private Button butSetWakeUpWord;
  private Button butStartAsr;
  private Button butStartWakeUp;
  private Button butEnd;
  private Button butStartTts;
  private Button butEndTts;
  private Button butGetToken;
  private Button butExit;
  private TextView txtNlu;
  private View viewBg;
  private TextView textResult;
  private UnisoundAsrEngine unisoundAsrEngine;
  private IAudioSource iAudioSource;
  private UnisoundTtsEngine unisoundTtsEngine;
  private AndroidAudioTrack androidAudioTrackOnline = new AndroidAudioTrack(16000);
  private AndroidAudioTrack androidAudioTrackOffline = new AndroidAudioTrack(22050);

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    butSetWakeUpWord = findViewById(R.id.butSetWakeUpWord);
    butStartAsr = findViewById(R.id.butStartAsr);
    butStartWakeUp = findViewById(R.id.butStartWakeuUp);
    butEnd = findViewById(R.id.butEnd);
    butStartTts = findViewById(R.id.butStartTts);
    butEndTts = findViewById(R.id.butEndTts);
    textResult = findViewById(R.id.txtResult);
    butGetToken = findViewById(R.id.butGetToken);
    butExit = findViewById(R.id.butExit);
    txtNlu = findViewById(R.id.txtNlu);
    viewBg = findViewById(R.id.viewBg);
    iAudioSource = new AndroidRecordAudioSource(AudioFormat.CHANNEL_IN_MONO);
    //iAudioSource = new AecAudioSource(new AndroidRecordAudioSource(TtsAudioFormat.CHANNEL_IN_STEREO));
    //iAudioSource = new AecAudioSource(new FileAudioSource("/sdcard/unisound/aecTest.pcm", 2));
    initTTS();
    unisoundAsrEngine =
        new UnisoundAsrEngine(this,  iAudioSource, true);
    unisoundAsrEngine.setListener(this);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_INIT_MODE, UnisoundAsrInitMode.MIX);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_SERVER_VAD_ENABLE, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_FILTER_NAME, "search");
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_NLU_ENABLE, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_LOCAL_VAD_ENABLE, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_RESULT_WITH_TYPE, false);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_PRINT_DEBUG_LOG, false);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_DEVICE_UDID,
        "59fc0f0019ff4ad8b4f997a55a3c27b3222");
    //unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_DEVICE_UDID, UUID.randomUUID().toString());
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_SAVE_RECORD, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_ASR_DOMAIN, "kar");
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_NLU_SCENARIO, "child");
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_DEVICE_ACTIVE, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_CALLBACK_VOLUME, false);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_PRINT_DEBUG_LOG, false);
    unisoundAsrEngine.init();

    butSetWakeUpWord.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        HashSet<String> allWakeUpWords = new HashSet<>();
        allWakeUpWords.add("你好魔方");
        for (int y = 0; y < 100; y++) {
          allWakeUpWords.add("测试唤醒词" + y);
        }
        unisoundAsrEngine.setWakeUpWord(allWakeUpWords, allWakeUpWords);
      }
    });
    HashSet<String> wakeUpWords = unisoundAsrEngine.getWakeUpWord();
    SdkLogMgr.d("wakeUpWords:" + Arrays.toString(wakeUpWords.toArray()));
    butStartWakeUp.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        unisoundAsrEngine.startWakeUp();
      }
    });
    butStartAsr.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        unisoundAsrEngine.startAsr(false);
      }
    });
    butEnd.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        unisoundAsrEngine.cancel();
      }
    });

    butStartTts.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        unisoundTtsEngine.setTtsOption(TtsOption.TTS_PLAY_MODE, UnisoundTtsPlayMode.ONLINE);
        unisoundTtsEngine.setTtsOption(TtsOption.TTS_SAMPLE_RATE, 16000);
        unisoundTtsEngine.playTts("123456789");
      }
    });

    butEndTts.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        unisoundTtsEngine.cancelTts();
      }
    });

    butGetToken.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        String token = (String) unisoundAsrEngine.getOption(AsrOption.ASR_OPTION_DEVICE_TOKEN);
        textResult.setText(token);
      }
    });

    butExit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        unisoundAsrEngine.exitMultiRoundDialogue();
      }
    });
  }

  @Override protected void onDestroy() {
    if (unisoundAsrEngine != null) {
      unisoundAsrEngine.release();
      unisoundAsrEngine = null;
    }
    if (unisoundTtsEngine != null) {
      unisoundTtsEngine.release();
      unisoundTtsEngine = null;
    }
    super.onDestroy();
  }

  @Override public void onResult(int event, String result) {
    Log.d(TAG, "onResult:" + result);
    if (event == AsrEvent.ASR_EVENT_WAKEUP_RESULT) {
      int doaResult = (int) unisoundAsrEngine.getOption(AsrOption.ASR_OPTION_DOA_RESULT);
      SdkLogMgr.d(TAG, "doaResult:" + doaResult);
      viewBg.setBackgroundColor(color[position % color.length]);
      textResult.setText(result);
      position++;
    }
    if (event == AsrEvent.ASR_EVENT_ASR_RESULT) {
      textResult.setText(result);
      if (result.contains("service")) {
        unisoundAsrEngine.startWakeUp();
        txtNlu.setText(result);
      }
    }
  }

  @Override public void onEvent(int event) {
    Log.d(TAG, "onEvent:" + event);
    if (event == AsrEvent.ASR_EVENT_VAD_SPEECH_END) {
      unisoundAsrEngine.stopAsr(false);
    }
  }

  @Override public void onError(int error) {
    Log.d(TAG, "onError:" + error);
  }

  private void initTTS() {
    unisoundTtsEngine =
        new UnisoundTtsEngine(this.getApplicationContext(), 
            androidAudioTrackOnline, false);
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_INIT_MODE, UnisoundTtsInitMode.MIX);
    if (!(new File(Config.TTS_PATH + Config.FRONTEND_MODEL).exists())) {
      AssetsUtils.copyAssetsFile(this, Config.FRONTEND_MODEL,
          Config.TTS_PATH + Config.FRONTEND_MODEL, false);
    }
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_FRONT_MODEL_PATH,
        Config.TTS_PATH + Config.FRONTEND_MODEL);
    if (!(new File(Config.TTS_PATH + Config.BACKEND_MODEL).exists())) {
      AssetsUtils.copyAssetsFile(this, Config.BACKEND_MODEL, Config.TTS_PATH + Config.BACKEND_MODEL,
          false);
    }
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_BACK_MODEL_PATH,
        Config.TTS_PATH + Config.BACKEND_MODEL);
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_SPEED, 70);
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_END_DELAY, 0);
    unisoundTtsEngine.setListener(new ITtsEventListener() {
      @Override public void onEvent(int event) {
        if (event == TtsEvent.TTS_EVENT_PLAY_END) {

        }
      }

      @Override public void onError(int error) {

      }
    });
    unisoundTtsEngine.init();
  }
}
