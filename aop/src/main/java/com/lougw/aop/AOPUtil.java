package com.lougw.aop;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.lougw.aop.db.StopWatchDataBaseIml;


/**
 *
 */

public class AOPUtil {
    private static final String TAG = AOPUtil.class.getSimpleName();
    /**
     * 线程管理
     */
    private static final int MSG_DO_DOWNLOAD = 1;
    private static Context mContext;
    private static StopWatchDataBaseIml mDataBaseIml;
    private static HandlerThread mAOPManagerThread;
    private static AOPHandler mAOPManagerHandler;

    private static class AopUtilHolder {
        private static final AOPUtil instance = new AOPUtil();
    }

    private AOPUtil() {
    }

    public static AOPUtil getInstance() {
        return AopUtilHolder.instance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mDataBaseIml = new StopWatchDataBaseIml(mContext);
        mAOPManagerThread = new HandlerThread("aop manager thread");
        mAOPManagerThread.start();
        mAOPManagerHandler = new AOPHandler(mAOPManagerThread.getLooper());
    }

    class AOPHandler extends Handler {

        public AOPHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DO_DOWNLOAD:
                    doSave((StopWatch) msg.obj);
                    break;
            }
        }
    }


    public void save(StopWatch info) {
        if (mContext == null || info == null) {
            return;
        }
        if (mAOPManagerHandler != null && mAOPManagerThread.isAlive()) {
            mAOPManagerHandler.sendMessage(mAOPManagerHandler.obtainMessage(MSG_DO_DOWNLOAD, info));
        }
    }

    private void doSave(StopWatch info) {
        try {
            mDataBaseIml.insert(info);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return mContext.getResources();
    }

    /**
     * 跟距id獲得字符串
     *
     * @param resId
     * @return
     */
    public static String getResourceEntryName(int resId) {
        try {
            return getResources().getResourceEntryName(resId);
        } catch (Exception e) {
            return "";
        }
    }
}