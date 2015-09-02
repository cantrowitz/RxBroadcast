package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscription;

import static org.mockito.Matchers.any;
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
    BroadcastReceiver broadcastReceiver;

    GlobalBroadcastProvider testSubject;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(context.getApplicationContext()).thenReturn(context);
        testSubject = new GlobalBroadcastProvider(intentFilter, context);
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
    public void testUnregisterBroadcastReceiver_isCalled_whenUnsubscribed(){
        Subscription subscribe = Observable.create(testSubject)
                .subscribe();
        subscribe.unsubscribe();
        verify(context).unregisterReceiver(any(BroadcastReceiver.class));
    }
}