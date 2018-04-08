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

import android.support.annotation.Nullable;

/**
 * Created by Keval on 08/04/18.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
public interface AuthenticationCallback {

    void fingerprintAuthenticationNotSupported();

    void hasNoFingerprintEnrolled();

    void onAuthenticationError(@ErrorCodes final int errorCode, @Nullable final CharSequence errString);

    void onAuthenticationHelp(@HelperCodes final int helpCode, @Nullable final CharSequence helpString);

    void authenticationCanceledByUser();

    void onAuthenticationSucceeded();

    void onAuthenticationFailed();
}
