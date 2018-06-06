package app.activity;

import android.content.Intent;
import android.os.Bundle;

import com.topmission.gomipoi.R;

import app.animation.translate.SlideTranslateAnimation;
import app.define.FragmentEventCode;
import app.fragment.TutorialFragment;
import common.activity.GBActivityBase;
import lib.fragment.FragmentTranslateAnimationBase;

/**
 * チュートリアル画面のActivityクラス
 */
public class TutorialActivity extends GBActivityBase {

    // ------------------------------
    // Override
    // ------------------------------
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_common);
    }

    @Override
    protected void onCreateHandler(Bundle bundler) {
        super.onCreateHandler(bundler);
        setFirstFragment(
                TutorialFragment.newInstance(),
                TutorialFragment.getName(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TutorialはisEnabledConnection=Falseにしてあるので、
        // UUIDのパーミッションをチェックする必要はない
        // メンテナンス等のチェックを行う
        initLoading();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.layoutFragment;
    }

    @Override
    protected int getResizeBaseViewId() {
        return R.id.layoutFragment;
    }

    @Override
    protected boolean isEnabledConnection() {
        return false;
    }

    // ------------------------------
    // OnFragmentListener
    // ------------------------------
    @Override
    public void onEvent(int eventCode, Object data) {
        FragmentEventCode code = FragmentEventCode.valueOf(eventCode);
        switch (code) {
            case TutorialNext: {
                FragmentTranslateAnimationBase animation = new SlideTranslateAnimation();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(animation.getRightEnter(), animation.getLeftExit());
                TutorialActivity.this.finish();
                break;
            }

            default: {
                super.onEvent(eventCode, data);
                break;
            }
        }
    }
}
