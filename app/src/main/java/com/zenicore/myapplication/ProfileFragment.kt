package com.zenicore.myapplication

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.zenicore.myapplication.databinding.FragmentProfileBinding
import java.io.ByteArrayOutputStream
import com.zenicore.myapplication.auth.SigninActivity

class ProfileFragment : Fragment() {
    companion object {
        const val REQUEST_GALLERY = 100
    }

    private lateinit var binding: FragmentProfileBinding
    private var imageUri: Uri? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = auth.currentUser

        if (user != null) {
            if (user.photoUrl != null) {
                Picasso.get().load(user.photoUrl).into(binding.ivProfile)
            } else {
                Picasso.get().load("https://picsum.photos/200").into(binding.ivProfile)
            }
            binding.etName.setText(user.displayName)
            binding.etEmail.setText(user.email)

            val userId = user.uid
            val db = FirebaseDatabase.getInstance()
            val userRef = db.getReference("users").child(userId)

            userRef.child("bio").get().addOnSuccessListener { snapshot ->
                val bio = snapshot.getValue(String::class.java)
                binding.etBio.setText(bio)
            }

            userRef.child("highscore").get().addOnSuccessListener { snapshot ->

                val highScore = snapshot.getValue(Int::class.java) ?: 0
                binding.tvScore.text = "Score: $highScore"
            }
        }

        binding.ivProfile.setOnClickListener {
            intentGallery()
        }

        binding.btnUpdate.setOnClickListener {
            val image = imageUri ?: user?.photoUrl ?: Uri.parse("https://picsum.photos/200")
            val name = binding.etName.text.toString().trim()
            val bio = binding.etBio.text.toString().trim()

            if (name.isEmpty()) {
                binding.etName.error = "Nama Harus Di Isi!"
                binding.etName.requestFocus()
                return@setOnClickListener
            }

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(image)
                .build()

            user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(activity, "Profile Updated", Toast.LENGTH_SHORT).show()

                    val userId = user.uid
                    val db = FirebaseDatabase.getInstance()
                    val userRef = db.getReference("users").child(userId)

                    userRef.child("bio").setValue(bio).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Toast.makeText(activity, "Bio Updated", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "Failed to update bio", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(activity, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, SigninActivity::class.java))
            activity?.finish()
        }
    }

    private fun intentGallery() {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).also { intent ->
            startActivityForResult(intent, REQUEST_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                uploadImage(bitmap)
            }
        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child(
            "img/${FirebaseAuth.getInstance().currentUser?.uid}"
        )

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        ref.putBytes(image)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnCompleteListener { task ->
                        task.result?.let { uri ->
                            imageUri = uri
                            binding.ivProfile.setImageBitmap(imgBitmap)
                            updateProfileImageUri(uri)
                        }
                    }
                } else {
                    Toast.makeText(activity, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateProfileImageUri(uri: Uri) {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Profile Image Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Failed to update profile image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
