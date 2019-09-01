package com.unisound.media.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.unisound.watchassist.R;
import com.unisound.sdk.asr.AsrEvent;
import com.unisound.sdk.asr.AsrOption;
import com.unisound.sdk.asr.UnisoundAsrEngine;
import com.unisound.sdk.asr.impl.IAsrResultListener;
import com.unisound.sdk.utils.SdkLogMgr;
import java.util.HashSet;

public class WakeupActivity extends AppCompatActivity implements IAsrResultListener {
  private static final String TAG = "WakeupActivity";

  private int[] color = { Color.RED, Color.GREEN };
  private int position = 0;
  private TextView txtNlu;
  private TextView txtNluLists;
  private View viewBg;
  private EditText etWakeup;

  private UnisoundAsrEngine unisoundAsrEngine;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_wakeup);
    initView();
    unisoundAsrEngine = VoicePresenter.getInstance().getUnisoundAsrEngine();
    VoicePresenter.getInstance().setAsrListener(this);
    onSetWakeUpWord();
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
  }

  private void initView() {
    txtNlu = findViewById(R.id.txtNlu);
    txtNluLists = findViewById(R.id.tv_wakeup_list);
    viewBg = findViewById(R.id.viewBg);
    etWakeup = findViewById(R.id.et_wakeup);
  }

  public void onSetWakeUpWord() {
    HashSet<String> mainWakeUpWord = new HashSet<>();
    mainWakeUpWord.add("你好魔方");
    unisoundAsrEngine.setWakeUpWord(mainWakeUpWord, mainWakeUpWord);
    refreshWakeUpWord();
  }

  public void onStartWakeUp(View view) {
    onAddWakeUpWord();
    softInputMethod(false);
    if (!unisoundAsrEngine.startWakeUp()) {
      Toast.makeText(this, "唤醒失败", Toast.LENGTH_SHORT).show();
    }
  }

  public void onAddWakeUpWord() {
    if (!TextUtils.isEmpty(etWakeup.getText())) {
      HashSet<String> mainWakeUpWord = new HashSet<>();
      mainWakeUpWord.add(etWakeup.getText().toString());
      mainWakeUpWord.addAll(unisoundAsrEngine.getWakeUpWord());
      etWakeup.getText().clear();
      unisoundAsrEngine.setWakeUpWord(mainWakeUpWord, mainWakeUpWord);
      refreshWakeUpWord();
    }
  }

  private void refreshWakeUpWord() {
    HashSet<String> mainWakeUpWord = unisoundAsrEngine.getWakeUpWord();
    StringBuffer wakeUpBuffer = new StringBuffer();
    wakeUpBuffer.append("唤醒词：\n");
    for (String wakeup : mainWakeUpWord) {
      wakeUpBuffer.append(wakeup).append("\n");
    }
    txtNluLists.setText(wakeUpBuffer.toString());
  }

  @Override public void onResult(int event, String result) {
    Log.d(TAG, "onResult:" + result);
    if (event == AsrEvent.ASR_EVENT_WAKEUP_RESULT) {
      int doaResult = (int) VoicePresenter.getInstance()
          .getUnisoundAsrEngine()
          .getOption(AsrOption.ASR_OPTION_DOA_RESULT);
      SdkLogMgr.d(TAG, "doaResult:" + doaResult);
      viewBg.setBackgroundColor(color[position % color.length]);
      txtNlu.setText(result);
      position++;
    }
  }

  @Override public void onEvent(int event) {
    Log.d(TAG, "onEvent:" + event);
  }

  @Override public void onError(int error) {
    Log.d(TAG, "onError:" + error);
  }

  public void softInputMethod(boolean show) {
    try {
      InputMethodManager inputMethodManager =
          ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
      if (show) {
        inputMethodManager.showSoftInput(null, InputMethodManager.SHOW_FORCED);
      } else {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
          inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
              InputMethodManager.HIDE_NOT_ALWAYS);
        }
      }
    } catch (Exception e) {

    }
  }
}