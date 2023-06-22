package com.example.proyectof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectof.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_ProyectoF)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_login)

        auth=FirebaseAuth.getInstance()

        binding.btnIngresar.setOnClickListener{
            loginUsuario()
        }

        binding.btnRegistrarNuevo.setOnClickListener {
            startActivity(Intent(this, RegistrarActivity::class.java))
        }

        binding.txvOlvidastePassword.setOnClickListener {
            startActivity(Intent(this, RecuperarPasswordActivity::class.java))
        }

        binding.btnIngresarGoogle.setOnClickListener {
            //Configuracion
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            val signInIntent = googleClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
        }

    }

    private fun loginUsuario(){
        if (!TextUtils.isEmpty(binding.edtUsuario.text) && !TextUtils.isEmpty(binding.edtPassword.text)){
            binding.progressBar2.visibility = View.VISIBLE
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(binding.edtUsuario.text.toString(), binding.edtPassword.text.toString())
                    .addOnCompleteListener({
                        if (it.isSuccessful){
                            accionLogin(binding.edtUsuario.text.toString())
                        }else{
                            val builder = AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Usuario o Contraseña incorrecto")
                                .setPositiveButton("Aceptar", null)
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                    })
        }else{
            val builder = AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Ingrese Usuario y Contraseña")
                .setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }


    private fun accion(){
        var intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("Usuario", binding.edtUsuario.text)
        startActivity(intent)
    }

    private fun accionLogin(email: String){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN){

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = task.getResult(ApiException::class.java)

                if (account != null){
                    Log.d("Tag", "firebasegoogleid $account.id")
                    firebaseAuthWithGoogle(account.idToken!!)
                    accionLogin(account.email?:"")
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Error al ingresar")
                        .setPositiveButton("Aceptar", null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            } catch (e: ApiException){
                Log.w("tag", "Fallo $e")
            }


        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Log.d("Tag","signInWithCredential:sucess")
                    /*
                    startActivity(Intent(this, MainActivity::class.java))
                     */

                }else{
                    Log.w("tag", "signInWithCredential:failure", task.exception)
                    val builder = AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("No se pudo logear")
                        .setPositiveButton("Aceptar", null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
    }

}