package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
class LocalBroadcastProvider extends BroadcastProviderStrategy {
    final LocalBroadcastManager localBroadcastManager;

    LocalBroadcastProvider(Context context, IntentFilter intentFilter) {
        super(context, intentFilter);
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @Override
    protected void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
