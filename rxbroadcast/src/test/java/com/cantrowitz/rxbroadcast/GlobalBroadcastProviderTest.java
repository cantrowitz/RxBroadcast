package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by adamcantrowitz on 9/2/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GlobalBroadcastProviderTest {
    @Mock
    Context context;
    @Mock
    IntentFilter intentFilter;
    @Mock
    Intent intent;
    @Mock
    BroadcastReceiver broadcastReceiver;

    @InjectMocks
    BroadcastRegistrar testSubject;

    @Before
    public void setUp() throws Exception {
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
}