/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sellger.konta.sketch_loyaltyapp.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.local.LoyaltyLocalDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.local.LoyaltyRoomDatabase;
import com.sellger.konta.sketch_loyaltyapp.data.remote.LoyaltyRemoteDataSource;
import com.sellger.konta.sketch_loyaltyapp.utils.AppExecutors;

/**
 * Enables injection of mock implementations for
 * {@link LoyaltyDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static LoyaltyRepository provideLoyaltyRepository(@NonNull Context context) {
        LoyaltyRoomDatabase database = LoyaltyRoomDatabase.getDatabase(context);
        return LoyaltyRepository.getInstance(LoyaltyRemoteDataSource.getInstance(),
                LoyaltyLocalDataSource.getInstance(new AppExecutors(), database.menuDao(),
                        database.productDao(), database.couponDao(), database.markerDao(),
                        database.openHourDao(), database.pageDao()));
    }
}
