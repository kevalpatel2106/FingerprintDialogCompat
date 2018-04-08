/*
 * Copyright 2018 Keval Patel
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance wit
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 *  the specific language governing permissions and limitations under the License.
 */

package com.kevalpatel2106.fingerprint_dialog_compat;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintDialog;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;

/**
 * Created by Keval on 07/04/18.
 * Builder for the fingerprint dialog. This builder will display the dialog based on the android version.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
public class FingerprintDialogBuilder {

    @NonNull
    private final Context mContext;

    private String mTitle;

    private String mSubTitle;

    private String mDescription;

    private String mButtonTitle;

    public FingerprintDialogBuilder(@NonNull final Context context) {
        mContext = context;
    }

    public FingerprintDialogBuilder setTitle(@NonNull final String title) {
        mTitle = title;
        return this;
    }

    public FingerprintDialogBuilder setTitle(@StringRes final int title) {
        mTitle = mContext.getString(title);
        return this;
    }

    public FingerprintDialogBuilder setSubtitle(@NonNull final String subtitle) {
        mSubTitle = subtitle;
        return this;
    }

    public FingerprintDialogBuilder setSubtitle(@StringRes final int subtitle) {
        mSubTitle = mContext.getString(subtitle);
        return this;
    }

    public FingerprintDialogBuilder setDescription(@NonNull final String description) {
        mDescription = description;
        return this;
    }

    public FingerprintDialogBuilder setDescription(@StringRes final int description) {
        mDescription = mContext.getString(description);
        return this;
    }

    public FingerprintDialogBuilder setNegativeButton(@Nullable final String text) {
        mButtonTitle = text;
        return this;
    }

    public FingerprintDialogBuilder setNegativeButton(@StringRes final int text) {
        mButtonTitle = mContext.getString(text);
        return this;
    }

    /**
     * Build the {@link FingerprintDialogCompatV23}. This dialog will be displayed for android version
     * between {@link android.os.Build.VERSION_CODES#M} to {@link android.os.Build.VERSION_CODES#O_MR1}.
     */
    public void show(FragmentManager fragmentManager, final AuthenticationCallback authenticationCallback) {

        //Validate the title
        if (mTitle == null) {
            throw new IllegalArgumentException("Title of the dialog cannot be null. Call setTitle() " +
                    "to set the title of the dialog.");
        }

        //Validate the subtitle
        if (mSubTitle == null) {
            throw new IllegalArgumentException("Subtitle of the dialog cannot be null. Call " +
                    "setSubtitle() to set the subtitle of the dialog.");
        }

        //Validate the description
        if (mDescription == null) {
            throw new IllegalArgumentException("Description of the dialog cannot be null. Call " +
                    "setDescription() to set the description of the dialog.");
        }

        if (mButtonTitle == null) {
            //Set the default button title
            mButtonTitle = mContext.getString(android.R.string.cancel);
        }

        //Check if the android version supports fingerprint authentication?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //Check if the device has the fingerprint sensor?
            if (FingerprintUtils.isSupportedHardware(mContext)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
                        || Build.VERSION.CODENAME.equals("P")/* TODO Remove once API 28 releases */) {
                    showFingerprintDialog(authenticationCallback);
                } else {
                    final FingerprintDialogCompatV23 fingerprintDialogCompat = FingerprintDialogCompatV23
                            .createDialog(mTitle, mSubTitle, mDescription, mButtonTitle);
                    fingerprintDialogCompat.setAuthenticationCallback(authenticationCallback);
                    fingerprintDialogCompat.show(fragmentManager, FingerprintDialogCompatV23.class.getName());
                }
                return;
            }
        }

        //Android device doesn't support fingerprint sensor.
        authenticationCallback.fingerprintAuthenticationNotSupported();
    }

    private void showFingerprintDialog(@NonNull final AuthenticationCallback authenticationCallback) {
        new FingerprintDialog.Builder()
                .setTitle(mTitle)
                .setSubtitle(mSubTitle)
                .setDescription(mDescription)
                .setNegativeButton(mButtonTitle,
                        mContext.getMainExecutor(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                authenticationCallback.authenticationCanceledByUser();
                            }
                        })
                .build(mContext)
                .authenticate(new CancellationSignal(),
                        mContext.getMainExecutor(),
                        new AuthenticationCallbackV28(authenticationCallback));
    }
}
