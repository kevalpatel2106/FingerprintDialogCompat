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
import kotlinx.android.synthetic.main.activity_pin_authentication.*

class PinAuthenticationActivity : AppCompatActivity() {

    private val correctPin = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_authentication)

        pin_code_et.setOnPinEnteredListener {
            if (it.length == correctPin.length) {
                if (it.toString() == correctPin) {
                    startActivity(Intent(this@PinAuthenticationActivity,
                            AuthenticationSuccessActivity::class.java))
                } else {
                    startActivity(Intent(this@PinAuthenticationActivity,
                            AuthenticationFailActivity::class.java))
                }
            }

            finish()
        }
    }
}
