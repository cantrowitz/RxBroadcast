package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
class GlobalBroadcastProvider extends BroadcastProviderStrategy {

    protected final Context context;

    GlobalBroadcastProvider(IntentFilter intentFilter, Context context) {
        super(intentFilter);
        this.context = context.getApplicationContext();
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
