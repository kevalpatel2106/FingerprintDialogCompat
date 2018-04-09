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

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by Keval on 08/04/18.
 * Callback to get the results of the fingerprint authentication. These callbacks defines homogeneous
 * way of getting callbacks for fingerprint authentication process across Android versions.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
public interface AuthenticationCallback {

    /**
     * This method will notify the application whenever,
     * <br/>
     * <li>There is no finger print hardware on the device</li>
     * <li>The device is running on the android version below {@link android.os.Build.VERSION_CODES#M}</li>
     * <br/>
     * In  above cases the library won't display any fingerprint dialog and won't perform any authentication.
     * The developer should use any other way of authenticating the user, like pin or password to
     * authenticate the user.
     */
    void fingerprintAuthenticationNotSupported();

    /**
     * This callback will notify the application whenever user has not registered any fingerprint
     * into the phone settings.
     * <p>
     * In  above cases the library won't display any fingerprint dialog and won't perform any authentication.
     * Developer should redirect the user to the "Settings" application
     * (using {@link FingerprintUtils#openSecuritySettings(Context)}) and prompt the user to enroll
     * at least one fingerprint.
     *
     * @see FingerprintUtils#openSecuritySettings(Context)
     */
    void hasNoFingerprintEnrolled();

    /**
     * Called when an unrecoverable error has been encountered and the operation is complete.
     * No further callbacks will be made on this object. The library will stop scanning for the
     * fingerprint after this callback.
     *
     * @param errorCode A {@link ErrorCodes} identifying the error message.
     * @param errString A human-readable string that can be shown in UI
     * @see ErrorCodes
     */
    void onAuthenticationError(@ErrorCodes final int errorCode, @Nullable final CharSequence errString);

    /**
     * Called when a recoverable error has been encountered during authentication. The help
     * string is provided to give the user guidance for what went wrong, such as
     * "Sensor dirty, please clean it." The library will continue to scan for the fingerprint after
     * this callback.
     *
     * @param helpCode   A {@link HelperCodes} identifying the error message.
     * @param helpString A human-readable string that can be shown in UI
     * @see HelperCodes
     */
    void onAuthenticationHelp(@HelperCodes final int helpCode, @Nullable final CharSequence helpString);

    /**
     * This callback will be called whenever the user cancels the fingerprint authentication by
     * clicking the negative/cancel button on the dialog.
     */
    void authenticationCanceledByUser();

    /**
     * This callback indicates that the fingerprint authentication is successful. The library will
     * stop scanning for the fingerprint after this callback and dismiss the dialog.
     */
    void onAuthenticationSucceeded();

    /**
     * This callback indicates that the scanned finger does not match with the enrolled finger. The
     * library will continue to scan for the fingerprint after this callback.
     */
    void onAuthenticationFailed();
}
