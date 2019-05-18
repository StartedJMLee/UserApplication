package com.example.userapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class NotificationActivity extends AppCompatActivity {
    private String userID;
    private String workName; //값 받아오는거 구현 필요
        /*// 알림생성버튼
        private Button create;
        // 알림제거버튼
        private Button remove;
        */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = getIntent().getStringExtra("userID");
        //setContentView(R.layout.activity_notification);


            //create = findViewById(R.id.create);
            //remove = findViewById(R.id.remove);
/*
            create.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {

                    createNotification();
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    removeNotification();
                }
            });*/
        }

        private void createNotification() {
            //클릭하면 보낼 Intent
            Intent intent = new Intent(NotificationActivity.this, WorkPageActivity.class);
            intent.putExtra("workName", workName);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    NotificationActivity.this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

            //알림 내용
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("답글 알림");
            builder.setContentText("작성하신 댓글에 답글이 달렸습니다.");
            builder.setColor(Color.BLUE);

            // 사용자가 탭을 클릭하면 자동 제거
            builder.setAutoCancel(true);
            //클릭하면 작품페이지로 이동
            builder.setContentIntent(pendingIntent);

            // 알림 표시
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
            // id값은
            // 정의해야하는 각 알림의 고유한 int값
            notificationManager.notify(1, builder.build());
        }

        private void removeNotification() {

            // Notification 제거
            NotificationManagerCompat.from(this).cancel(1);
        }
    }
