package com.xytsz.xytaj.activity;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xytsz.xytaj.R;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2017/10/24.
 *
 * 视频播放
 */
public class PlayVideoActivity  extends AppCompatActivity{
    public static final String TAG = "PlayVideoActiviy";

    private ScalableVideoView mScalableVideoView;
    private ImageView mPlayImageView;
    private ImageView mThumbnailImageView;
    private String filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent()!= null){
            filePath = getIntent().getStringExtra("videoPath");
        }
        setContentView(R.layout.activity_playvideo);

        initActionbar();
        mScalableVideoView = (ScalableVideoView) findViewById(R.id.video_view);

        try {
            // 这个调用是为了初始化mediaplayer并让它能及时和surface绑定
            mScalableVideoView.setDataSource("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayImageView = (ImageView) findViewById(R.id.playImageView);

        mThumbnailImageView = (ImageView) findViewById(R.id.thumbnailImageView);
        mThumbnailImageView.setImageBitmap(getVideoThumbnail(filePath));


    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.play_video);
        }
    }

    /**
     * 获取播放缩略图
     * @param filePath
     * @return
     */
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(TimeUnit.MILLISECONDS.toMicros(1));
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_view:
                mScalableVideoView.stop();
                mPlayImageView.setVisibility(View.VISIBLE);
                mThumbnailImageView.setVisibility(View.VISIBLE);
                break;
            case R.id.playImageView:
                try {
                    mScalableVideoView.setDataSource(filePath);
                    mScalableVideoView.setLooping(true);
                    mScalableVideoView.prepare();
                    mScalableVideoView.start();
                    mPlayImageView.setVisibility(View.GONE);
                    mThumbnailImageView.setVisibility(View.GONE);
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                    Toast.makeText(this, "播放视频异常", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
