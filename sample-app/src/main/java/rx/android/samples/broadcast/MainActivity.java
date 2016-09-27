package rx.android.samples.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.TextView;

import com.cantrowitz.rxbroadcast.BroadcastReceiverAbortProxy;
import com.cantrowitz.rxbroadcast.OrderedBroadcastAbortStrategy;
import com.cantrowitz.rxbroadcast.RxBroadcast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private final static String ACTION_TICK = MainActivity.class.getName() + "_ACTION_TICK";
    private final static String ACTION_PRIORITY = MainActivity.class.getName() + "_ACTION_PRIORITY";
    private final static String ACTION_VALUE_CHANGED = MainActivity.class.getName() +
            "_ACTION_VALUE_CHANGED";
    private final static String EXTRA_DATA = MainActivity.class.getName() + "_EXTRA_DATA";
    private final DateFormat timeFormat = SimpleDateFormat.getTimeInstance();

    private TextView localBroadcastTextView, globalBroadcastTextView, hiPriTextView, loPriTextView;
    private Subscription localSubscription, globalSubscription, hiPriSubscription,
            loPriSubscription;
    private Handler handler = new Handler();
    private LocalBroadcastManager localBroadcastManager;
    private boolean globalValue = false;
    private int priValue = 0;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            Intent localIntent = new Intent(ACTION_TICK);
            localIntent.putExtra(EXTRA_DATA, System.currentTimeMillis());
            localBroadcastManager.sendBroadcast(localIntent);

            globalValue = !globalValue;
            Intent globalIntent = new Intent(ACTION_VALUE_CHANGED);
            globalIntent.putExtra(EXTRA_DATA, globalValue);
            MainActivity.this.sendBroadcast(globalIntent);

            Intent priorityIntent = new Intent(ACTION_PRIORITY);
            priorityIntent.putExtra(EXTRA_DATA, priValue++);
            MainActivity.this.sendOrderedBroadcast(priorityIntent, null);

            handler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastTextView = (TextView) findViewById(android.R.id.text1);
        globalBroadcastTextView = (TextView) findViewById(android.R.id.text2);
        hiPriTextView = (TextView) findViewById(R.id.hi_pri);
        loPriTextView = (TextView) findViewById(R.id.low_pri);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        localSubscription = getLocalBroadcastObservable()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        setLocalBroadcastTextView(s);
                    }
                });

        globalSubscription = getGlobalBroadcastObservable()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        setGlobalBroadcastTextView(aBoolean);
                    }
                });

        hiPriSubscription = getHiPriBroadcastObservable()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        setHiPriTextView(integer);
                    }
                });

        loPriSubscription = getLoPriBroadcastObservable()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        setLoPriTextView(integer);
                    }
                });


        handler.postDelayed(timerRunnable, DateUtils.SECOND_IN_MILLIS);
    }

    private void setLoPriTextView(Integer integer) {
        loPriTextView.setText(String.format(Locale.US, "Value is %d", integer));
    }

    private void setHiPriTextView(Integer integer) {
        hiPriTextView.setText(String.format(Locale.US, "Value is %d", integer));
    }

    private void setGlobalBroadcastTextView(Boolean aBoolean) {
        globalBroadcastTextView.setText(aBoolean.toString());
    }

    private void setLocalBroadcastTextView(String s) {
        localBroadcastTextView.setText(s);
    }

    @NonNull
    private Observable<Boolean> getGlobalBroadcastObservable() {
        return RxBroadcast.fromBroadcast(this, new IntentFilter(ACTION_VALUE_CHANGED))
                .map(new Func1<Intent, Boolean>() {
                    @Override
                    public Boolean call(Intent intent) {
                        return intent.getBooleanExtra(EXTRA_DATA, false);
                    }
                });
    }

    @NonNull
    private Observable<String> getLocalBroadcastObservable() {
        return RxBroadcast.fromLocalBroadcast(this, new IntentFilter(ACTION_TICK))
                .map(new Func1<Intent, Long>() {
                    @Override
                    public Long call(Intent intent) {
                        return intent.getLongExtra(EXTRA_DATA, 0);
                    }
                })
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return timeFormat.format(new Date(aLong));
                    }
                });
    }

    private Observable<Integer> getHiPriBroadcastObservable() {
        IntentFilter intentFilter = new IntentFilter(ACTION_PRIORITY);
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

        final OrderedBroadcastAbortStrategy allowMultiplesOfFive = new
                OrderedBroadcastAbortStrategy() {
                    @Override
                    public void handleOrderedBroadcast(Context context, Intent intent,
                                                       BroadcastReceiverAbortProxy
                                                               broadcastReceiverAbortProxy) {
                        int value = intent.getIntExtra(EXTRA_DATA, 0);
                        if (value % 5 == 0) {
                            broadcastReceiverAbortProxy.clearAbortBroadcast();
                        } else {
                            broadcastReceiverAbortProxy.abortBroadcast();
                        }

                    }
                };
        return RxBroadcast.fromBroadcast(
                this,
                intentFilter,
                allowMultiplesOfFive)
                .map(new Func1<Intent, Integer>() {
                    @Override
                    public Integer call(Intent intent) {
                        return intent.getIntExtra(EXTRA_DATA, 0);
                    }
                });
    }

    private Observable<Integer> getLoPriBroadcastObservable() {
        IntentFilter intentFilter = new IntentFilter(ACTION_PRIORITY);
        intentFilter.setPriority(IntentFilter.SYSTEM_LOW_PRIORITY);

        return RxBroadcast.fromBroadcast(this, intentFilter)
                .map(new Func1<Intent, Integer>() {
                    @Override
                    public Integer call(Intent intent) {
                        return intent.getIntExtra(EXTRA_DATA, 0);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (localSubscription != null) {
            localSubscription.unsubscribe();
        }

        if (globalSubscription != null) {
            globalSubscription.unsubscribe();
        }

        if (hiPriSubscription != null) {
            hiPriSubscription.unsubscribe();
        }

        if (loPriSubscription != null) {
            loPriSubscription.unsubscribe();
        }
        handler.removeCallbacks(timerRunnable);
    }
}
