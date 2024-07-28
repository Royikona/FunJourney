package com.zenicore.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.zenicore.myapplication.R.id.btn_invite
import com.zenicore.myapplication.R.layout.activity_invite

class InviteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(activity_invite)

        val shareButton: Button = findViewById(btn_invite)
        shareButton.setOnClickListener {
            shareContent()
        }
    }

    private fun shareContent() {
        val shareMessage = "Check out this app GameBank with my referral code: REF1234"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareMessage)
        }
        startActivity(Intent.createChooser(shareIntent, "Share With"))
    }
}
