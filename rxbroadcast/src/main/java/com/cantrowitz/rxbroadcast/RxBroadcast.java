package com.cantrowitz.rxbroadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import rx.Observable;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
public class RxBroadcast {

    public static Observable<Intent> fromBroadcast(Context context, IntentFilter intentFilter) {
        return Observable.create(new GlobalBroadcastProvider(context, intentFilter));
    }

    public static Observable<Intent> fromLocalBroadcast(Context context, IntentFilter intentFilter) {
        return Observable.create(new LocalBroadcastProvider(context, intentFilter));
    }
}
