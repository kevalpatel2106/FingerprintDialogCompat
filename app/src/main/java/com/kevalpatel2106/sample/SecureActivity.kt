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

package com.kevalpatel2106.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kevalpatel2106.fingerprintdialog.AuthenticationCallback
import com.kevalpatel2106.fingerprintdialog.FingerprintDialogBuilder
import com.kevalpatel2106.fingerprintdialog.FingerprintUtils
import kotlinx.android.synthetic.main.activity_secure.*

/**
 * Test activity.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
class SecureActivity : AppCompatActivity() {

    private var isAuthenticateUsingPin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secure)

        setFingerprintAuthentication()
        authenticate_btn.setOnClickListener {
            if (isAuthenticateUsingPin) {
                startActivity(Intent(this@SecureActivity, PinAuthenticationActivity::class.java))
                setFingerprintAuthentication()
            } else {
                showAuthenticationDialog()
            }
        }
    }

    private fun showAuthenticationDialog() {
        FingerprintDialogBuilder(this)
                .setTitle(R.string.fingerprint_dialog_title)
                .setSubtitle(R.string.fingerprint_dialog_subtitle)
                .setDescription(R.string.fingerprint_dialog_description)
                .setNegativeButton(R.string.fingerprint_dialog_button_title)
                .show(supportFragmentManager, object : AuthenticationCallback {
                    override fun hasNoFingerprintEnrolled() {
                        // User has no fingerprint enrolled.
                        // Redirecting to the settings.
                        FingerprintUtils.openSecuritySettings(this@SecureActivity)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        Toast.makeText(this@SecureActivity, errString, Toast.LENGTH_SHORT).show()

                        // Unrecoverable error.
                        // Switch to alternate authentication method.
                        setPinAuthentication()
                    }

                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                        // Authentication process has some warning.
                        // Handle it if you want.
                    }

                    override fun onAuthenticationSucceeded() {
                        // Authentication success
                        // You user is now authenticated.
                        startActivity(Intent(this@SecureActivity, AuthenticationSuccessActivity::class.java))
                    }

                    override fun onAuthenticationFailed() {
                        // Authentication failed.
                        // Fingerprint scanning is still running.
                    }

                    override fun fingerprintAuthenticationNotSupported() {
                        // Device doesn't support fingerprint authentication.
                        // Switch to alternate authentication method.
                        setPinAuthentication()
                    }

                    override fun authenticationCanceledByUser() {
                        Toast.makeText(this@SecureActivity,
                                "Authentication canceled by the user!!!",
                                Toast.LENGTH_LONG).show()
                    }
                })
    }

    private fun setFingerprintAuthentication() {
        isAuthenticateUsingPin = false
        authenticate_btn.text = getString(R.string.authenticate_using_fingerprint)
    }

    private fun setPinAuthentication() {
        isAuthenticateUsingPin = true
        authenticate_btn.text = "PIN Authentication"
    }
}
