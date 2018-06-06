package app.ad.banner;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.util.Timer;

import app.ad.GBAdSetting;

/**
 * バナータイプのADの管理クラス
 */
public class GBAdBannerManager {

    // ------------------------------
    // Define
    // ------------------------------
    private static final int AD_SWITCH_TIME_MS	= 30000;
    private static final int HEIGHT_AD_DFP = 50;
    private static final int WIDTH_AD_DFP = 320;

    // ------------------------------
    // Member
    // ------------------------------
    private static FrameLayout sAdBannerLayout;
    private static PublisherAdView sAdView;
    private static Handler sHandler;
    private static Timer sTimer;

    // ------------------------------
    // Accesser
    // ------------------------------
    public static void setAdBanner(FrameLayout adBanner) {
        sAdBannerLayout = adBanner;
    }

    public static FrameLayout getBanner(Context context) {
        if (sAdBannerLayout == null) {
            makeBanner(context);
        }

        if (sAdBannerLayout.getParent() != null) {
            ((ViewGroup) sAdBannerLayout.getParent()).removeView(sAdBannerLayout);
        }
        return sAdBannerLayout;
    }

    public static void onPause() {
        if (sAdView != null)
            sAdView.pause();
    }

    public static void onResume() {
        if (sAdView != null)
            sAdView.resume();
    }

    public static void close() {
        onPause();
        if (sAdView != null) {
            if (sAdBannerLayout.getParent() != null) {
                ((ViewGroup) sAdBannerLayout.getParent()).removeView(sAdBannerLayout);
            }
        }
    }

    public static PublisherAdView getDFPView(Context context) {
        PublisherAdView adView = new PublisherAdView(context);

        PublisherAdRequest request = getAdRequest();

        adView.setAdUnitId(GBAdSetting.DFP_BANNER_ID);
        // bannerのサイズを320x50dpに固定
        adView.setAdSizes(AdSize.BANNER);
        adView.loadAd(request);

        return adView;
    }

    // ------------------------------
    // Function
    // ------------------------------
    private static void makeBanner(final Context context) {
        // ビュー作成
        sAdBannerLayout = new FrameLayout(context);
        ImageView noAdImage = new ImageView(context);
        View adView = new View(context);

        // 広告ビュー設定
        sAdView = getDFPView(context);
        adView = sAdView;

        // サブビューを追加
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        // サイズ調整
        float density = context.getResources().getDisplayMetrics().density;
        FrameLayout.LayoutParams adViewParams = new FrameLayout.LayoutParams(
                (int)(WIDTH_AD_DFP * density),
                (int)(HEIGHT_AD_DFP * density));
        adViewParams.gravity = Gravity.CENTER;

        sAdBannerLayout.addView(noAdImage, imageParams);
        sAdBannerLayout.addView(adView, adViewParams);

        final Activity activity = (Activity) context;
        // Image設定
        noAdImage.setImageResource(getNoAdImageResId());
        noAdImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (activity != null) {
                }
            }
        });
    }

    private static int getNoAdImageResId() {
        return 0;
    }

    private static PublisherAdRequest getAdRequest() {
        PublisherAdRequest.Builder requestBuilder = new PublisherAdRequest.Builder();
        requestBuilder.addTestDevice("0143DA65E6B00FC9ED67F103E3863EF0");
        return requestBuilder.build();
    }

}
