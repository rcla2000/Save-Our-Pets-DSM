package org.app.saveourpets.vacunas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.utils.Validaciones
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AgregarVacunaActivity : AppCompatActivity() {
    private lateinit var vacunaEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var btnAgregar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_vacuna)
    }

    private fun agregarVacuna() {
        vacunaEditText = findViewById<EditText>(R.id.edt_vacuna)
        descripcionEditText = findViewById<EditText>(R.id.edt_descripcion)
        btnAgregar = findViewById<Button>(R.id.btn_agregar_vacuna)
        var errores : Int = 0

        btnAgregar.setOnClickListener {
            if (Validaciones.estaVacio(vacunaEditText.text.toString())) {
                errores += 1
                vacunaEditText.setError(resources.getString(R.string.error_nombre_vacuna))
            }
            if (Validaciones.estaVacio(descripcionEditText.text.toString())) {
                errores += 1
                descripcionEditText.setError(resources.getString(R.string.error_des_vacuna))
            }

            if (errores == 0) {
                val nombre = vacunaEditText.text.toString()
                val descripcion = descripcionEditText.text.toString()
                val vacuna = Vacuna(0, nombre, descripcion)
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.0.4/api-save-our-pets/public/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                val api = retrofit.create(ClientAPI::class.java)

                api.crearVacuna(vacuna).enqueue(object : Callback<Vacuna> {
                    override fun onResponse(call: Call<Vacuna>, response: Response<Vacuna>) {
                        if (response.isSuccessful) {
                            val ok = response.errorBody()?.string()
                            Toast.makeText(this@AgregarVacunaActivity, resources.getString(R.string.info_vacuna), Toast.LENGTH_SHORT).show()
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(this@AgregarVacunaActivity, resources.getString(R.string.error_vacuna), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Vacuna>, t: Throwable) {
                        Toast.makeText(this@AgregarVacunaActivity, resources.getString(R.string.error_vacuna), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}