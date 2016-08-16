package com.imkarl.video.recorder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.duanqu.qupai.auth.AuthService;
import com.duanqu.qupai.auth.QupaiAuthListener;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.ProjectOptions;
import com.duanqu.qupai.engine.session.ThumbnailExportOptions;
import com.duanqu.qupai.engine.session.UISettings;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.duanqu.qupai.utils.AppGlobalSetting;
import com.duanqu.qupai.utils.FileUtils;

import java.io.File;
import java.util.UUID;

/**
 * 视频录制
 * @version imkarl 2016-08
 */
public class VideoRecorder {

    // 存储目录 建议使用uid cid之类的信息,不要写死
    private static final String APP_AUTH_SPACE = UUID.randomUUID().toString().replace("-","");
    //accessToken 通过调用授权接口得到
    private static String accessToken;

    private static VideoRecorder instance;
    private static VideoRecorder getInstance() {
        if (instance == null) {
            instance = new VideoRecorder();
        }
        return instance;
    }

    private QupaiService qupaiService;
    private VideoRecorder() {
    }
    private void init(Context context, RecordConfig config) {
        qupaiService = QupaiManager.getQupaiService(context);
        UISettings _UISettings = new UISettings() {
            @Override
            public boolean hasEditor() {
                // 是否需要编辑功能
                return false;
            }
            @Override
            public boolean hasImporter() {
                // 是否需要导入功能
                return false;
            }
            @Override
            public boolean hasGuide() {
                // 是否启动引导功能，建议用户第一次使用时设置为true
                return false;
            }
        };

        MovieExportOptions movie_options = new MovieExportOptions.Builder()
                //视频码率
                .setVideoBitrate(config.getVideoBitrate())
                .configureMuxer("movflags", "+faststart")
                .build();

        ProjectOptions projectOptions = new ProjectOptions.Builder()
                .setVideoSize(config.getVideoWidth(), config.getVideoHeight())
                .setVideoFrameRate(config.getVideoFrameRate())
                .setDurationRange(config.getVideoMinDuration(), config.getVideoMaxDuration())
                .get();

        ThumbnailExportOptions thumbnailExportOptions = new ThumbnailExportOptions.Builder()
                .setCount(1).get();

        VideoSessionCreateInfo.Builder infoBuilder = new VideoSessionCreateInfo.Builder()
                //摄像头方向,可配置前置或后置摄像头
                .setCameraFacing(Camera.CameraInfo.CAMERA_FACING_BACK)
                //美颜百分比
                .setBeautyProgress(80)
                //美颜是否默认开启
                .setBeautySkinOn(true)
                // 导出选项
                .setMovieExportOptions(movie_options)
                .setThumbnailExportOptions(thumbnailExportOptions);
        if (!TextUtils.isEmpty(config.getWaterMarkPath())) {
            //水印地址
            infoBuilder.setWaterMarkPath(config.getWaterMarkPath());
            //水印摆放位置
            infoBuilder.setWaterMarkPosition(1);
        }

        //初始化，建议在application里面做初始化
        qupaiService.initRecord(infoBuilder.build(), projectOptions, _UISettings);
    }

    /**
     * 初始化
     * Tips:建议只在application里面调用一次
     */
    public static void initialize(Context context) {
        RecordConfig config = new RecordConfig();
        initialize(context, config);
    }
    /**
     * 初始化
     * Tips:建议只在application里面调用一次
     */
    public static void initialize(Context context, RecordConfig config) {
        VideoRecorder recorder = getInstance();
        if (recorder.qupaiService == null) {
            recorder.init(context, config);

            AppGlobalSetting appGlobalSetting = new AppGlobalSetting(context);
            appGlobalSetting.saveGlobalConfigItem("qupai.minisdk.auth.error.code", 200);
            appGlobalSetting.saveGlobalConfigItem("qupai.minisdk.auth.error.message", APP_AUTH_SPACE);
        }
    }

    /**
     * 鉴权
     * Tips:建议只调用一次
     */
    public static void startAuth(Context context, String appkey, String appsecret) {
        AuthService service = AuthService.getInstance();
        service.setQupaiAuthListener(new QupaiAuthListener() {
            @Override
            public void onAuthError(int errorCode, String message) {
                Log.e("VideoRecorder", "ErrorCode=" + errorCode + ",message=" + message);
            }
            @Override
            public void onAuthComplte(int responseCode, String responseMessage) {
                Log.e("VideoRecorder", "onAuthComplte=" + responseCode + ",message=" + responseMessage);
                accessToken = responseMessage;
            }
        });
        service.startAuth(context, appkey, appsecret, APP_AUTH_SPACE);
    }

    /**
     * 检查是否合法
     */
    private void check(Context context) throws ExceptionInInitializerError, VerifyError {
        context = context.getApplicationContext();

        // check init
        if (qupaiService == null) {
            throw new ExceptionInInitializerError("插件没有初始化");
        }

        // check auth
        AppGlobalSetting appGlobalSetting = new AppGlobalSetting(context);
        int authCode = appGlobalSetting.getIntGlobalItem("qupai.minisdk.auth.error.code", -100);
        String authMessage = appGlobalSetting.getStringGlobalItem("qupai.minisdk.auth.error.message",
                context.getResources().getString(R.string.qupai_auth_first_toast));

        if(authCode != 200) {
            if(authCode > 1000 && authCode < 2000) {
            } else if(authCode <= 2000) {
                authMessage = context.getResources().getString(R.string.qupai_auth_first_toast);
            }
            throw new VerifyError("code:"+authCode+", message:"+authMessage);
        }
    }

    /**
     * 开始录制
     */
    public static void startRecord(Activity activity, int requestCode, boolean isShowGuide)
            throws ExceptionInInitializerError, VerifyError {
        VideoRecorder recorder = getInstance();
        recorder.check(activity);
        recorder.qupaiService.showRecordPage(activity, requestCode, isShowGuide);
    }
    /**
     * 开始录制
     */
    public static void startRecord(Fragment fragment, int requestCode, boolean isShowGuide)
            throws ExceptionInInitializerError, VerifyError{
        VideoRecorder recorder = getInstance();
        recorder.check(fragment.getActivity());
        recorder.qupaiService.showRecordPage(fragment, requestCode, isShowGuide);
    }

    /**
     * 获取录制结果
     * @param data
     * @return
     */
    public static RecordResult getRecordResult(Intent data) {
        if (data == null) {
            return null;
        }
        return new RecordResult(data);
    }

    /**
     * 删除录制草稿
     */
    public static void deleteDraft(Intent data) {
        RecordResult result = getRecordResult(data);

        final File videoFile = result.getVideoFile();
        final File thumFile = result.getThumbnailFile();

        File dir = result.getVideoFile().getParentFile();
        if(dir != null) {
            (new AsyncTask<File, File, Void>() {
                protected Void doInBackground(File... files) {
                    for(File file : files) {
                        if (videoFile != null && file.getName().equals(videoFile.getName())) {
                            continue;
                        }
                        if (thumFile != null && file.getName().equals(thumFile.getName())) {
                            continue;
                        }

                        FileUtils.deleteDirectory(file);
                    }
                    return null;
                }
            }).execute(dir.listFiles());
        }
    }

}
