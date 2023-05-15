package org.app.saveourpets.usuarios.particular

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.reportes.CrearReporteActivity
import org.app.saveourpets.usuarios.LoginActivity
import org.app.saveourpets.usuarios.Sesion
import org.app.saveourpets.usuarios.Usuario
import org.app.saveourpets.utils.Validaciones
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActualizarPerfilActivity : AppCompatActivity() {
    private lateinit var edtNombres : EditText
    private lateinit var edtApellidos : EditText
    private lateinit var edtTelefono : EditText
    private lateinit var edtDui : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtDireccion : EditText
    private lateinit var btnActualizar : Button
    private lateinit var btnVolver : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_perfil)
        cargarDatosPerfil()
        accionBtnVolver()
        accionBtnActualizarPerfil()
    }

    private fun cargarDatosPerfil() {
        edtNombres = findViewById(R.id.edt_nombres)
        edtApellidos = findViewById(R.id.edt_apellidos)
        edtTelefono = findViewById(R.id.edt_telefono)
        edtDui = findViewById(R.id.edt_dui)
        edtEmail = findViewById(R.id.edt_email)
        edtDireccion = findViewById(R.id.edt_direccion)

        edtNombres.setText(Sesion.usuario.nombres)
        edtApellidos.setText(Sesion.usuario.apellidos)
        edtTelefono.setText(Sesion.usuario.telefono)
        edtDui.setText(Sesion.usuario.DUI)
        edtEmail.setText(Sesion.usuario.email)
        edtDireccion.setText(Sesion.usuario.direccion)
    }

    // Función para validar campos de entrada
    private fun errores() : Int {
        var errores : Int = 0

        if (Validaciones.estaVacio(edtNombres.text.toString())) {
            edtNombres.error = resources.getString(R.string.error_user_nombres)
            errores += 1
        }
        if (Validaciones.estaVacio(edtApellidos.text.toString())) {
            edtApellidos.error = resources.getString(R.string.error_user_apellidos)
            errores += 1
        }
        if (Validaciones.estaVacio(edtTelefono.text.toString())) {
            edtTelefono.error = resources.getString(R.string.error_user_tel)
            errores += 1
        } else if (!Validaciones.validarTelefono(edtTelefono.text.toString())) {
            edtTelefono.error = resources.getString(R.string.error_user_tel_2)
            errores += 1
        }
        if (!Validaciones.validarDui(edtDui.text.toString())) {
            edtDui.error = resources.getString(R.string.error_dui)
            errores += 1
        }
        if (!Validaciones.validarEmail(edtEmail.text.toString())) {
            edtEmail.error = resources.getString(R.string.login_error_email)
            errores += 1
        }
        if (Validaciones.estaVacio(edtDireccion.text.toString())) {
            edtDireccion.error = resources.getString(R.string.error_direccion)
            errores += 1
        }

        return errores
    }

    private fun accionBtnActualizarPerfil() {
        btnActualizar = findViewById(R.id.btn_actualizar)

        btnActualizar.setOnClickListener {
            if (errores() == 0) {
                val nombres = edtNombres.text.toString()
                val apellidos = edtApellidos.text.toString()
                val telefono = edtTelefono.text.toString()
                val dui = edtDui.text.toString()
                val email = edtEmail.text.toString()
                val direccion = edtDireccion.text.toString()

                val usuario = Usuario(
                    Sesion.usuario.id_usuario,
                    Sesion.usuario.id_tipo,
                    nombres,
                    apellidos,
                    email,
                    telefono,
                    dui,
                    direccion,
                    Sesion.usuario.fecha_nacimiento,
                    Sesion.usuario.contra
                )

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://saveourpets.probalosv.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                val api = retrofit.create(ClientAPI::class.java)

                api.actualizarUsuario(Sesion.usuario.id_usuario, usuario).enqueue(object : Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if (response.isSuccessful) {
                            Toast.makeText(baseContext, resources.getString(R.string.txt_info_actualizar_user), Toast.LENGTH_SHORT).show()
                            Sesion.usuario = response.body()!!
                            val intent = Intent(baseContext, PerfilActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(baseContext, resources.getString(R.string.txt_error_actualizar_user), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Toast.makeText(baseContext, resources.getString(R.string.txt_error_actualizar_user), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun accionBtnVolver() {
        btnVolver = findViewById(R.id.btn_volver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_usuario, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reportar -> {
                val intent = Intent(this, CrearReporteActivity::class.java)
                startActivity(intent)
                finish()
            }
            /*R.id.action_adoptar -> {
                val intent = Intent(this, VacunasActivity::class.java)
                startActivity(intent)
                finish()
            }*/
            R.id.action_perfil -> {
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_cerrar_sesion -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}