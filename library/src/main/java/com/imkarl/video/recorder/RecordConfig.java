package com.imkarl.video.recorder;

/**
 * 录制属性配置
 */
public class RecordConfig {

    /**
     * 视频的宽高
     * Tips：暂时只支持1:1
     */
    public static final int DEFAULT_VIDEO_WIDTH = 480;
    public static final int DEFAULT_VIDEO_HEIGHT = 480;

    /**
     * 默认
     */
    public static final int DEFAULT_VIDEO_FRAME_RATE = 30;
    /**
     * 默认视频时长
     */
    public static final float DEFAULT_MAX_DURATION = 10;
    /**
     * 默认最小时长
     */
    public static final float DEFAULT_MIN_DURATION = 2;
    /**
     * 默认视频码率
     */
    public static final int DEFAULT_BITRATE = 2000 * 1000;


    /**
     * 视频的宽高
     * Tips：暂时只支持1:1
     */
    private int videoWidth = DEFAULT_VIDEO_WIDTH;
    private int videoHeight = DEFAULT_VIDEO_HEIGHT;
    /**
     * 默认
     */
    private int videoFrameRate = DEFAULT_VIDEO_FRAME_RATE;
    /**
     * 默认视频时长
     */
    private float videoMaxDuration = DEFAULT_MAX_DURATION;
    /**
     * 默认最小时长
     */
    private float videoMinDuration = DEFAULT_MIN_DURATION;
    /**
     * 默认视频码率
     */
    private int videoBitrate = DEFAULT_BITRATE;
    /**
     * 水印本地路径，文件必须为rgba格式的PNG图片
     */
    private String waterMarkPath;

    public RecordConfig() {
    }
    public RecordConfig(int videoWidth,
                        int videoHeight,
                        int videoFrameRate,
                        float videoMaxDuration,
                        float videoMinDuration,
                        int videoBitrate,
                        String waterMarkPath) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        this.videoFrameRate = videoFrameRate;
        this.videoMaxDuration = videoMaxDuration;
        this.videoMinDuration = videoMinDuration;
        this.videoBitrate = videoBitrate;
        this.waterMarkPath = waterMarkPath;
    }

    @Override
    public String toString() {
        return "RecordConfig{" +
                "videoWidth=" + videoWidth +
                ", videoHeight=" + videoHeight +
                ", videoFrameRate=" + videoFrameRate +
                ", videoMaxDuration=" + videoMaxDuration +
                ", videoMinDuration=" + videoMinDuration +
                ", videoBitrate=" + videoBitrate +
                ", waterMarkPath='" + waterMarkPath + '\'' +
                '}';
    }

    public int getVideoWidth() {
        return videoWidth;
    }
    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }
    public int getVideoHeight() {
        return videoHeight;
    }
    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }
    public int getVideoFrameRate() {
        return videoFrameRate;
    }
    public void setVideoFrameRate(int videoFrameRate) {
        this.videoFrameRate = videoFrameRate;
    }
    public float getVideoMaxDuration() {
        return videoMaxDuration;
    }
    public void setVideoMaxDuration(float videoMaxDuration) {
        this.videoMaxDuration = videoMaxDuration;
    }
    public float getVideoMinDuration() {
        return videoMinDuration;
    }
    public void setVideoMinDuration(float videoMinDuration) {
        this.videoMinDuration = videoMinDuration;
    }
    public int getVideoBitrate() {
        return videoBitrate;
    }
    public void setVideoBitrate(int videoBitrate) {
        this.videoBitrate = videoBitrate;
    }
    public String getWaterMarkPath() {
        return waterMarkPath;
    }
    public void setWaterMarkPath(String waterMarkPath) {
        this.waterMarkPath = waterMarkPath;
    }
}
