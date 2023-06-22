package com.example.authenticationwithemail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.authenticationwithemail.Models.User
import com.example.authenticationwithemail.databinding.ActivityRealTimeDbactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.StringBuilder

class RealTimeDBActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    lateinit var binding: ActivityRealTimeDbactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealTimeDbactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        Toast.makeText(this, "Real ${currentUser!!.email}", Toast.LENGTH_SHORT).show()


        //RealTimeDatabase Read and Write
        //Write

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("Xohlagan string yoziladi")


        val email = currentUser.email
        val displayName = currentUser.displayName
        val phoneNumber = currentUser.phoneNumber
        val photoUrl = currentUser.photoUrl
        val uid = currentUser.uid

        val user = User(email, displayName, phoneNumber, photoUrl.toString(), uid)

        reference.child(uid).setValue(user)

        // reference.setValue("Doniyor Qaleysan ?")

        //Read

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val stringBuilder = StringBuilder()
                val children = snapshot.children

                for (child in children) {

                    val value = child.getValue(User::class.java)
                    stringBuilder.append(value?.displayName)

                }

                binding.tv.text = stringBuilder.toString()

            }

            override fun onCancelled(error: DatabaseError) {


            }

        })

    }
}