package com.imkarl.video.recorder.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imkarl.video.recorder.RecordResult;
import com.imkarl.video.recorder.VideoRecorder;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static int RECORDE_SHOW = 10001;

    Button btn_auth;
    Button btn_record;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_auth = (Button) findViewById(R.id.btn_auth);
        btn_record = (Button) findViewById(R.id.record);
        tv_result = (TextView) findViewById(R.id.tv_result);

        btn_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoRecorder.startAuth(v.getContext(), "20713707870378e", "f363e3832846473baf6feebb27eb7402");
            }
        });

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    VideoRecorder.startRecord(MainActivity.this, RECORDE_SHOW, true);
                } catch (Error error) {
                    Log.e("QUPAI", "ERROER", error);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            RecordResult result = VideoRecorder.getRecordResult(data);
            VideoRecorder.deleteDraft(data);

            //得到视频地址，缩略图地址
            File videoFile = result.getVideoFile();
            File thumFile = result.getThumbnailFile();
            long duration = result.getDuration();

            tv_result.setText("视频路径:" + videoFile + "\n图片路径:" + thumFile + "\n时长:" + duration);
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "RESULT_CANCELED", Toast.LENGTH_LONG).show();
        }
    }

}
