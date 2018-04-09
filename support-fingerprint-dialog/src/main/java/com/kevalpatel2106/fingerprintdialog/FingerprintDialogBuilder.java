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

package com.kevalpatel2106.fingerprintdialog;

import android.annotation.TargetApi;
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
@SuppressWarnings("WeakerAccess")
public class FingerprintDialogBuilder {

    /**
     * {@link Context} of the caller.
     */
    @NonNull
    private final Context mContext;

    /**
     * Title of fingerprint dialog.
     */
    private String mTitle;

    /**
     * Subtitle of fingerprint dialog.
     */
    private String mSubTitle;

    /**
     * Description of fingerprint dialog.
     */
    private String mDescription;

    /**
     * Title to display on the negative button of fingerprint dialog.
     */
    private String mButtonTitle;

    /**
     * Public constructor.
     *
     * @param context {@link Context} of the caller.
     */
    public FingerprintDialogBuilder(@NonNull final Context context) {
        mContext = context;
    }

    /**
     * Set the title of the dialog. This is the required field.
     *
     * @param title Title string.
     * @return {@link FingerprintDialogBuilder}
     * @see #setTitle(int)
     */
    public FingerprintDialogBuilder setTitle(@NonNull final String title) {
        mTitle = title;
        return this;
    }

    /**
     * Set the title of the dialog. This is the required field.
     *
     * @param title String resource of the title.
     * @return {@link FingerprintDialogBuilder}
     * @see #setTitle(String)
     */
    public FingerprintDialogBuilder setTitle(@StringRes final int title) {
        mTitle = mContext.getString(title);
        return this;
    }

    /**
     * Set the subtitle of the dialog. This is the required field.
     *
     * @param subtitle Subtitle string.
     * @return {@link FingerprintDialogBuilder}
     * @see #setSubtitle(int)
     */
    public FingerprintDialogBuilder setSubtitle(@NonNull final String subtitle) {
        mSubTitle = subtitle;
        return this;
    }

    /**
     * Set the subtitle of the dialog. This is the required field.
     *
     * @param subtitle String resource of the subtitle.
     * @return {@link FingerprintDialogBuilder}
     * @see #setSubtitle(String)
     */
    public FingerprintDialogBuilder setSubtitle(@StringRes final int subtitle) {
        mSubTitle = mContext.getString(subtitle);
        return this;
    }

    /**
     * Set the description of the dialog. This is the required field.
     *
     * @param description String resource of the description.
     * @return {@link FingerprintDialogBuilder}
     * @see #setDescription(int)
     */
    public FingerprintDialogBuilder setDescription(@NonNull final String description) {
        mDescription = description;
        return this;
    }

    /**
     * Set the description of the dialog. This is the required field.
     *
     * @param description String resource of the description.
     * @return {@link FingerprintDialogBuilder}
     * @see #setDescription(String)
     */
    public FingerprintDialogBuilder setDescription(@StringRes final int description) {
        mDescription = mContext.getString(description);
        return this;
    }

    /**
     * Set the title of the negative button in the dialog. The default title of the button is "Cancel".
     *
     * @param text String of button title.
     * @return {@link FingerprintDialogBuilder}
     * @see #setNegativeButton(int)
     */
    public FingerprintDialogBuilder setNegativeButton(@Nullable final String text) {
        mButtonTitle = text;
        return this;
    }

    /**
     * Set the title of the negative button in the dialog. The default title of the button is "Cancel".
     *
     * @param text String resource of button title.
     * @return {@link FingerprintDialogBuilder}
     * @see #setNegativeButton(String)
     */
    public FingerprintDialogBuilder setNegativeButton(@StringRes final int text) {
        mButtonTitle = mContext.getString(text);
        return this;
    }

    /**
     * Build the {@link FingerprintDialogCompatV23}. This dialog will be displayed for android version.
     */
    public void show(@NonNull final FragmentManager fragmentManager,
                     @NonNull final AuthenticationCallback authenticationCallback) {

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            authenticationCallback.fingerprintAuthenticationNotSupported();
            return;
        }

        //Check if the device has the fingerprint sensor?
        if (!FingerprintUtils.isSupportedHardware(mContext)) {
            authenticationCallback.fingerprintAuthenticationNotSupported();
            return;
        }

        //Check if there are any fingerprints enrolled?
        if (!FingerprintUtils.isFingerprintEnroled(mContext)) {
            authenticationCallback.hasNoFingerprintEnrolled();
            return;
        }

        //noinspection ConstantConditions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
                || Build.VERSION.CODENAME.equals("P")/* TODO Remove once API 28 releases */) {
            showFingerprintDialog(authenticationCallback);
        } else {
            final FingerprintDialogCompatV23 fingerprintDialogCompat = FingerprintDialogCompatV23
                    .createDialog(mTitle, mSubTitle, mDescription, mButtonTitle);
            fingerprintDialogCompat.setAuthenticationCallback(authenticationCallback);
            fingerprintDialogCompat.show(fragmentManager, FingerprintDialogCompatV23.class.getName());
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
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
