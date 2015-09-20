package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;

/**
 * Created by adamcantrowitz on 9/20/15.
 */
interface BroadcastReceiverRegistrationStrategy {
    void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver);

    void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver);
}
