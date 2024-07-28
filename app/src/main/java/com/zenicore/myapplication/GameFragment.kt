package com.zenicore.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class GameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        val startGameButton: Button = view.findViewById(R.id.btn_start_game)
        startGameButton.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}