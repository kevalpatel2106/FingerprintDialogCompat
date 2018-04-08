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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kevalpatel2106.fingerprint_dialog_compat.AuthenticationCallback
import com.kevalpatel2106.fingerprint_dialog_compat.FingerprintDialogBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FingerprintDialogBuilder(this)
                .setTitle("Title Title Title Title Title Title Title Title Title Title ")
                .setSubtitle("Subtitle Subtitle Subtitle Subtitle Subtitle Subtitle Subtitle Subtitle Subtitle Subtitle Subtitle ")
                .setDescription("Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description ")
                .setNegativeButton(null)
                .show(supportFragmentManager, object : AuthenticationCallback {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        Toast.makeText(this@MainActivity, errString, Toast.LENGTH_LONG).show()
                    }

                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                        Toast.makeText(this@MainActivity, helpString, Toast.LENGTH_LONG).show()
                    }

                    override fun onAuthenticationSucceeded() {
                        Toast.makeText(this@MainActivity,
                                "Authentication success!!!",
                                Toast.LENGTH_LONG).show()

                    }

                    override fun onAuthenticationFailed() {
                        Toast.makeText(this@MainActivity,
                                "Authentication failed!!!",
                                Toast.LENGTH_LONG).show()
                    }

                    override fun fingerprintAuthenticationNotSupported() {
                        Toast.makeText(this@MainActivity,
                                "Authentication not supported!!!",
                                Toast.LENGTH_LONG).show()
                    }

                    override fun authenticationCanceledByUser() {
                        Toast.makeText(this@MainActivity,
                                "Authentication canceled by the user!!!",
                                Toast.LENGTH_LONG).show()
                    }
                })
    }
}
