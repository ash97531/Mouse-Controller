package com.kridacreations.mouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;

import static android.content.ContentValues.TAG;

public class HowToUse extends AppCompatActivity {

    LinearLayout dotsLayout;
    TextView[] dots;

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;

    private InterstitialAd mInterstitialAd;

    private AdView mAdView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(getString(R.string.admob_banner_ads_id));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView2 = findViewById(R.id.adViewBanner2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest);


        InterstitialAd.load(this,getString(R.string.admob_interestial_ads_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

        getSupportActionBar().setTitle("How To Use");

        dotsLayout = findViewById(R.id.dots_container);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.viewpager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

//        tabLayout.addTab(tabLayout.newTab().setText("First"));
//        tabLayout.addTab(tabLayout.newTab().setText("Second"));
//        tabLayout.addTab(tabLayout.newTab().setText("Third"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dots = new TextView[2];
        dotsIndicator();

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                selectedIndicator(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
                super.onPageSelected(position);
            }
        });
    }

    private void selectedIndicator(int position) {
        for(int i=0; i<dots.length; i++){
            if(i==position){
                dots[i].setTextColor(getResources().getColor(R.color.design_default_color_primary));
            }else{
                dots[i].setTextColor(getResources().getColor(R.color.faded_purple));
            }
        }
    }

    private void dotsIndicator() {
        for(int i=0;i <dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#9679;"));
            dots[i].setTextSize(18);
            dotsLayout.addView(dots[i]);
        }
    }

//    @Override
//    public void onBackPressed() {
//
//        if (mInterstitialAd != null) {
//            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
//                @Override
//                public void onAdDismissedFullScreenContent() {
////                    // Called when fullscreen content is dismissed.
////                    Intent intent = new Intent(HowToUse.this, MainActivity.class);
////                    startActivity(intent);
////                    Log.v("else","else");
////                    Log.d("TAG", "The ad was dismissed.");
//                }
//
//                @Override
//                public void onAdFailedToShowFullScreenContent(AdError adError) {
////                    // Called when fullscreen content failed to show.
////                    Intent intent = new Intent(HowToUse.this, MainActivity.class);
////                    startActivity(intent);
////                    Log.v("else","else");
////                    Log.d("TAG", "The ad failed to show.");
//                }
//
//                @Override
//                public void onAdShowedFullScreenContent() {
//                    // Called when fullscreen content is shown.
//                    // Make sure to set your reference to null so you don't
//                    // show it a second time.
//                    mInterstitialAd = null;
//                    Log.d("TAG", "The ad was shown.");
//                }
//            });
//            mInterstitialAd.show(HowToUse.this);
//            Log.v("shown","shown");
//        } else {
////            Intent intent = new Intent(HowToUse.this, MainActivity.class);
////            startActivity(intent);
////            Log.v("else","else");
//        }
//    }
}