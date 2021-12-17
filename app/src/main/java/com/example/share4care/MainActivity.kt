package com.example.share4care

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.share4care.shihan.LoginActivity


class MainActivity : AppCompatActivity() {

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        handler = Handler()

        handler.postDelayed({
            val goToLogin = getIntentToOtherActivity<LoginActivity>(this)
            startActivity(goToLogin)
            finish()

        }, 3000)


        val goToLogin = getIntentToOtherActivity<LoginActivity>(this)
    }

    private inline fun <reified T : Any> getIntentToOtherActivity(currentActivity: Activity): Intent {
        return Intent(currentActivity, T::class.java)
    }
}