package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import rx.AsyncEmitter;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by adamcantrowitz on 9/1/15.
 */
public class RxBroadcast {

    private static final OrderedBroadcastStrategy NO_OP_ORDERED_BROADCAST_STRATEGY = new
            OrderedBroadcastStrategy() {
                @Override
                public void handleOrderedBroadcast(Context context,
                                                   Intent intent,
                                                   BroadcastReceiverAbortProxy abortProxy) {
                    //no-op
                }
            };

    private RxBroadcast() {
        throw new AssertionError("No instances");
    }

    /**
     * Create {@link Observable} that wraps {@link BroadcastReceiver} and emits received intents.
     *
     * @param context      the context the {@link BroadcastReceiver} will be created from
     * @param intentFilter the filter for the particular intent
     * @return {@link Observable} of {@link Intent} that matches the filter
     */
    public static Observable<Intent> fromBroadcast(Context context, IntentFilter intentFilter) {
        return fromBroadcast(context, intentFilter, NO_OP_ORDERED_BROADCAST_STRATEGY);
    }

    /**
     * Create {@link Observable} that wraps {@link BroadcastReceiver} and emits received intents.
     * <p>
     * <em>This is only useful in conjunction with Ordered Broadcasts, e.g.,
     * {@link Context#sendOrderedBroadcast(Intent, String)}</em>
     *
     * @param context                  the context the {@link BroadcastReceiver} will be
     *                                 created from
     * @param intentFilter             the filter for the particular intent
     * @param orderedBroadcastStrategy the strategy to use for Ordered Broadcasts
     * @return {@link Observable} of {@link Intent} that matches the filter
     */
    public static Observable<Intent> fromBroadcast(
            Context context,
            IntentFilter intentFilter,
            OrderedBroadcastStrategy orderedBroadcastStrategy) {
        BroadcastRegistrar broadcastRegistrar = new BroadcastRegistrar(context, intentFilter);
        return createBroadcastObservable(broadcastRegistrar, orderedBroadcastStrategy);
    }

    /**
     * Create {@link Observable} that wraps {@link BroadcastReceiver} and emits received intents.
     *
     * @param context             the context the {@link BroadcastReceiver} will be created from
     * @param intentFilter        the filter for the particular intent
     * @param broadcastPermission String naming a permissions that a broadcaster must hold in
     *                            order to send an Intent to you. If null, no permission is
     *                            required.
     * @param handler             Handler identifying the thread that will receive the Intent. If
     *                            null, the main thread of the process will be used.
     * @return {@link Observable} of {@link Intent} that matches the filter
     */
    public static Observable<Intent> fromBroadcast(
            Context context,
            IntentFilter intentFilter,
            String broadcastPermission,
            Handler handler) {
        return fromBroadcast(
                context,
                intentFilter,
                broadcastPermission,
                handler,
                NO_OP_ORDERED_BROADCAST_STRATEGY
        );
    }

    /**
     * Create {@link Observable} that wraps {@link BroadcastReceiver} and emits received intents.
     * <p>
     * <em>This is only useful in conjunction with Ordered Broadcasts, e.g.,
     * {@link Context#sendOrderedBroadcast(Intent, String)}</em>
     *
     * @param context                  the context the {@link BroadcastReceiver} will be created
     *                                 from
     * @param intentFilter             the filter for the particular intent
     * @param broadcastPermission      String naming a permissions that a broadcaster must hold
     *                                 in order to send an Intent to you. If null, no permission
     *                                 is required.
     * @param handler                  Handler identifying the thread that will receive the
     *                                 Intent. If null, the main thread of the process will be used.
     * @param orderedBroadcastStrategy the strategy to use for Ordered Broadcasts
     * @return {@link Observable} of {@link Intent} that matches the filter
     */
    public static Observable<Intent> fromBroadcast(
            Context context,
            IntentFilter intentFilter,
            String broadcastPermission,
            Handler handler,
            OrderedBroadcastStrategy orderedBroadcastStrategy) {
        BroadcastWithPermissionsRegistrar broadcastWithPermissionsRegistrar =
                new BroadcastWithPermissionsRegistrar(
                        context,
                        intentFilter,
                        broadcastPermission,
                        handler);
        return createBroadcastObservable(
                broadcastWithPermissionsRegistrar,
                orderedBroadcastStrategy);
    }

    /**
     * Create {@link Observable} that wraps {@link BroadcastReceiver} and emits received intents.
     * <p>
     * This uses a {@link LocalBroadcastManager}
     *
     * @param context      the context the {@link BroadcastReceiver} will be created from
     * @param intentFilter the filter for the particular intent
     * @return {@link Observable} of {@link Intent} that matches the filter
     */
    public static Observable<Intent> fromLocalBroadcast(
            Context context,
            IntentFilter intentFilter) {
        LocalBroadcastRegistrar localBroadcastRegistrar = new LocalBroadcastRegistrar(
                intentFilter,
                LocalBroadcastManager.getInstance(context));
        return createBroadcastObservable(localBroadcastRegistrar, NO_OP_ORDERED_BROADCAST_STRATEGY);
    }

    private static Observable<Intent> createBroadcastObservable(
            final BroadcastRegistrarStrategy broadcastRegistrarStrategy,
            final OrderedBroadcastStrategy orderedBroadcastStrategy) {
        return Observable.fromEmitter(new Action1<AsyncEmitter<Intent>>() {
            @Override
            public void call(final AsyncEmitter<Intent> intentEmitter) {

                final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        intentEmitter.onNext(intent);

                        if (isOrderedBroadcast()) {
                            orderedBroadcastStrategy.handleOrderedBroadcast(
                                    context,
                                    intent,
                                    BroadcastReceiverAbortProxy.create(this));
                        }
                    }
                };

                intentEmitter.setCancellation(new AsyncEmitter.Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        broadcastRegistrarStrategy.unregisterBroadcastReceiver(broadcastReceiver);
                    }
                });

                broadcastRegistrarStrategy.registerBroadcastReceiver(broadcastReceiver);
            }
        }, AsyncEmitter.BackpressureMode.NONE);
    }
}
