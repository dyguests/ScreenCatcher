package com.fanhl.screencatcher.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanhl.screencatcher.R;
import com.fanhl.screencatcher.core.screenshot.ScreenShotActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.listen_capture) void listen_capture() {
        ScreenShotActivity.launch(this);
    }
}
