package com.fanhl.screencatcher.core.screenShot;

import android.net.Uri;

/**
 * Created by fanhl on 2016/11/30.
 */
public interface OnScreenshotTakenListener {
    void onScreenshotTaken(Uri uri);
}