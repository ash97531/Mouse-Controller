package com.kridacreations.mouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

//    public Connector connector = new Connector("helo");
    Thread thread1 = null;
    TextView statusInfo, xText, yText, zText;
    String SERVER_IP;
    EditText enterIP;
    Button connect, continueToController, howToUse;
    Boolean connected = false;
    private AdView mAdView;


    private InterstitialAd mInterstitialAd;

//    private Sensor mySensor;
//    private SensorManager SM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Banner add
        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(getString(R.string.admob_banner_ads_id));

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adViewBanner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // InterstitialAd
//        AdRequest adRequest = new AdRequest.Builder().build();

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
//        xText = (TextView)findViewById(R.id.Xtext);
//        yText = (TextView)findViewById(R.id.Ytext);
//        zText = (TextView)findViewById(R.id.Ztext);

//        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
//
//        // Accelerometer Sensor
//        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//        SM.unregisterListener(this);
//        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        enterIP = (EditText) findViewById(R.id.enterIP);
        connect = (Button) findViewById(R.id.connect);
        continueToController = (Button) findViewById(R.id.continueToController);
        statusInfo = (TextView) findViewById(R.id.statusInfo);
        howToUse = (Button) findViewById(R.id.HowToUse);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 999) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };

        enterIP.setFilters(filters);

        howToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd != null) {
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Intent intent = new Intent(MainActivity.this, HowToUse.class);
                            startActivity(intent);
                            Log.v("else","else");
                            Log.d("TAG", "The ad was dismissed.");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            Intent intent = new Intent(MainActivity.this, HowToUse.class);
                            startActivity(intent);
                            Log.v("else","else");
                            Log.d("TAG", "The ad failed to show.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
//                            mInterstitialAd = null;
                            Log.d("TAG", "The ad was shown.");
                        }
                    });
                    mInterstitialAd.show(MainActivity.this);
                    Log.v("shown","shown");
                } else {
                    Intent intent = new Intent(MainActivity.this, HowToUse.class);
                    startActivity(intent);
                    Log.v("else","else");
                }
                Log.v("outer","outer");


            }
        });

        continueToController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connected){
                    connected = false;
                    continueToController.setBackground(getResources().getDrawable(R.drawable.faded_button));
//                    Intent intent = new Intent(MainActivity.this, MouseController.class);
//                    intent.putExtra("IP", SERVER_IP);
//                    startActivity(intent);

                    boolean addLoaded = false;
                    if (mInterstitialAd != null) {
                        addLoaded = true;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Intent intent = new Intent(MainActivity.this, MouseController.class);
                                intent.putExtra("IP", SERVER_IP);
                                startActivity(intent);
                                Log.v("else","else");
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Intent intent = new Intent(MainActivity.this, MouseController.class);
                                intent.putExtra("IP", SERVER_IP);
                                startActivity(intent);
                                Log.v("else","else");
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
//                            mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                        mInterstitialAd.show(MainActivity.this);
                        Log.v("shown","shown");
                    } else if(!addLoaded){
                        Intent intent = new Intent(MainActivity.this, MouseController.class);
                        intent.putExtra("IP", SERVER_IP);
                        startActivity(intent);
                        Log.v("else","else");
                    }

                }
            }
        });



        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Log.v("Main", enterIP.getText().toString());

                SERVER_IP = enterIP.getText().toString();

                thread1 = new Thread(new Thread1());
                thread1.start();


            }
        });
//        initiateSocketConnection();
    }


    private PrintWriter output;
    private BufferedReader input;

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//        xText.setText("X: " + event.values[0]);
//        yText.setText("Y: " + event.values[1]);
//        zText.setText("Z: " + event.values[2]);
//
////        if(event.values[0] > 0.5)
////            xText.setText("X: " + (event.values[0] - 0.5));
////        if(event.values[1] > 1)
////            yText.setText("Y: " + (event.values[1]-1));
////        if(event.values[2] > 9.8)
////            zText.setText("Z: " + (event.values[2] - 9.8));
////        if(event.values[0] < -0.5)
////            xText.setText("X: " + (event.values[0] + 0.5));
////        if(event.values[1] < 0)
////            yText.setText("Y: " + (event.values[1]));
////        if(event.values[2] < 9)
////            zText.setText("Z: " + (event.values[2] - 9));
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // No use
//    }


    class Thread1 implements Runnable {
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, 5000);
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output.print("0 0");
                output.flush();

                connected = true;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        continueToController.setBackgroundColor(0xFF6200EA);
                        continueToController.setBackground(getResources().getDrawable(R.drawable.button));
                        statusInfo.setTextColor(0xFF109C38);
                        statusInfo.setText("Connected");
                        statusInfo.setVisibility(View.VISIBLE);
                    }
                });

                statusInfo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        statusInfo.setVisibility(View.GONE);
                    }
                }, 1500);

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        tvMessages.setText("Connected\n");
//                    }
//                });
//                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
//                new Thread() {
//                    public void run() {
//                        errorMessage.setVisibility(View.VISIBLE);
//                        try {
//                            sleep(1000);
//                        } catch (InterruptedException interruptedException) {
//                            interruptedException.printStackTrace();
//                        }
//                        errorMessage.setVisibility(View.GONE);
//                    }
//                }.start();
                connected = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        continueToController.setBackgroundColor(0x426200EA);
                        continueToController.setBackground(getResources().getDrawable(R.drawable.faded_button));
                        statusInfo.setTextColor(0xFFFB4444);
                        statusInfo.setText("Try Again!!!");
                        statusInfo.setVisibility(View.VISIBLE);
                    }
                });

                statusInfo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        statusInfo.setVisibility(View.GONE);
                    }
                }, 1500);
            }
        }
    }
}