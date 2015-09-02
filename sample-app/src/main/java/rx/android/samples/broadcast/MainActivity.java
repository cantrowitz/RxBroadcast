package rx.android.samples.broadcast;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.TextView;

import com.cantrowitz.rxbroadcast.RxBroadcast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private final static String ACTION_TICK = MainActivity.class.getName() + "_ACTION_TICK";
    private final static String ACTION_VALUE_CHANGED = MainActivity.class.getName() + "_ACTION_VALUE_CHANGED";
    private final static String EXTRA_DATA = MainActivity.class.getName() + "_EXTRA_DATA";
    private final DateFormat timeFormat = SimpleDateFormat.getTimeInstance();

    private TextView localBroadcastTextView, globalBroadcastTextView;
    private Subscription localSubscription, globalSubscription;
    private Handler handler = new Handler();
    private LocalBroadcastManager localBroadcastManager;
    private boolean globalValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastTextView = (TextView) findViewById(android.R.id.text1);
        globalBroadcastTextView = (TextView) findViewById(android.R.id.text2);
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


        handler.postDelayed(timerRunnable, DateUtils.SECOND_IN_MILLIS);
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

    @Override
    protected void onStop() {
        super.onStop();

        if (localSubscription != null) {
            localSubscription.unsubscribe();
        }

        if (globalSubscription != null) {
            globalSubscription.unsubscribe();
        }
        handler.removeCallbacks(timerRunnable);
    }

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

            handler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
        }
    };
}
