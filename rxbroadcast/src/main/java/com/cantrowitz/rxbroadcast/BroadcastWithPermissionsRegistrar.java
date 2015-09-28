package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
class BroadcastWithPermissionsRegistrar implements BroadcastRegistrarStrategy {

    private final String permissions;
    private final Handler handler;
    private final Context context;
    private final IntentFilter intentFilter;

    BroadcastWithPermissionsRegistrar(Context context, IntentFilter intentFilter,
                                      String broadcastPermission, Handler handler) {
        this.context = context;
        this.intentFilter = intentFilter;
        this.permissions = broadcastPermission;
        this.handler = handler;
    }

    @Override
    public void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        context.registerReceiver(broadcastReceiver, intentFilter, permissions, handler);
    }

    @Override
    public void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        context.unregisterReceiver(broadcastReceiver);
    }
}
