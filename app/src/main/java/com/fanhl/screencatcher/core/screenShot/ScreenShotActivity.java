package com.fanhl.screencatcher.core.screenShot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanhl.screencatcher.R;

/**
 * Android 监听截屏获取图像
 * See {http://blog.csdn.net/liuhanhan512/article/details/36665915}
 */
public class ScreenShotActivity extends AppCompatActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, ScreenShotActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);
    }
}
