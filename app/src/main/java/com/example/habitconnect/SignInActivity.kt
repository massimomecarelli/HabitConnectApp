package com.example.habitconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.habitconnect.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ritorno all'activity parent (main)


        firebaseAuth = FirebaseAuth.getInstance()
        // listener sulla textview per aprire l'activity di sign up
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // listener sul bottone di sign in
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            // controlla campi vuoti
            if (email.isNotEmpty() && pass.isNotEmpty()) {

                // signin in firebase passando email e password
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, CommunityActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Credenziali errate", Toast.LENGTH_SHORT).show()

                    }
                }
            } else {
                Toast.makeText(this, "Riempi tutti i campi", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onStart() {
        super.onStart()

        // allo start dell'activity controlla se l'utente ha già fatto l'accesso e ha una sessione attiva
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)
        }
    }
}