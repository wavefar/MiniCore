package org.wavefar.lib.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.wavefar.lib.ui.fragment.agentweb.AgentWebFragment;
import org.wavefar.lib.ui.widget.agentweb.common.FragmentKeyDown;

import java.lang.ref.WeakReference;

import static android.view.View.generateViewId;


/**
 *
 * 盛装Fragment的一个容器(包含AgentWebFragment、BaseFragment) Activity
 * 普通界面只需要编写Fragment,使用此Activity盛装,这样就不需要每个界面都在AndroidManifest中注册一遍
 *  @author Administrator
 */
public class ContainerActivity extends BaseSimpleActivity {
    public static final String FRAGMENT = "fragment";
    public static final String BUNDLE = "bundle";
    protected WeakReference<Fragment> mFragment;
    private ViewGroup mainLayout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        mainLayout = new LinearLayout(this);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        //generateViewId()生成不重复的id
        mainLayout.setId(generateViewId());
        setContentView(mainLayout);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(mainLayout.getId());
        if (fragment == null) {
            initFromIntent(getIntent());
        }
    }

    protected void initFromIntent(Intent data) {
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }
        try {
            String fragmentName = data.getStringExtra(FRAGMENT);
            if (fragmentName == null || "".equals(fragmentName)) {
                throw new IllegalArgumentException("can not find page fragmentName");
            }
            Class<?> fragmentClass = Class.forName(fragmentName);

            Fragment fragment = (Fragment) fragmentClass.newInstance();

            Bundle args = data.getBundleExtra(BUNDLE);
            if (args != null) {
                fragment.setArguments(args);
            }
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(mainLayout.getId(), fragment);
            trans.commitAllowingStateLoss();
            mFragment = new WeakReference<>(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(mainLayout.getId());
        if (fragment instanceof AgentWebFragment) {
            FragmentKeyDown mFragmentKeyDown = (AgentWebFragment) fragment;
            return mFragmentKeyDown.onFragmentKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(mainLayout.getId());
        assert fragment != null;
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
