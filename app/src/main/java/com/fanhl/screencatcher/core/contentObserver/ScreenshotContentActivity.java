package com.fanhl.screencatcher.core.contentObserver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanhl.screencatcher.R;

public class ScreenshotContentActivity extends AppCompatActivity {
    public static void launch(Context context) {
        context.startActivity(new Intent(context, ScreenshotContentActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot_content);
    }

    @Override protected void onResume() {
        super.onResume();
        ScreenshotContentObserver.startObserve(this);
    }

    @Override protected void onPause() {
        ScreenshotContentObserver.stopObserve();
        super.onPause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
