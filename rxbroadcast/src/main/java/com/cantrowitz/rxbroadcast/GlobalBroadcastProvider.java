package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
class GlobalBroadcastProvider extends BroadcastProviderStrategy {

    GlobalBroadcastProvider(Context context, IntentFilter intentFilter) {
        super(context, intentFilter);
    }

    @Override
    protected void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        context.unregisterReceiver(broadcastReceiver);
    }
}
