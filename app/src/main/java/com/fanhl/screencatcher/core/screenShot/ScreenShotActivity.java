package com.fanhl.screencatcher.core.screenShot;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.fanhl.screencatcher.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Android 监听截屏获取图像
 * See {http://blog.csdn.net/liuhanhan512/article/details/36665915}
 */
public class ScreenShotActivity extends AppCompatActivity {
    private static final int DEFAULT_WIDTH = 512;
    private static final int DEFAULT_HEIGHT = 384;

    @BindView(R.id.imageView) ImageView image;

    private ScreenshotObserver screenShotObserver;

    private int width;
    private int height;
    private int sampleSize = 1;

    private ContentResolver contentResolver;

    private Handler hanlder = new Handler();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, ScreenShotActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);
        ButterKnife.bind(this);

        contentResolver = getContentResolver();
        screenShotObserver = new ScreenshotObserver(new OnScreenshotTakenListener() {
            @Override public void onScreenshotTaken(Uri uri) {
                getBitmapSize(uri);
                final Bitmap bitmap = getBitmap(uri);
                if (bitmap != null) {
                    hanlder.post(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageBitmap(bitmap);
                        }

                    });

                }
            }
        });
        screenShotObserver.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screenShotObserver.stop();
    }

    private void getBitmapSize(Uri uri) {//get bitmap size before draw
        InputStream is = null;
        try {

            is = getInputStream(uri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);

            width = options.outWidth;
            height = options.outHeight;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private Bitmap getBitmap(Uri uri) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {

            try {
                is = getInputStream(uri);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            while ((width / sampleSize > DEFAULT_WIDTH * 2) || (height / sampleSize > DEFAULT_HEIGHT * 2)) {
                sampleSize *= 2;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;

            bitmap = BitmapFactory.decodeStream(is, null, options);

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }

        return bitmap;
    }

    private InputStream getInputStream(Uri mUri) throws IOException {
        try {
            if (mUri.getScheme().equals("file")) {
                return new java.io.FileInputStream(mUri.getPath());
            } else {//from database
                return contentResolver.openInputStream(mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }
}
