package org.app.saveourpets.usuarios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.usuarios.particular.RegistroParticularActivity
import org.app.saveourpets.utils.Validaciones
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnRegistro : Button
    private lateinit var btnLogin : Button
    private lateinit var btnReporte : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        accionBtnRegistro()
        accionBtnLogin()
    }

    // Función para validar los campos de entrada
    private fun errores() : Int {
        var errores : Int = 0

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)

        if (Validaciones.estaVacio(edtEmail.text.toString())) {
            errores += 1
            edtEmail.error = resources.getString(R.string.error_email)
        }
        if (!Validaciones.validarEmail(edtEmail.text.toString())) {
            errores += 1
            edtEmail.error = resources.getString(R.string.login_error_email)
        }
        if (Validaciones.estaVacio(edtPassword.text.toString())) {
            errores += 1
            edtPassword.error = resources.getString(R.string.login_error_password)
        }
        return errores
    }

    private fun accionBtnRegistro() {
        btnRegistro = findViewById(R.id.btn_registro)

        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegistroParticularActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun accionBtnLogin() {
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener {
            if (errores() == 0) {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                val usuario = Usuario(email, password)

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.0.7/api-save-our-pets/public/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                val api = retrofit.create(ClientAPI::class.java)

                api.login(usuario).enqueue(object :
                    Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if (response.isSuccessful) {
                            val res : Boolean? = response.body()
                            if (res == true) {
                                Toast.makeText(this@LoginActivity, resources.getString(R.string.login_correcto), Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@LoginActivity, resources.getString(R.string.error_login_1), Toast.LENGTH_LONG).show()
                            }
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(this@LoginActivity, resources.getString(R.string.error_login_2), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, resources.getString(R.string.error_login_3), Toast.LENGTH_LONG).show()
                    }
                })

            }
        }
    }
}