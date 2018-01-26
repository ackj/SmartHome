package cn.itsite.apush.broadcast;

import android.content.Context;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

import cn.itsite.abase.log.ALog;
import cn.itsite.apush.NoticeHelper;

/**
 * Created by leguang on 2017/9/22 0022.
 * Email：langmanleguang@qq.com
 */

public class AliyunPushReceiver extends MessageReceiver {
    public static final String TAG = AliyunPushReceiver.class.getSimpleName();

    @Override
    public void onNotification(Context context, final String title, final String summary, final Map<String, String> extraMap) {
        ALog.e("onNotification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
    }

    @Override
    public void onMessage(Context context, CPushMessage message) {
        ALog.e("onMessage, messageId: " + message.getMessageId() + ", title: " + message.getTitle() + ", content:" + message.getContent());
        NoticeHelper.notification(context, message.getContent());
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        ALog.e("onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        ALog.e("onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        ALog.e("onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        ALog.e("onNotificationRemoved, messageId:" + messageId);
    }


}