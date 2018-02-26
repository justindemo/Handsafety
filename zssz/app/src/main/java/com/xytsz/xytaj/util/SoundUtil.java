package com.xytsz.xytaj.util;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by admin on 2017/9/20.
 * 声音的工具类
 */
public class SoundUtil {

    static final private double EMA_FILTER = 0.6;
    private String path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zssz/Audio/";
    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;
    private MediaPlayer mediaPlayer;

    public void start(String name) {
        if (!Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return;
        }
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            mRecorder.setOutputFile(path + name);
            try {
                mRecorder.prepare();
                mRecorder.start();
                mEMA = 0.0;
            } catch (IllegalStateException e) {
                System.out.print(e.getMessage());
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }

        }
    }

    public void stop() {
        if (mRecorder != null) {
            try {
                mRecorder.setOnErrorListener(null);
                mRecorder.setPreviewDisplay(null);
                mRecorder.setOnInfoListener(null);

                mRecorder.stop();
            } catch (IllegalStateException e) {
                mRecorder = null;
                mRecorder = new MediaRecorder();
                //mRecorder.stop();
            }catch (RuntimeException e){
                Log.i("Tag",Log.getStackTraceString(e));
            }
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void pause() {
        if (mRecorder != null) {
            mRecorder.stop();
        }
    }

    public void start() {
        if (mRecorder != null) {
            mRecorder.start();
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude() / 2700.0);
        else
            return 0;

    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    private OnFinishListener onFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public interface OnFinishListener {
        void onFinish();

        void onError();
    }

    private int time;

    public int getTime(String name) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(name);
            mediaPlayer.prepare();
            mediaPlayer.getDuration();
            int duration = mediaPlayer.getDuration();
            if (duration > 0 && duration <= 1000) {
                time = 1;
                return time;
            }
            time = duration / 1000;

            mediaPlayer = null;
            return time;
        } catch (IOException e) {
            return 0;
        }
    }

    public int play(String name) {
        //首先进来是null
        if (mediaPlayer != null) {

            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    onFinishListener.onFinish();
                }else {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(name);
                    mediaPlayer.prepare();

                    mediaPlayer.start();


                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            onFinishListener.onFinish();
                        }
                    });
                }
                return 0;

            } catch (Exception e) {
                onFinishListener.onError();
            }
        }

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(name);
                mediaPlayer.prepare();

                mediaPlayer.start();


                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        onFinishListener.onFinish();
                    }
                });


                return time;
            } catch (Exception e) {


                onFinishListener.onError();

            }
        }

        return 0;
    }


}
