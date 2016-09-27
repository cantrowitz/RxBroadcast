package com.cantrowitz.rxbroadcast;

import android.content.Context;
import android.content.Intent;

/**
 * Created by adamcantrowitz on 9/26/16.
 */

public interface OrderedBroadcastStrategy {

    /**
     * This method intends to handle user specific logic based on received Ordered Broadcasts and
     * deciding if it should abort the propagation (or not)
     *
     * @param context                     - Context of the BroadcastReceiver
     * @param intent                      - Intent received
     * @param broadcastReceiverAbortProxy - the object on which to operate
     */
    void handleOrderedBroadcast(
            Context context,
            Intent intent,
            BroadcastReceiverAbortProxy broadcastReceiverAbortProxy);

}
