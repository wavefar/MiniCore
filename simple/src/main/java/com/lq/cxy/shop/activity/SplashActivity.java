package com.lq.cxy.shop.activity;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.lq.cxy.shop.R;

import org.wavefar.lib.base.BaseSimpleActivity;
import org.wavefar.lib.utils.IntentUtils;


/**
 * 启动页
 * @author summer
 */
public class SplashActivity extends BaseSimpleActivity {

    @Override
    protected int getContentView(){
        return R.layout.activity_splash;
    }

    @Override
    public void initViewObservable() {
        ImageView imageView = findViewById(R.id.logo_iv);
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(3000);
        imageView.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                IntentUtils.redirect(MainActivity.class);
                finish();
            }
        });
    }
}
