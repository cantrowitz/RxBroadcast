package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
class LocalBroadcastProviderRegistration implements BroadcastReceiverRegistrationStrategy {
    final LocalBroadcastManager localBroadcastManager;
    private final IntentFilter intentFilter;

    LocalBroadcastProviderRegistration(IntentFilter intentFilter, LocalBroadcastManager localBroadcastManager) {
        this.intentFilter = intentFilter;
        this.localBroadcastManager = localBroadcastManager;
    }

    @Override
    public void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
