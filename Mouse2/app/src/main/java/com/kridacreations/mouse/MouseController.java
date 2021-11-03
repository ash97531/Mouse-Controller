package com.kridacreations.mouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class MouseController extends AppCompatActivity implements SensorEventListener {
//    TextView xText, yText, zText;
    EditText enterMessage;
    Button sendMessage, leftClick, rightClick, middleClick, scrollDown, scrollUp;
    String SERVER_IP, clickInfo;
    TextView gyroscopeAlert, holdTextView, tapToUnhold, connectionDot1, connectionDot2, connectionDot3;
    LinearLayout holdLayout;
    float curX = 0, curY = 0, curZ, setX = 0, setY = 0, setZ = 0;

    Boolean isHold = false;

//    Thread thread1 = null, thread4 = null;
    Thread thread2 = null;

    Sensor gyroscopeSensor;
    SensorManager SM;

    Runnable mouseMove;
//    Runnable task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        getSupportActionBar().setTitle("Controller");

//        xText = (TextView)findViewById(R.id.Xtext);
//        yText = (TextView)findViewById(R.id.Ytext);
//        zText = (TextView)findViewById(R.id.Ztext);

        Intent intent = getIntent();
        SERVER_IP = intent.getStringExtra("IP");

//        enterMessage = (EditText) findViewById(R.id.enterMessage);
//        sendMessage = (Button) findViewById(R.id.send);
        leftClick = (Button) findViewById(R.id.leftClick);
        rightClick = (Button) findViewById(R.id.rightClick);
        middleClick = (Button) findViewById(R.id.middleClick);
        scrollDown = (Button) findViewById(R.id.scrollDOWN);
        scrollUp = (Button) findViewById(R.id.scrollUP);
        gyroscopeAlert = (TextView) findViewById(R.id.gyroscope_alert);
        holdTextView = (TextView) findViewById(R.id.hold);
        tapToUnhold = (TextView) findViewById(R.id.tapToUnhold);
        holdLayout = (LinearLayout) findViewById(R.id.hold_layout);
        connectionDot1 = (TextView) findViewById(R.id.connection_dot_1);
        connectionDot2 = (TextView) findViewById(R.id.connection_dot_2);
        connectionDot3 = (TextView) findViewById(R.id.connection_dot_3);

        SM = (SensorManager)getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(gyroscopeSensor == null){
            gyroscopeAlert.setVisibility(View.VISIBLE);
        }

        SM.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);

        holdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHold = !isHold;
                if(isHold){
                    holdTextView.setVisibility(View.VISIBLE);
                    tapToUnhold.setVisibility(View.VISIBLE);
                    holdLayout.setBackground(getResources().getDrawable(R.drawable.hold_view));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holdLayout.setElevation(6f);
                        tapToUnhold.setElevation(6f);
                        holdTextView.setElevation(6f);
                    }
                    Log.v("point pressed", "point pressed");
                }else{
                    holdTextView.setVisibility(View.INVISIBLE);
                    tapToUnhold.setVisibility(View.INVISIBLE);
                    holdLayout.setBackground(getResources().getDrawable(R.drawable.invisible_layout));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holdLayout.setElevation(2f);
                        tapToUnhold.setElevation(2f);
                        holdTextView.setElevation(2f);
                    }
                    Log.v("point up", "point up");
                }
            }
        });

        mouseMove = new Runnable() {
            @Override
            public void run() {
//                Timer t = new Timer();
//                t.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
                        if(!isHold) {
                            try {
                                Socket socket = new Socket(SERVER_IP, 5000);
                                PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                                outToServer.print(curX * 60);
                                outToServer.print(" ");
                                outToServer.print(curZ * 60);

//                        outToServer.print(curX * 60);
//                        outToServer.print(" ");
//                        outToServer.print(curY * 60);

                                outToServer.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//                    }
//                }, 0, 10);
            }
        };

        thread2 = new Thread(mouseMove);

//        new Thread(new connectionDotThread()).start();
//        if(!thread2.isAlive()){
//            thread2.start();
//        }

//        task = new Runnable() {
//            @Override
//            public void run() {
//                Timer t = new Timer();
//                t.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        try {
//                            Socket socket = new Socket(SERVER_IP, 5000);
//                            PrintWriter outToServer  = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//
//                            outToServer.print(clickInfo);
//
//                            outToServer.flush();
//                        }catch (IOException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }, 0, 10);
//            }
//        };
//
//        thread4 = new Thread(task);

//        sendMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                thread1 = new java.lang.Thread(new Thread1());
////                thread1.start();
//                setX = curX;
//                setY = curY;
//                setZ = curZ;
//            }
//        });

        leftClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInfo = "L";
//                Log.v("Left click", "Left click");
//                thread3 = new java.lang.Thread(new Thread3());
//                thread3.start();
                new java.lang.Thread(new Thread3()).start();
            }
        });

        rightClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInfo = "R";
