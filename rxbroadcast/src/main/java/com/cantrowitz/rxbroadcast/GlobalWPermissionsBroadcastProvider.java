package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
class GlobalWPermissionsBroadcastProvider extends GlobalBroadcastProvider {

    private final String permissions;
    private final Handler handler;

    GlobalWPermissionsBroadcastProvider(IntentFilter intentFilter, Context context,
                                        String broadcastPermission, Handler handler) {
        super(intentFilter,context);
        this.permissions = broadcastPermission;
        this.handler = handler;
    }

    @Override
    protected void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        context.registerReceiver(broadcastReceiver, intentFilter,permissions, handler);
    }

}
