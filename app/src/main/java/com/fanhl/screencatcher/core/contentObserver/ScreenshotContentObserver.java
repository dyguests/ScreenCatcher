package com.fanhl.screencatcher.core.contentObserver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by fanhl on 2016/11/30.
 */
public class ScreenshotContentObserver extends ContentObserver {
    public static final String TAG = ScreenshotContentObserver.class.getSimpleName();

    private Context mContext;
    private int imageNum;

    private static ScreenshotContentObserver instance;

    private ScreenshotContentObserver(Context context) {
        super(null);
        mContext = context;
    }

    public static void startObserve(Context context) {
        if (instance == null) {
            instance = new ScreenshotContentObserver(context);
        }
        instance.register();
    }

    public static void stopObserve() {
        instance.unregister();
    }

    private void register() {
        mContext.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, this);
    }

    private void unregister() {
        mContext.getContentResolver().unregisterContentObserver(this);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        String[] columns = {
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.DATA,
        };
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    MediaStore.MediaColumns.DATE_MODIFIED + " desc");
            if (cursor == null) {
                return;
            }
            int count = cursor.getCount();
            if (imageNum == 0) {
                imageNum = count;
            } else if (imageNum >= count) {
                return;
            }
            imageNum = count;
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                long addTime = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED));
                if (matchAddTime(addTime) && matchPath(filePath) && matchSize(filePath)) {
                    doReport(filePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加时间与当前时间不超过1.5s,大部分时候不超过1s。
     *
     * @param addTime 图片添加时间，单位:秒
     */
    private boolean matchAddTime(long addTime) {
        return System.currentTimeMillis() - addTime * 1000 < 1500;
    }

    /**
     * 尺寸不大于屏幕尺寸（发现360奇酷手机可以对截屏进行裁剪）
     */
    private boolean matchSize(String filePath) {
//        Point size = Util.getScreenWidthAndHeight(mContext);//获取屏幕尺寸
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, options);
//
//        return size.x >= options.outWidth && size.y >= options.outHeight;
        return true;
    }

    /**
     * 已调查的手机截屏图片的路径中带有screenshot
     */
    private boolean matchPath(String filePath) {
        String lower = filePath.toLowerCase();
        return lower.contains("screenshot");
    }

    private void doReport(String filePath) {
        Log.d(TAG, filePath);
        //删除截屏
        File file = new File(filePath);
//        file.delete();
        //TODO:
    }
}
