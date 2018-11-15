package com.xytsz.xytaj.activity;

import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import android.widget.LinearLayout;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.util.IntentUtil;
import com.xytsz.xytaj.util.SpUtils;

/**
 * Created by admin on 2017/1/17.
 * splash页面
 */
public class SplashAcitvity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activty_splash);

       /* LinearLayout mroot = (LinearLayout) findViewById(R.id.ll_splash_root);
        //动画效果参数直接定义
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f, 1.0f);
        alphaAnimation.setDuration(1000);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                boolean isfirst = SpUtils.getBoolean(SplashAcitvity.this, GlobalContanstant.ISFIRSTENTER,true);
//                if (isfirst){
//                    IntentUtil.startActivity(SplashAcitvity.this,GuideActivity.class);
//                }else {
                    IntentUtil.startActivity(SplashAcitvity.this,HomeActivity.class);

//                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        mroot.setAnimation(alphaAnimation);*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String loginId = SpUtils.getString(getApplicationContext(), GlobalContanstant.LOGINID);
                if (loginId != null && !loginId.isEmpty()) {
                    IntentUtil.startActivity(SplashAcitvity.this, HomeActivity.class);
                }else {
                    IntentUtil.startActivity(SplashAcitvity.this, MainActivity.class);
                }
                SplashAcitvity.this.finish();
            }
        },2000);

    }
}
