package com.zenicore.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zenicore.myapplication.databinding.FragmentHomeBinding
import com.zenicore.myapplication.databinding.FragmentProfileBinding
import com.zenicore.myapplication.databinding.PartialMainActivityBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var textViewUsername: TextView
    private lateinit var textViewScore: TextView
    private lateinit var textViewLevel: TextView
    private lateinit var imgBadge: ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var imageButtonGame: ImageButton
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        // Menginisialisasi komponen UI
        textViewUsername = view.findViewById(R.id.textView5)
        textViewScore = view.findViewById(R.id.tvScore)
        textViewLevel = view.findViewById(R.id.tvLevel)
        imgBadge = view.findViewById(R.id.imgBadge)
        imageButtonGame = view.findViewById(R.id.imageButton4)

        auth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = auth.currentUser
        val userId = user?.uid


        // Mengambil referensi ke database
        databaseReference =
            userId?.let { FirebaseDatabase.getInstance().getReference("users").child(it) }!! // Ganti "userId" dengan ID pengguna yang sesuai

        // Mendengarkan perubahan data pada username dan score
        databaseReference.child("displayName").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.getValue(String::class.java)
                // Update TextView with the username from the database
                textViewUsername.text = username ?: user?.displayName
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Tangani error di sini jika diperlukan
            }
        })

        databaseReference.child("highscore").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val score = dataSnapshot.getValue(Int::class.java) ?: 0
                textViewScore.text = "Score: $score"

                // Mengubah badge berdasarkan level
                val (level, levelDescription, badgeResId) = when (score) {
                    in 5..100 -> Triple(1, "Beginner", R.drawable.badge_beginner)
                    in 101..500 -> Triple(2, "Intermediate", R.drawable.badge_intermediate)
                    else -> Triple(3, "Pro", R.drawable.badge_pro)
                }
                textViewLevel.text = "Level $level: $levelDescription"

                // Update badge based on level
                imgBadge.setImageResource(badgeResId)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Tangani error di sini jika diperlukan
            }
        })

        // Mengatur listener untuk button game
        imageButtonGame.setOnClickListener {
            val intent = Intent(activity, GameActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
