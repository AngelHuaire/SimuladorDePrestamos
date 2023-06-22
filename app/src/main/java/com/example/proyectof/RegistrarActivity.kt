package com.example.proyectof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectof.databinding.ActivityPrestamoBinding
import com.example.proyectof.databinding.ActivityRegistrarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrarActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistrarBinding

    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_registrar)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("User")

        binding.btnCrearCuenta.setOnClickListener {
            crearNuevaCuenta()
        }

    }

    private fun crearNuevaCuenta(){

        if (!TextUtils.isEmpty(binding.edtNombreRegistrar.text) && !TextUtils.isEmpty(binding.edtApellidosRegistrar.text)
            && !TextUtils.isEmpty(binding.edtEmailRegistrar.text) && !TextUtils.isEmpty(binding.edtPasswordRegistrar.text)){
            binding.progressBar.visibility=View.VISIBLE

            auth.createUserWithEmailAndPassword(binding.edtEmailRegistrar.text.toString(), binding.edtPasswordRegistrar.text.toString())
                .addOnCompleteListener(this){
                        task ->

                    if (task.isComplete){
                        val user:FirebaseUser?=auth.currentUser
                        verificarEmail(user)

                  /*      val userBD = dbReference.child(user?.uid)

                        userBD.child("Nombre").setValue(nombreRegistrar)
                        userBD.child("Apellidos").setValue(apellidosRegistrar)
                        */

                        accion()



                    }
                }
        }else{
            val builder = AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Complete todos los datos")
                .setPositiveButton("Aceptar", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }




    private fun accion(){
        startActivity(Intent(this, LoginActivity::class.java))
    }





    private fun verificarEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                    task ->

                if (task.isComplete){
                    Toast.makeText(this, "Email enviado", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "Error al enviar email", Toast.LENGTH_LONG).show()
                }
            }
    }



}