package org.app.saveourpets.usuarios.particular

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.especies.Especie
import org.app.saveourpets.usuarios.LoginActivity
import org.app.saveourpets.usuarios.Usuario
import org.app.saveourpets.utils.Validaciones
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroParticularActivity : AppCompatActivity() {
    private lateinit var edtNombres : EditText
    private lateinit var edtApellidos : EditText
    private lateinit var edtTelefono : EditText
    private lateinit var edtDui : EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtDireccion : EditText
    private lateinit var edtFechaNacimiento : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnRegistro : Button
    private lateinit var btnVolver : Button
    private lateinit var dpFechaNacimiento : DatePicker

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_particular)
        accionBtnVolver()
        accionBtnRegistro()
    }

    private fun obtenerFechaDatePicker() : String {
        var dia = dpFechaNacimiento?.dayOfMonth.toString().padStart(2, '0')
        var mes = (dpFechaNacimiento!!.month+1).toString().padStart(2, '0')
        var anio = dpFechaNacimiento?.year.toString().padStart(4, '0')
        return dia + "/" + mes + "/" + anio
    }

    fun mostrarCalendario(view: View?) : Unit{
        dpFechaNacimiento?.visibility = View.VISIBLE
    }

    private fun convertirFecha(fecha : String) : String {
        if (!fecha.isEmpty()) {
            val partesFecha = fecha.split("/")
            val dia = partesFecha[0]
            val mes = partesFecha[1]
            val anio = partesFecha[2]
            return "$anio/$mes/$dia"
        }
        return "0000/00/00"
    }

    // Función para validar campos de entrada
    private fun errores() : Int {
        var errores : Int = 0
        edtNombres = findViewById(R.id.edt_nombres)
        edtApellidos = findViewById(R.id.edt_apellidos)
        edtTelefono = findViewById(R.id.edt_telefono)
        edtDui = findViewById(R.id.edt_dui)
        edtEmail = findViewById(R.id.edt_email)
        edtDireccion = findViewById(R.id.edt_direccion)
        edtPassword = findViewById(R.id.edt_password)
        edtFechaNacimiento = findViewById(R.id.edt_fecha_nacimiento)

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
        }
        if (!Validaciones.validarTelefono(edtTelefono.text.toString())) {
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
        if (Validaciones.estaVacio(edtFechaNacimiento.text.toString())) {
            edtFechaNacimiento.error = resources.getString(R.string.error_fecha_nacimiento)
            errores += 1
        } else if (!Validaciones.validarFecha(edtFechaNacimiento.text.toString())) {
            edtFechaNacimiento.error = resources.getString(R.string.error_fecha_nacimiento_2)
            errores += 1
        }
        if (Validaciones.estaVacio(edtPassword.text.toString())) {
            edtPassword.error = resources.getString(R.string.error_password)
            errores += 1
        }

        return errores
    }

    private fun accionBtnVolver() {
        btnVolver = findViewById(R.id.btn_volver)

        btnVolver.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun accionBtnRegistro() {
        btnRegistro = findViewById(R.id.btn_registro)
        dpFechaNacimiento = findViewById(R.id.dpFechaNacimiento)
        edtFechaNacimiento = findViewById(R.id.edt_fecha_nacimiento)
        // Asignando fecha actual del calendario al editText
        edtFechaNacimiento?.setText(obtenerFechaDatePicker())

        // Evento para cuando se seleccione una fecha del datepicker
        dpFechaNacimiento?.setOnDateChangedListener {
                dpFechaNacimiento, anio, mes, dia ->
            edtFechaNacimiento?.setText(obtenerFechaDatePicker())
            dpFechaNacimiento?.visibility = View.GONE
        }

        btnRegistro.setOnClickListener {
            if (errores() == 0) {
                val nombres = edtNombres.text.toString()
                val apellidos = edtApellidos.text.toString()
                val telefono = edtTelefono.text.toString()
                val dui = edtDui.text.toString()
                val email = edtEmail.text.toString()
                val direccion = edtDireccion.text.toString()
                val fechaNacimiento = convertirFecha(edtFechaNacimiento.text.toString())
                val password = edtPassword.text.toString()
                val tipoUsuario : Int = 3

                val usuario = Usuario(
                    0,
                    tipoUsuario,
                    nombres,
                    apellidos,
                    email,
                    telefono,
                    dui,
                    direccion,
                    fechaNacimiento,
                    password
                )

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.0.7/api-save-our-pets/public/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                val api = retrofit.create(ClientAPI::class.java)

                api.crearUsuario(usuario).enqueue(object : Callback<Usuario> {
                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@RegistroParticularActivity, resources.getString(R.string.txt_info_registro_user), Toast.LENGTH_SHORT).show()
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(this@RegistroParticularActivity, resources.getString(R.string.txt_error_registro_user), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                        Toast.makeText(this@RegistroParticularActivity, resources.getString(R.string.txt_error_registro_user), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}