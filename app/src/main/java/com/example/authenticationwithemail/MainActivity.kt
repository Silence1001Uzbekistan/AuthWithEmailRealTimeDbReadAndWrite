package com.example.authenticationwithemail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.authenticationwithemail.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var googleSignInClient: GoogleSignInClient

    var RC_SIGN_IN = 1

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()


        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        binding.signInBtn.setOnClickListener {

            signIn()

        }


    }


    private fun signIn() {

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == RC_SIGN_IN) {


            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {


                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {

            }

        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->

            if (task.isSuccessful) {

                val user = auth.currentUser
                //doSomething(user)
                Toast.makeText(this, user!!.email, Toast.LENGTH_SHORT).show()

                //Ro'yxatdan o'tib bo'ldi
                //Nimadir ish bajarish mumkin

                startActivity(Intent(this,RealTimeDBActivity::class.java))


            } else {

                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()

            }

        }

    }

}