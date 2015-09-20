package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscription;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by adamcantrowitz on 9/2/15.
 */
public class GlobalWPermissionsBroadcastProviderTest {
    @Mock
    Context context;
    @Mock
    IntentFilter intentFilter;
    @Mock
    BroadcastReceiver broadcastReceiver;
    @Mock
    Handler handler;

    GlobalWPermissionsBroadcastProviderRegistration testSubject;
    private static final String broadcastPermission = "broadcastPermission";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(context.getApplicationContext()).thenReturn(context);
        testSubject = new GlobalWPermissionsBroadcastProviderRegistration(intentFilter, context,
                broadcastPermission, handler);
    }

    @Test
    public void testUnregisterBroadcastReceiver() throws Exception {
        testSubject.unregisterBroadcastReceiver(broadcastReceiver);
        verify(context).unregisterReceiver(broadcastReceiver);
    }
    @Test
    public void testSubscriptionLifecycle(){
        BroadcastProvider broadcastProvider = new BroadcastProvider(testSubject);
        Subscription subscribe = Observable.create(broadcastProvider)
                .subscribe();
        verify(context).registerReceiver(any(BroadcastReceiver.class), eq(intentFilter),
                eq(broadcastPermission), eq(handler));
        subscribe.unsubscribe();
        verify(context).unregisterReceiver(any(BroadcastReceiver.class));
    }

    @Test
    public void testRegisterBroadcastReceiver() throws Exception {
        testSubject.registerBroadcastReceiver(broadcastReceiver);
        verify(context).registerReceiver(broadcastReceiver,intentFilter,broadcastPermission,handler);
    }
}