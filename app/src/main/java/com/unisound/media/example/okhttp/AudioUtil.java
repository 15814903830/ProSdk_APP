package com.unisound.media.example.okhttp;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

/***
 * 系统音量控制器
 *
 * @author wxs
 *
 */
public class AudioUtil {
    /**
     * 音量加
     *
     * @param ac
     */
    public static boolean soundUP(Activity ac) {
        return soundCtrl(1, ac);
    }

    /**
     * 音量减
     *
     * @param ac
     */
    public static boolean soundDOWN(Activity ac) {
        return soundCtrl(0, ac);
    }

    private static boolean soundCtrl(int flag, Activity ac) {
        AudioManager mAudioManager = (AudioManager) ac.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (flag == 0) {
            if (currentVolume == 0) {
                return false;
            }
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
            return true;
        } else {
            if (currentVolume == maxVolume) {
                return false;
            }
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
            return true;
        }
    }
}