package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by adamcantrowitz on 9/2/15.
 */
public class GlobalBroadcastProviderTest {
    @Mock
    Context context;
    @Mock
    IntentFilter intentFilter;
    @Mock
    Intent intent;
    BroadcastReceiver broadcastReceiver;

    BroadcastRegistrar testSubject;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(context.getApplicationContext()).thenReturn(context);
        testSubject = new BroadcastRegistrar(context, intentFilter);
    }

    @Test
    public void testRegisterBroadcastReceiver() throws Exception {
        testSubject.registerBroadcastReceiver(broadcastReceiver);
        verify(context).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Test
    public void testUnregisterBroadcastReceiver() throws Exception {
        testSubject.unregisterBroadcastReceiver(broadcastReceiver);
        verify(context).unregisterReceiver(broadcastReceiver);
    }

    @Test
    public void testSubscriptionLifecycle() {
        TestSubscriber<Intent> testSubscriber = new TestSubscriber<>();
        BroadcastProvider broadcastProvider = new BroadcastProvider(testSubject);
        Subscription subscribe = Observable.create(broadcastProvider)
                .subscribe(testSubscriber);
        broadcastReceiver = broadcastProvider.getBroadcastReceiver();

        verify(context).registerReceiver(eq(broadcastReceiver), eq(intentFilter));
        broadcastReceiver.onReceive(context, intent);
        subscribe.unsubscribe();
        verify(context).unregisterReceiver(broadcastReceiver);
        testSubscriber.assertValue(intent);
        testSubscriber.assertNoErrors();

    }
}