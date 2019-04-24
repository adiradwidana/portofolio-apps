/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.base.app.domain.utils.fcm;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.base.app.domain.utils.Logger;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.base.app.R;
import com.base.app.presentation.home.main.MainActivity;

import java.util.Map;

import timber.log.Timber;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final String PARAM_BODY = "body";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_ID_TRANSACTION = "id_transaksi";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.d("Pongodev-LOG", "FirebaseMessagingService:onMessageReceived1 -> " + remoteMessage.getData());
        Logger.d("Pongodev-LOG", "FirebaseMessagingService:onMessageReceived2 -> " + remoteMessage.toString());

        Map<String, String> data = remoteMessage.getData();
        /*if (data.size() > 0) {
            TimberLogger.d("%s -> %s\n" +
                            "%s -> %s\n" +
                            "%s -> %s\n" +
                            "%s -> %s\n" +
                            "%s -> %s\n" +
                            "%s -> %s\n" +
                            "%s -> %s\n" +
                            "%s -> %s\n" +
                            "%s -> %s\n" +
                            "%s -> %s\n",
                    PARAM_TYPE, data.get(PARAM_TYPE),
                    PARAM_TITLE, data.get(PARAM_TITLE),
                    PARAM_BODY, data.get(PARAM_BODY),
                    PARAM_COMIC_ALIAS, data.get(PARAM_COMIC_ALIAS),
                    PARAM_CHAPTER_ALIAS, data.get(PARAM_CHAPTER_ALIAS),
                    PARAM_IS_SHORT, data.get(PARAM_IS_SHORT),
                    PARAM_SOUND, data.get(PARAM_SOUND),
                    PARAM_IMAGE, data.get(PARAM_IMAGE),
                    PARAM_COMMENT_ID, data.get(PARAM_COMMENT_ID),
                    PARAM_REFERENCE, data.get(PARAM_REFERENCE)
            );*/
        sendNotification(data);
    }

    private void sendNotification(Map<String, String> data) {
        Bundle bundle = new Bundle();
        Intent intent;
        String channelId = getString(R.string.default_notification_channel_id);

        String type = data.get(PARAM_TYPE);
        int typeInt = -1;

        try {
            typeInt = Integer.parseInt(type);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        if (typeInt == 1) {
            intent = new Intent(this, MainActivity.class);

        } else {
            bundle.putString(PARAM_ID_TRANSACTION, data.get(PARAM_ID_TRANSACTION));
            intent = new Intent(this, MainActivity.class);
        }

        Logger.d("Pongodev-LOG", "FirebaseMessagingService:sendNotification -> "+typeInt);

        bundle.putInt(PARAM_TYPE, typeInt);

        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // todo notification ID ux
        int randomNumber = (int) (Math.random() * Integer.MAX_VALUE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, randomNumber, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setColor(ContextCompat.getColor(this, R.color.red_A100))
                        .setContentTitle(data.get(PARAM_TITLE))
                        .setContentText(data.get(PARAM_BODY))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(randomNumber, notificationBuilder.build());
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     * <p>
     * //* @param data FCM message body received.
     */
    /*private void sendNotification(Map<String, String> data) {
     *//*Intent intent;
        Bundle bundle = new Bundle();
        String type = data.get(PARAM_TYPE);
        int typeInt = -1;
        String channelId = getString(R.string.notification_channel_id_default);

        try {
            typeInt = Integer.parseInt(type);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        switch (typeInt) {
            case NOTIFICATION_TYPE_HOME:
                intent = new Intent(this, MainActivity.class);
                break;
            case NOTIFICATION_TYPE_COMIC:
                boolean isShort = Boolean.parseBoolean(data.get(PARAM_IS_SHORT));
                bundle.putString(ChapterListActivity.EXTRA_COMIC_ALIAS, data.get(PARAM_COMIC_ALIAS));
                bundle.putString(ChapterListActivity.EXTRA_COMIC_TYPE, Comic.TYPE_COMIC);
                if (isShort) {
                    intent = new Intent(this, ReadPageActivity.class);
                    bundle.putString(ReadPageActivity.EXTRA_CHAPTER_ALIAS, Comic.ONESHOT_CHAPTER_ALIAS);
                    bundle.putBoolean(ReadPageActivity.EXTRA_HEADER_ENABLED, true);
                } else {
                    intent = new Intent(this, ChapterListActivity.class);
                    bundle.putString(ChapterListActivity.EXTRA_COMIC_TITLE, "");
                }
                channelId = getString(R.string.notification_channel_id_comic_update);
                break;
            case NOTIFICATION_TYPE_COMIC_CHAPTER:
                String chapterAlias = data.get(PARAM_CHAPTER_ALIAS);
                intent = new Intent(this, ReadPageActivity.class);
                bundle.putString(ReadPageActivity.EXTRA_COMIC_ALIAS, data.get(PARAM_COMIC_ALIAS));
                bundle.putString(ReadPageActivity.EXTRA_CHAPTER_ALIAS, chapterAlias);
                bundle.putString(ReadPageActivity.EXTRA_COMIC_TYPE, Comic.TYPE_COMIC);
                bundle.putBoolean(ReadPageActivity.EXTRA_HEADER_ENABLED, Comic.ONESHOT_CHAPTER_ALIAS.equals(chapterAlias));
                channelId = getString(R.string.notification_channel_id_subscription_update);
                break;
            case NOTIFICATION_TYPE_UPDATE:
                intent = new Intent(this, MainActivity.class);
                break;
            case NOTIFICATION_TYPE_CHALLENGE:
                boolean isChallengeShort = Boolean.parseBoolean(data.get(PARAM_IS_SHORT));
                bundle.putString(ChapterListActivity.EXTRA_COMIC_ALIAS, data.get(PARAM_COMIC_ALIAS));
                bundle.putString(ChapterListActivity.EXTRA_COMIC_TYPE, Comic.TYPE_CHALLENGE);
                if (isChallengeShort) {
                    intent = new Intent(this, ReadPageActivity.class);
                    bundle.putString(ReadPageActivity.EXTRA_CHAPTER_ALIAS, Comic.ONESHOT_CHAPTER_ALIAS);
                    bundle.putBoolean(ReadPageActivity.EXTRA_HEADER_ENABLED, true);
                } else {
                    intent = new Intent(this, ChapterListActivity.class);
                    bundle.putString(ChapterListActivity.EXTRA_COMIC_TITLE, "");
                }
                channelId = getString(R.string.notification_channel_id_comic_update);
                break;
            case NOTIFICATION_TYPE_CHALLENGE_CHAPTER:
                String chapterChallengeAlias = data.get(PARAM_CHAPTER_ALIAS);
                intent = new Intent(this, ReadPageActivity.class);
                bundle.putString(ReadPageActivity.EXTRA_COMIC_ALIAS, data.get(PARAM_COMIC_ALIAS));
                bundle.putString(ReadPageActivity.EXTRA_CHAPTER_ALIAS, chapterChallengeAlias);
                bundle.putString(ReadPageActivity.EXTRA_COMIC_TYPE, Comic.TYPE_CHALLENGE);
                bundle.putBoolean(ReadPageActivity.EXTRA_HEADER_ENABLED, Comic.ONESHOT_CHAPTER_ALIAS.equals(chapterChallengeAlias));
                channelId = getString(R.string.notification_channel_id_subscription_update);
                break;
            case NOTIFICATION_TYPE_COMMENT:
                intent = new Intent(this, CommentActivity.class);
                bundle.putString(CommentActivity.EXTRA_COMIC_ALIAS, data.get(PARAM_COMIC_ALIAS));
                bundle.putString(CommentActivity.EXTRA_CHAPTER_ALIAS, data.get(PARAM_CHAPTER_ALIAS));
                bundle.putString(CommentActivity.EXTRA_PARENT_ID, data.get(PARAM_COMMENT_ID));
                channelId = getString(R.string.notification_channel_id_comment);
                break;
            case NOTIFICATION_TYPE_LINK:
                String link = data.get(PARAM_REFERENCE);

                if (Boolean.valueOf(data.get(PARAM_IS_EXTERNAL))) {
                    Uri webpage = Uri.parse(link);
                    intent = new Intent(Intent.ACTION_VIEW, webpage);
                } else {
                    intent = new Intent(this, SingleFragmentActivity.class);
                    bundle.putInt(SingleFragmentActivity.ARG_TYPE, SingleFragmentActivity.TYPE_WEB);
                    bundle.putString(SingleFragmentActivity.ARG_TITLE, "");
                    bundle.putString(SingleFragmentActivity.ARG_REFERENCE, link);
                }
                break;
            case NOTIFICATION_TYPE_MENU:
                String menu = data.get(PARAM_REFERENCE);
                intent = new Intent(this, MainActivity.class);
                bundle.putString(PARAM_REFERENCE, menu);
                break;
            default:
                CiayoAnalytics.logInvalidNotificationDataEvent(this, data.toString());
                return;
        }

        String title = data.get(PARAM_TITLE);
        String body = data.get(PARAM_BODY);

        bundle.putInt(CiayoAnalytics.PARAM_TYPE, typeInt);
        bundle.putString(CiayoAnalytics.PARAM_CONTENT, String.format(ANALYTIC_CONTENT_FORMAT, title, body));

        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
*//*
        // todo notification ID ux
        *//*int randomNumber = (int) (Math.random() * Integer.MAX_VALUE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, randomNumber, intent,
                PendingIntent.FLAG_ONE_SHOT);*//*

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification_white)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(ContextCompat.getColor(this, R.color.red))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(randomNumber, notificationBuilder.build());
    }*/
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String refreshedToken = instanceIdResult.getToken();
            Timber.d("onTokenRefresh -> %s", refreshedToken);

            //sendRegistrationToServer(refreshedToken);
            /* pass the token to the AppsFlyer SDK */
        });
    }
}
