package app.manager;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.topmission.gomipoi.R;

import java.util.ArrayList;
import java.util.List;

import app.application.GBApplication;
import app.view.OutlineTextView;
import lib.timer.OnWaitTimerListener;
import lib.timer.WaitTimer;

/**
 */
public class PointManager {

    // ------------------------------
    // Member
    // ------------------------------
    private Context context;

    private OutlineTextView outlineTextViewPoint1;
    private OutlineTextView outlineTextViewPoint2;
    private OutlineTextView outlineTextViewPoint3;

    private List<String> mStackQueue;

    // ------------------------------
    // Constructor
    // ------------------------------
    public PointManager(Context context, View layoutPointEffect) {
        this.context = context;
        this.outlineTextViewPoint1 = (OutlineTextView)layoutPointEffect.findViewById(R.id.outlineTextViewPoint1);
        this.outlineTextViewPoint2 = (OutlineTextView)layoutPointEffect.findViewById(R.id.outlineTextViewPoint2);
        this.outlineTextViewPoint3 = (OutlineTextView)layoutPointEffect.findViewById(R.id.outlineTextViewPoint3);

        mStackQueue = new ArrayList<>();
    }

    // ------------------------------
    // Accesser
    // ------------------------------
    public void showPoint(String pointText) {
        if (outlineTextViewPoint1 != null && outlineTextViewPoint1.getVisibility() == View.GONE) {
            outlineTextViewPoint1.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
            startPointText1Animation(pointText);
            return;
        }

        if (outlineTextViewPoint2 != null && outlineTextViewPoint2.getVisibility() == View.GONE) {
            outlineTextViewPoint2.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
            startPointText2Animation(pointText, 200L);
            return;
        }

        if (outlineTextViewPoint3 != null && outlineTextViewPoint3.getVisibility() == View.GONE) {
            outlineTextViewPoint3.setOutlineTextAligh(OutlineTextView.ALIGN_CENTER);
            startPointText3Animation(pointText, 400L);
            return;
        }

        mStackQueue.add(0, pointText);
    }

    // ------------------------------
    // Function
    // ------------------------------
    private void startPointText1Animation(String text) {
        if (outlineTextViewPoint1 == null) {
            return;
        }

        outlineTextViewPoint1.setVisibility(View.INVISIBLE);
        outlineTextViewPoint1.setText(text);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                0,
                0,
                0,
                (int)((GBApplication)context).getResizeManager().calcValue(-20));
        translateAnimation.setDuration(300L);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                outlineTextViewPoint1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                outlineTextViewPoint1.clearAnimation();

                if (mStackQueue != null && mStackQueue.size() > 0) {
                    String nextText = mStackQueue.get(mStackQueue.size() - 1);
                    mStackQueue.remove(mStackQueue.size() - 1);
                    startPointText1Animation(nextText);
                } else {
                    outlineTextViewPoint1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        outlineTextViewPoint1.bringToFront();
        outlineTextViewPoint1.startAnimation(translateAnimation);
    }

    private void startPointText2Animation(String text, long offsetTime) {
        if (outlineTextViewPoint2 == null) {
            return;
        }

        outlineTextViewPoint2.setVisibility(View.INVISIBLE);
        outlineTextViewPoint2.setText(text);
        final TranslateAnimation translateAnimation = new TranslateAnimation(
                0,
                0,
                0,
                (int)((GBApplication)context).getResizeManager().calcValue(-20));
        translateAnimation.setDuration(300L);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                outlineTextViewPoint2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                outlineTextViewPoint2.clearAnimation();

                if (mStackQueue != null && mStackQueue.size() > 0) {
                    String nextText = mStackQueue.get(mStackQueue.size() - 1);
                    mStackQueue.remove(mStackQueue.size() - 1);
                    startPointText2Animation(nextText, 0L);
                } else {
                    outlineTextViewPoint2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        outlineTextViewPoint2.bringToFront();

        if (offsetTime != 0L) {
            WaitTimer timer = new WaitTimer(offsetTime, new OnWaitTimerListener() {
                @Override
                public void onCompletedTimer(Object data) {
                    if (outlineTextViewPoint2 != null) {
                        outlineTextViewPoint2.startAnimation(translateAnimation);
                    }
                }
            });
            timer.start();
        } else {
            outlineTextViewPoint2.startAnimation(translateAnimation);
        }
    }

    private void startPointText3Animation(String text, long offsetTime) {
        if (outlineTextViewPoint3 == null) {
            return;
        }

        outlineTextViewPoint3.setVisibility(View.INVISIBLE);
        outlineTextViewPoint3.setText(text);
        final TranslateAnimation translateAnimation = new TranslateAnimation(
                0,
                0,
                0,
                (int)((GBApplication)context).getResizeManager().calcValue(-20));
        translateAnimation.setDuration(300L);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                outlineTextViewPoint3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                outlineTextViewPoint3.clearAnimation();

                if (mStackQueue != null && mStackQueue.size() > 0) {
                    String nextText = mStackQueue.get(mStackQueue.size() - 1);
                    mStackQueue.remove(mStackQueue.size() - 1);
                    startPointText3Animation(nextText, 0L);
                } else {
                    outlineTextViewPoint3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        outlineTextViewPoint3.bringToFront();

        if (offsetTime != 0L) {
            WaitTimer timer = new WaitTimer(offsetTime, new OnWaitTimerListener() {
                @Override
                public void onCompletedTimer(Object data) {
                    if (outlineTextViewPoint3 != null) {
                        outlineTextViewPoint3.startAnimation(translateAnimation);
                    }
                }
            });
            timer.start();
        } else {
            outlineTextViewPoint3.startAnimation(translateAnimation);
        }
    }

}
