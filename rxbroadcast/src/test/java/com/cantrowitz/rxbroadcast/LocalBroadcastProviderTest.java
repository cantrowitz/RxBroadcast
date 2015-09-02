package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscription;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Created by adamcantrowitz on 9/2/15.
 */
public class LocalBroadcastProviderTest {

    @Mock
    LocalBroadcastManager localBroadcastManager;
    @Mock
    IntentFilter intentFilter;
    @Mock
    BroadcastReceiver broadcastReceiver;

    private LocalBroadcastProvider testSubject;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testSubject = new LocalBroadcastProvider(intentFilter, localBroadcastManager);
    }

    @Test
    public void testRegisterBroadcastReceiver() throws Exception {
        testSubject.registerBroadcastReceiver(broadcastReceiver);
        verify(localBroadcastManager).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Test
    public void testUnregisterBroadcastReceiver() throws Exception {
        testSubject.unregisterBroadcastReceiver(broadcastReceiver);
        verify(localBroadcastManager).unregisterReceiver(broadcastReceiver);
    }

    @Test
    public void testSubscriptionLifecycle() {
        Subscription subscribe = Observable.create(testSubject)
                .subscribe();
        verify(localBroadcastManager).registerReceiver(any(BroadcastReceiver.class), eq(intentFilter));
        subscribe.unsubscribe();
        verify(localBroadcastManager).unregisterReceiver(any(BroadcastReceiver.class));
    }
}