package com.unisound.media.example;

import android.content.Context;
import android.media.AudioFormat;
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
import com.unisound.sdk.utils.AssetsUtils;
import java.io.File;

public class VoicePresenter {

  private UnisoundAsrEngine unisoundAsrEngine;
  private IAudioSource iAudioSource;
  private UnisoundTtsEngine unisoundTtsEngine;
  private AndroidAudioTrack androidAudioTrackOnline = new AndroidAudioTrack(16000);
  private AndroidAudioTrack androidAudioTrackOffline = new AndroidAudioTrack(22050);
  private Context context;

  private static VoicePresenter voicePresenter = new VoicePresenter();

  public synchronized static VoicePresenter getInstance() {
    return voicePresenter;
  }

  private VoicePresenter() {
  }

  public void init(Context context) {
    this.context = context;
    initTTS();
    initAsr();
  }

  public void initAsr() {
    if (unisoundAsrEngine != null) {
      return;
    }
    iAudioSource = new AndroidRecordAudioSource(AudioFormat.CHANNEL_IN_MONO);
    //    iAudioSource = new AecAudioSource(new AndroidRecordAudioSource(AudioFormat.CHANNEL_IN_STEREO));
    //iAudioSource = new AecAudioSource(new FileAudioSource("/sdcard/unisound/aecTest.pcm", 2));
    unisoundAsrEngine =
        new UnisoundAsrEngine(context,  iAudioSource, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_INIT_MODE, UnisoundAsrInitMode.MIX);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_SERVER_VAD_ENABLE, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_FILTER_NAME, "search");
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_NLU_ENABLE, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_LOCAL_VAD_ENABLE, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_RESULT_WITH_TYPE, false);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_PRINT_DEBUG_LOG, false);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_DEVICE_UDID,
        "59fc0f0019ff4ad8b4f997a55a3c27b3222");
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_SAVE_RECORD, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_ASR_DOMAIN, "kar");
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_NLU_SCENARIO, "child");
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_DEVICE_ACTIVE, true);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_CALLBACK_VOLUME, false);
    unisoundAsrEngine.setOption(AsrOption.ASR_OPTION_PRINT_DEBUG_LOG, false);
    unisoundAsrEngine.init();
  }

  private void initTTS() {
    unisoundTtsEngine =
        new UnisoundTtsEngine(context,  androidAudioTrackOnline,
            false);
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_INIT_MODE, UnisoundTtsInitMode.MIX);
    if (!(new File(Config.TTS_PATH + Config.FRONTEND_MODEL).exists())) {
      AssetsUtils.copyAssetsFile(context, Config.FRONTEND_MODEL,
          Config.TTS_PATH + Config.FRONTEND_MODEL, false);
    }
    unisoundTtsEngine.setTtsOption(TtsOption.TTS_OPTION_FRONT_MODEL_PATH,
        Config.TTS_PATH + Config.FRONTEND_MODEL);
    if (!(new File(Config.TTS_PATH + Config.BACKEND_MODEL).exists())) {
      AssetsUtils.copyAssetsFile(context, Config.BACKEND_MODEL,
          Config.TTS_PATH + Config.BACKEND_MODEL, false);
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

  public void release() {
    if (unisoundAsrEngine != null) {
      unisoundAsrEngine.release();
      unisoundAsrEngine = null;
    }
    if (unisoundTtsEngine != null) {
      unisoundTtsEngine.release();
      unisoundTtsEngine = null;
    }
  }

  public UnisoundAsrEngine getUnisoundAsrEngine() {
    return unisoundAsrEngine;
  }

  public UnisoundTtsEngine getUnisoundTtsEngine() {
    return unisoundTtsEngine;
  }

  public void setAsrListener(IAsrResultListener listener) {
    unisoundAsrEngine.setListener(listener);
  }
}
