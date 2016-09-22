package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Created by adamcantrowitz on 9/2/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class LocalBroadcastProviderTest {

    @Mock
    LocalBroadcastManager localBroadcastManager;
    @Mock
    IntentFilter intentFilter;
    @Mock
    Context context;
    @Mock
    Intent intent;
    @Mock
    BroadcastReceiver broadcastReceiver;

    @InjectMocks
    private LocalBroadcastRegistrar testSubject;

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
}