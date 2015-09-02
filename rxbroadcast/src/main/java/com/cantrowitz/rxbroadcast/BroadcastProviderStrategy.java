package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
 abstract class BroadcastProviderStrategy implements Observable.OnSubscribe<Intent> {
    protected final Context context;
    protected final IntentFilter intentFilter;

    BroadcastProviderStrategy(Context context, IntentFilter intentFilter) {
        this.context = context.getApplicationContext();
        this.intentFilter = intentFilter;
    }

    @Override
    public void call(final Subscriber<? super Intent> subscriber) {

        final BroadcastReceiver broadcastReceiver = createBroadcastReceiver(subscriber);

        final Subscription subscription = Subscriptions.create(new Action0() {
            @Override
            public void call() {
                unregisterBroadcastReceiver(broadcastReceiver);
            }
        });

        subscriber.add(subscription);
        registerBroadcastReceiver(broadcastReceiver);
    }

    protected abstract void registerBroadcastReceiver(BroadcastReceiver broadcastReceiver);

    protected abstract void unregisterBroadcastReceiver(BroadcastReceiver broadcastReceiver);

    @NonNull
    private BroadcastReceiver createBroadcastReceiver(final Subscriber<? super Intent> subscriber) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                subscriber.onNext(intent);
            }
        };
    }
}
