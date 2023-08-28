package com.example.habitconnect

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.habitconnect.databinding.ActivitySignUpBinding
import com.example.habitconnect.util.Checker
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ritorno all'activity parent (main)

        firebaseAuth = FirebaseAuth.getInstance()

        // listener sul testo "Sign in" che fa intent verso la pagina di sign in
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        // listener sul bottone di sign up per la registrazione
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            // controlla che i campi non siano vuoti e che la mail segua il pattern
            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (Checker.isValidEmail(email)) {
                    if (Checker.passwordLengthCheck(pass)) {
                        // controlla che il reinserimento della password coincida con la password
                        if (Checker.passwordCheck(pass, confirmPass)) {

                            // crea l'utente passando email e password
                            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener {
                                    // se va a buon fine va nella pagina di sign in
                                    if (it.isSuccessful) {
                                        val intent = Intent(this, SignInActivity::class.java)
                                        startActivity(intent)
                                    } else { // se va male mostra l'errore
                                        Toast.makeText(
                                            this,
                                            it.exception.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }
                        } else
                            Toast.makeText(this, "La Password non corrisponde", Toast.LENGTH_SHORT)
                                .show()
                    } else Toast.makeText(this, "La Password deve contenere almeno 6 caratteri", Toast.LENGTH_SHORT).show()
                }else Toast.makeText(this, "EMAIL NON VALIDA", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Riempi tutti i campi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}