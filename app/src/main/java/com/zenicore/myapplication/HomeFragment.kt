package com.zenicore.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var textViewUsername: TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var imageButtonGame: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        textViewUsername = view.findViewById(R.id.textView5)

        // Mengambil referensi ke Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("userId") // Ganti "userId" dengan ID pengguna yang sesuai

        // Mengambil username dari Firebase Database
        databaseReference.child("username").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.getValue(String::class.java)
                textViewUsername.text = username
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

        imageButtonGame = view.findViewById(R.id.imageButton4)
        imageButtonGame.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
