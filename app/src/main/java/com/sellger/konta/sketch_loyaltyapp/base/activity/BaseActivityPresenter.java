package com.sellger.konta.sketch_loyaltyapp.base.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.sellger.konta.sketch_loyaltyapp.service.network.NetworkSchedulerService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseActivityPresenter implements BaseActivityContract.Presenter {

    @NonNull
    private BaseActivityContract.View view;

    private boolean mIsFirstNetworkCallback = true;

    BaseActivityPresenter(@NonNull BaseActivityContract.View view) {
        this.view = view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void scheduleNetworkJob(Context context) {
        JobInfo jobInfo = new JobInfo.Builder(0, new ComponentName(context, NetworkSchedulerService.class))
                .setRequiresCharging(true)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    @Override
    public void startNetworkIntentService(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent startServiceIntent = new Intent(context, NetworkSchedulerService.class);
            context.startService(startServiceIntent);
            new Handler().postDelayed(this::setUpNetworkObservable, 2000);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setUpNetworkObservable() {
        Observable<Boolean> observable = NetworkSchedulerService.getObservable();
        Observer<Boolean> onNetworkObserver = new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(Boolean isNetwork) {
                if (mIsFirstNetworkCallback) {
                    if (!isNetwork) {
                        view.displaySnackbar(isNetwork);
                    }
                    mIsFirstNetworkCallback = false;
                } else {
                    view.displaySnackbar(isNetwork);
                }
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        };

        observable.subscribe(onNetworkObserver);
    }
}
