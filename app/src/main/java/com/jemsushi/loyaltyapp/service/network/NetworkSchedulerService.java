package com.jemsushi.loyaltyapp.service.network;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.jemsushi.loyaltyapp.Constants.CONNECTIVITY_ACTION;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkSchedulerService extends JobService implements
        NetworkStateReceiver.ConnectivityReceiverListener {

    private static final String TAG = NetworkSchedulerService.class.getSimpleName();

    private NetworkStateReceiver mNetworkStateReceiver;
    private static PublishSubject<Boolean> mIsNetworkConnected;

    @Override
    public void onCreate() {
        super.onCreate();
        mNetworkStateReceiver = new NetworkStateReceiver(this);
        mIsNetworkConnected = PublishSubject.create();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        registerReceiver(mNetworkStateReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        unregisterReceiver(mNetworkStateReceiver);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        mIsNetworkConnected.onNext(isConnected);
    }

    public static Observable<Boolean> getObservable() {
        return mIsNetworkConnected;
    }
}
