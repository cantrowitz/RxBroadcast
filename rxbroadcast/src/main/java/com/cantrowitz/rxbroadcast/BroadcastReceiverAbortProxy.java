package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;

/**
 * Created by adamcantrowitz on 9/26/16.
 */
public class BroadcastReceiverAbortProxy {
    private final BroadcastReceiver broadcastReceiver;

    private BroadcastReceiverAbortProxy(BroadcastReceiver broadcastReceiver) {
        this.broadcastReceiver = broadcastReceiver;
    }

    static BroadcastReceiverAbortProxy create(final BroadcastReceiver broadcastReceiver) {
        return new BroadcastReceiverAbortProxy(broadcastReceiver);
    }

    /**
     * Calls {@link BroadcastReceiver#abortBroadcast()}
     */
    public void abortBroadcast() {
        broadcastReceiver.abortBroadcast();
    }

    /**
     * Calls {@link BroadcastReceiver#clearAbortBroadcast()}
     */
    public void clearAbortBroadcast() {
        broadcastReceiver.clearAbortBroadcast();
    }

    /**
     * Calls {@link BroadcastReceiver#getAbortBroadcast()}
     *
     * @return the value of {@link BroadcastReceiver#getAbortBroadcast()}
     */
    public boolean getAbortBroadcast() {
        return broadcastReceiver.getAbortBroadcast();
    }
}
