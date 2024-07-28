package com.zenicore.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class GameActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var tvScore: TextView
    private lateinit var tvHighScore: TextView
    private var currentScore: Int = -1
    private var highScore: Int =-1
    private lateinit var handler: Handler
    private lateinit var scoreRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        webView = findViewById(R.id.webView1)
        tvScore = findViewById(R.id.tvScore)
        tvHighScore = findViewById(R.id.tvHighScore)

        setupWebView()
        fetchHighScore()

        val backButton: Button = findViewById(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        handler = Handler(Looper.getMainLooper())
        scoreRunnable = object : Runnable {
            override fun run() {
                currentScore++
                tvScore.text = "Score: $currentScore"
                if (currentScore > highScore) {
                    highScore = currentScore
                    tvHighScore.text = "High Score: $highScore"
                    saveHighScoreToFirebase(highScore)
                }
                handler.postDelayed(this, 20000) // 20 seconds
            }
        }
    }

    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInterface(), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }
        webView.loadUrl("https://play.famobi.com/element-blocks")
    }

    private fun fetchHighScore() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val db = FirebaseDatabase.getInstance()
            val userRef = db.getReference("users").child(it.uid)
            userRef.child("highscore").get().addOnSuccessListener { snapshot ->
                highScore = snapshot.getValue(Int::class.java) ?: 0
                tvHighScore.text = "High Score: $highScore"
            }
        }
    }

    private inner class WebAppInterface {
        @JavascriptInterface
        fun updateScore(score: String) {
            // This method can be kept for future use if needed
        }
    }

    private fun saveHighScoreToFirebase(score: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val db = FirebaseDatabase.getInstance()
            val userRef = db.getReference("users").child(it.uid)
            userRef.child("highscore").setValue(score)
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(scoreRunnable) // Start the score updating
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(scoreRunnable) // Stop the score updating
    }
}
