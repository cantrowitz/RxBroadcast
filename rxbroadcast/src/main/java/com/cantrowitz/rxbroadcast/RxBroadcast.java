package com.cantrowitz.rxbroadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import rx.Observable;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
public class RxBroadcast {

    public static Observable<Intent> fromBroadcast(Context context, IntentFilter intentFilter) {
        return Observable.create(new GlobalBroadcastProvider(intentFilter, context));
    }

    public static Observable<Intent> fromBroadcast(Context context, IntentFilter intentFilter,
                                                   String broadcastPermission, Handler handler) {
        return Observable.create(new GlobalWPermissionsBroadcastProvider(intentFilter, context,
                broadcastPermission, handler));
    }

    public static Observable<Intent> fromLocalBroadcast(Context context, IntentFilter intentFilter) {
        return Observable.create(new LocalBroadcastProvider(intentFilter,
                LocalBroadcastManager.getInstance(context)));
    }
}
