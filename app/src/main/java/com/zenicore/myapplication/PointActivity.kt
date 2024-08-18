package com.zenicore.myapplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PointActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)
        val badges: ImageView = findViewById(R.id.badges)
        badges.setOnClickListener { showBadgeInfoDialog() }
    }
    fun onBackButtonClick(view: View) {
        onBackPressed()  // This will handle the back navigation
    }
    fun onInviteButtonClick(view: View) {
        val intent = Intent(this, InviteActivity::class.java)
        startActivity(intent)
    }
    private fun showBadgeInfoDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_badge_info)
        val badgeInfoImage = dialog.findViewById<ImageView>(R.id.badge_info_image)
        val btnClose = dialog.findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
