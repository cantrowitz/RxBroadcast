package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by adamcantrowitz on 9/2/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class LocalBroadcastProviderTest {

    @Mock
    LocalBroadcastManager localBroadcastManager;
    @Mock
    IntentFilter intentFilter;
    @Mock
    Context context;
    @Mock
    Intent intent;

    BroadcastReceiver broadcastReceiver;

    private LocalBroadcastRegistrar testSubject;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testSubject = new LocalBroadcastRegistrar(intentFilter, localBroadcastManager);
    }

    @Test
    public void testRegisterBroadcastReceiver() throws Exception {
        BroadcastReceiver mockReceiver = mock(BroadcastReceiver.class);
        testSubject.registerBroadcastReceiver(mockReceiver);
        verify(localBroadcastManager).registerReceiver(mockReceiver, intentFilter);
    }

    @Test
    public void testUnregisterBroadcastReceiver() throws Exception {
        BroadcastReceiver mockReceiver = mock(BroadcastReceiver.class);
        testSubject.unregisterBroadcastReceiver(mockReceiver);
        verify(localBroadcastManager).unregisterReceiver(mockReceiver);
    }

    @Test
    public void testSubscriptionLifecycle() {
        TestSubscriber<Intent> testSubscriber = new TestSubscriber<>();
        BroadcastProvider broadcastProvider = new BroadcastProvider(testSubject);
        Subscription subscribe = Observable.create(broadcastProvider)
                .subscribe(testSubscriber);
        broadcastReceiver = broadcastProvider.getBroadcastReceiver();
        verify(localBroadcastManager).registerReceiver(eq(broadcastReceiver), eq(intentFilter));
        broadcastReceiver.onReceive(context, intent);
        subscribe.unsubscribe();
        verify(localBroadcastManager).unregisterReceiver(broadcastReceiver);
        testSubscriber.assertValue(intent);
        testSubscriber.assertNoErrors();
    }
}