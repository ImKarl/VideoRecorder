package com.imkarl.video.recorder.sample;

import android.app.Application;

import com.imkarl.video.recorder.VideoRecorder;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VideoRecorder.initialize(this);
    }
}
