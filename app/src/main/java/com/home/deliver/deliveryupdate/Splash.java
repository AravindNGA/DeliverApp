package com.home.deliver.deliveryupdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.home.deliver.deliveryupdate.oldrecycleractivity.DeliverListActivity;

/**
 * Created by aravindnga on 18/07/17.
 */

public class Splash extends AppCompatActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread mine = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1500);
                    //for show i made this screen for 2 seconds.
                    Intent in = new Intent(getApplicationContext(),DeliverListActivity.class);
                    startActivity(in);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mine.start();

    }
}
