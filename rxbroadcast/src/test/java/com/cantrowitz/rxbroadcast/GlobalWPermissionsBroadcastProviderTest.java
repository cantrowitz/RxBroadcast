package com.cantrowitz.rxbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by adamcantrowitz on 9/2/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GlobalWPermissionsBroadcastProviderTest {
    @Mock
    Context context;
    @Mock
    IntentFilter intentFilter;
    @Mock
    BroadcastReceiver broadcastReceiver;
    @Mock
    Handler handler;

    BroadcastWithPermissionsRegistrar testSubject;
    private static final String broadcastPermission = "broadcastPermission";

    @Before
    public void setUp() throws Exception {
        when(context.getApplicationContext()).thenReturn(context);
        testSubject = new BroadcastWithPermissionsRegistrar(context, intentFilter,
                broadcastPermission, handler);
    }

    @Test
    public void testUnregisterBroadcastReceiver() throws Exception {
        testSubject.unregisterBroadcastReceiver(broadcastReceiver);
        verify(context).unregisterReceiver(broadcastReceiver);
    }

    @Test
    public void testRegisterBroadcastReceiver() throws Exception {
        testSubject.registerBroadcastReceiver(broadcastReceiver);
        verify(context).registerReceiver(broadcastReceiver,intentFilter,broadcastPermission,handler);
    }
}