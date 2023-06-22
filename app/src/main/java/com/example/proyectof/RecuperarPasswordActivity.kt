package com.example.proyectof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectof.databinding.ActivityRecuperarPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperarPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecuperarPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRecuperarEmail.setOnClickListener {
            if (!TextUtils.isEmpty(binding.edtEmailRecuperar.text)){
                auth.sendPasswordResetEmail(binding.edtEmailRecuperar.text.toString())
                    .addOnCompleteListener(this){
                        task ->
                        if (task.isSuccessful){
                            binding.progressBar.visibility= View.VISIBLE
                            Toast.makeText(this, "Email enviado correctamente", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }else{
                            val builder = AlertDialog.Builder(this)
                                .setTitle("Error")
                                .setMessage("Error al recuperar email")
                                .setPositiveButton("Aceptar", null)
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                    }
            }
        }

    }
}