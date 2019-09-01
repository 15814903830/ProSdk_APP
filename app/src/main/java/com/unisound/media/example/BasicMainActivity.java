package com.unisound.media.example;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.unisound.watchassist.R;
import com.unisound.sdk.asr.AsrEvent;
import com.unisound.sdk.asr.AsrOption;
import com.unisound.sdk.asr.impl.IAsrResultListener;
import com.unisound.sdk.utils.AssetsUtils;
import java.io.File;
import java.io.IOException;

public class BasicMainActivity extends AppCompatActivity implements IAsrResultListener {

  private static final String TAG = "MainActivityUnisoundUsc";

  private Button butWakeUp;
  private Button btnMedicalAsr;
  private Button btnOnlineAsr;
  private Button btnOfflineAsr;
  private Button btnOfflineSlotAsr;
  private Button butOnlineOneShotAsr;
  private Button butStartTts;
  private Button butGetToken;
  private Button butVoiceToText;
  private Button butExit;
  private TextView txtNlu;
  private Context context;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this;
    setContentView(R.layout.layout_basicmain);
    butWakeUp = findViewById(R.id.butWakeUpWord);
    btnMedicalAsr = findViewById(R.id.butMedicalAsr);
    btnOnlineAsr = findViewById(R.id.butOnlineAsr);
    btnOfflineAsr = findViewById(R.id.butOfflineAsr);
    btnOfflineSlotAsr = findViewById(R.id.butOfflineSlotAsr);
    butOnlineOneShotAsr = findViewById(R.id.butOnlineOneShotAsr);
    butVoiceToText = findViewById(R.id.butVoiceToText);
    butStartTts = findViewById(R.id.butStartTts);
    butGetToken = findViewById(R.id.butGetToken);
    butExit = findViewById(R.id.butExit);
    txtNlu = findViewById(R.id.txtNlu);

    butWakeUp.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(context, WakeupActivity.class));
      }
    });
    btnMedicalAsr.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(context, MedicalAsrActivity.class));
      }
    });
    btnOnlineAsr.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(context, OnlineAsrActivity.class));
      }
    });
    btnOfflineAsr.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(context, OfflineAsrActivity.class));
      }
    });

    butStartTts.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(context, TtsActivity.class));
      }
    });

    btnOfflineSlotAsr.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(context, OfflineSlotAsrActivity.class));
      }
    });

    butOnlineOneShotAsr.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(context, OneShotAsrActivity.class));
      }
    });

    butVoiceToText.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(context, TextAsrActivity.class));
      }
    });

    butGetToken.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        String token = (String) VoicePresenter.getInstance()
            .getUnisoundAsrEngine()
            .getOption(AsrOption.ASR_OPTION_DEVICE_TOKEN);
        txtNlu.setText(token);
      }
    });
    VoicePresenter.getInstance().init(this.getApplicationContext());
    copyTtsModel();
  }

  @Override protected void onDestroy() {
    VoicePresenter.getInstance().release();
    super.onDestroy();
  }

  @Override public void onResult(int event, String result) {
    Log.d(TAG, "onResult:" + result);
    if (event == AsrEvent.ASR_EVENT_ASR_RESULT) {
      if (result.contains("service")) {
        VoicePresenter.getInstance().getUnisoundAsrEngine().startWakeUp();
        txtNlu.setText(result);
      }
    }
  }

  @Override public void onEvent(int event) {
    Log.d(TAG, "onEvent:" + event);
    if (event == AsrEvent.ASR_EVENT_VAD_SPEECH_END) {
      VoicePresenter.getInstance().getUnisoundAsrEngine().stopAsr(false);
    }
  }

  @Override public void onError(int error) {
    Log.d(TAG, "onError:" + error);
  }

  private void copyTtsModel() {
    new Thread(new Runnable() {
      @Override public void run() {
        AssetManager assetManager = context.getAssets();
        try {
          String[] files = assetManager.list("");
          for (String file : files) {
            if (file.startsWith("frontend") || file.startsWith("backend")) {
              if (!(new File(Config.TTS_PATH + file).exists())) {
                AssetsUtils.copyAssetsFile(context, file, Config.TTS_PATH + file, false);
              }
            } else {
              AssetsUtils.copyAssetsFile(context, file,
                  Environment.getExternalStorageDirectory() + File.separator + "unisound"
                      + File.separator + file, false);
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }
}
