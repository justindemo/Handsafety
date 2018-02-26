package com.xytsz.xytaj.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;

/**
 * Created by admin on 2017/3/28.
 * 发送大图
 */
public class SendBigPhotoActivity extends AppCompatActivity {

    private String imageurl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         if (getIntent() != null){
            imageurl = getIntent().getStringExtra("imageurl");
        }
        setContentView(R.layout.activity_sendphoto);
        ImageView imageView = (ImageView) findViewById(R.id.send_photo);
        Glide.with(getApplicationContext()).load(imageurl).into(imageView);
    }
}
