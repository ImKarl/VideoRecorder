package com.imkarl.video.recorder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.File;

/**
 * 录制后的响应结果
 */
public class RecordResult {

    private final Bundle _Bundle;
    public static final String RESULT_KEY = "qupai.edit.result";
    public static final String XTRA_PATH = "path";
    public static final String XTRA_THUMBNAIL = "thumbnail";
    public static final String XTRA_DURATION = "duration";

    public RecordResult(Intent intent) {
        _Bundle = intent.getBundleExtra(RESULT_KEY);
    }

    @Override
    public String toString() {
        return "RecordResult{" +
                "videoFile=" + getVideoFile() +
                ", thumbnailFile=" + getThumbnailFile() +
                ", duration=" + getDuration() +
                '}';
    }

    /**
     * 获取视频
     */
    public File getVideoFile() {
        String videoPath = _Bundle.getString(XTRA_PATH);
        if (TextUtils.isEmpty(videoPath)) {
            return null;
        }

        File videoFile = new File(videoPath);
        if (!videoFile.exists() || !videoFile.isFile()) {
            return null;
        }

        return videoFile;
    }

    /**
     * 获取缩略图
     */
    public File getThumbnailFile() {
        String[] thumbs = _Bundle.getStringArray(XTRA_THUMBNAIL);
        if (thumbs == null && thumbs.length > 0) {
            return null;
        }

        String thumbPath = thumbs[0];
        if (TextUtils.isEmpty(thumbPath)) {
            return null;
        }

        File thumbFile = new File(thumbPath);
        if (!thumbFile.exists() || !thumbFile.isFile()) {
            return null;
        }

        return thumbFile;
    }

    /**
     * 获取时长
     */
    public long getDuration() {
        return _Bundle.getLong(XTRA_DURATION);
    }

}
