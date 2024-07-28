package com.zenicore.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class OfferActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer)

        val cardTopUpLinkAja: CardView = findViewById(R.id.cardTopUpLinkAja)
        val cardTopUpGoPay: CardView = findViewById(R.id.cardTopUpGoPay)
        val cardTraveloka: CardView = findViewById(R.id.cardTraveloka)
        val cardReddorz: CardView = findViewById(R.id.cardReddorz)

        cardTopUpLinkAja.setOnClickListener { showToast() }
        cardTopUpGoPay.setOnClickListener { showToast() }
        cardTraveloka.setOnClickListener { showToast() }
        cardReddorz.setOnClickListener { showToast() }
    }

    private fun showToast() {
        Toast.makeText(this, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
    }
}