//                Log.v("Rigth click", "Right click");
//                thread3 = new java.lang.Thread(new Thread3());
//                thread3.start();
                new java.lang.Thread(new Thread3()).start();
            }
        });

        middleClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInfo = "M";
//                Log.v("Middle click", "Middle click");
                new java.lang.Thread(new Thread3()).start();
//                thread3;
            }
        });

//        scrollUp.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                clickInfo = "U";
////                thread4 = new java.lang.Thread(new Thread4());
//                thread4.start();
////                Toast.makeText(MouseController.this, "Long press", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInfo = "U";
                new java.lang.Thread(new Thread3()).start();
            }
        });

        scrollDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInfo = "D";
                new java.lang.Thread(new Thread3()).start();
            }
        });

//        thread2 = new Thread(new Thread2());
//        thread2.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        curX = (float) (((int)(event.values[0] * 100)) / 100.0);
        curY = (float) (((int)(event.values[1] * 100)) / 100.0);
        curZ = (float) (((int)(event.values[2] * 100)) / 100.0);

        new java.lang.Thread(new Thread2()).start();
//        xText.setText("X: " + curX);
//        yText.setText("Y: " + curY);
//        zText.setText("Z: " + curZ);
    }

//    private void sendCommand(){
//        if(!isHold) {
//            try {
//                Socket socket = new Socket(SERVER_IP, 5000);
//                PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//
//                outToServer.print(curX * 60);
//                outToServer.print(" ");
//                outToServer.print(curZ * 60);
//
////                        outToServer.print(curX * 60);
////                        outToServer.print(" ");
////                        outToServer.print(curY * 60);
//
//                outToServer.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No use
    }

//    class Thread1 implements Runnable {
//        @Override
//        public void run() {
//
//            try{
//                Log.v("MouseController", SERVER_IP);
//                Socket socket = new Socket(SERVER_IP, 5000);
////                socket = new Socket(SERVER_IP, 5000);
//                PrintWriter outToServer  = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
////                outToServer  = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//                outToServer.print(enterMessage.getText().toString());
//                outToServer.flush();
//                Log.v("result", "success");
//            }catch (IOException e){
//                e.printStackTrace();
//                Log.v("result", "failed");
//            }
//        }
//    }

    class Thread3 implements Runnable{
        @Override
        public void run() {
            if(!isHold) {
                try {
                    Socket socket = new Socket(SERVER_IP, 5000);
                    PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                    outToServer.print(clickInfo);

                    outToServer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    class connectionDotThread implements Runnable{
//        @Override
//        public void run() {
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    connectionDot1.setBackgroundColor(0xFF000000);
//                }
//            });
//
//            Timer t = new Timer();
//            final int[] count = {0};
//            t.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    count[0]++;
//                    if(count[0] == 1){
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                connectionDot3.setBackground(getResources().getDrawable(R.drawable.connection_dot_black));
//                            }
//                        });
////                        connectionDot3.setBackgroundColor(0xFF484848);
//                    }else if(count[0] == 2){
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                connectionDot2.setBackground(getResources().getDrawable(R.drawable.connection_dot_black));
//                            }
//                        });
////                        connectionDot2.setBackgroundColor(0xFF484848);
//                    }else if(count[0] == 3){
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                connectionDot1.setBackground(getResources().getDrawable(R.drawable.connection_dot_black));
//                            }
//                        });
////                        connectionDot1.setBackgroundColor(0xFF484848);
//                    }else{
//                        count[0] = 0;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                connectionDot1.setBackground(getResources().getDrawable(R.drawable.connection_dot_white));
//                                connectionDot2.setBackground(getResources().getDrawable(R.drawable.connection_dot_white));
//                                connectionDot3.setBackground(getResources().getDrawable(R.drawable.connection_dot_white));
//                            }
//                        });
////                        connectionDot2.setBackgroundColor(0xFF484848);
//                    }
//                }
//            }, 0, 500);
//        }
//    }

    class Thread2 implements Runnable{
        @Override
        public void run() {
//            Timer t = new Timer();
//            t.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {

            if(!isHold) {
                try {
                    Socket socket = new Socket(SERVER_IP, 5000);
                    PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                    outToServer.print(curX * 60);
                    outToServer.print(" ");
                    outToServer.print(curZ * 60);

//                        outToServer.print(curX * 60);
//                        outToServer.print(" ");
//                        outToServer.print(curY * 60);

                    outToServer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//                }
//            }, 0, 10);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.v("MouseController", "On pause");
        SM.unregisterListener(this);
//        if(thread2.isAlive()){
//            thread2.interrupt();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MouseController", "On destroy");
        SM.unregisterListener(this);
//        if(thread2.isAlive()){
//            thread2.interrupt();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("MouseController", "On resume");
        SM.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        if (SM.getRegisteredListenerList == null){ registerListener() }

//        SM.unregisterListener(this);
//        if(!thread2.isAlive()){
//            thread2.start();
//        }
    }
}