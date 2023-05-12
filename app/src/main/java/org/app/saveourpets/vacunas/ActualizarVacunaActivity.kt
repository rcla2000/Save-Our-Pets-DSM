package org.app.saveourpets.vacunas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.especies.Especie
import org.app.saveourpets.especies.ListarEspeciesActivity
import org.app.saveourpets.razas.RazasActivity
import org.app.saveourpets.utils.Validaciones
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActualizarVacunaActivity : AppCompatActivity() {
    private lateinit var vacunaEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var btnActualizar : Button
    private lateinit var btnVolver : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_vacuna)
        actualizarVacuna()
    }

    private fun obtenerDatosVacuna() : Vacuna {
        val idVacuna : Int = intent.getIntExtra("id_vacuna", -1)
        val nombre : String = intent.getStringExtra("vacuna").toString()
        val descripcion : String = intent.getStringExtra("descripcion").toString()
        val vacuna = Vacuna(idVacuna, nombre, descripcion)
        return vacuna
    }
    private fun actualizarVacuna() {
        vacunaEditText = findViewById<EditText>(R.id.edt_vacuna)
        descripcionEditText = findViewById<EditText>(R.id.edt_descripcion)
        btnActualizar = findViewById<Button>(R.id.btn_actualizar_vacuna)
        btnVolver = findViewById<Button>(R.id.btn_volver)
        var errores : Int = 0

        // Se llena el formulario con la información del registro
        val vacuna : Vacuna = obtenerDatosVacuna()
        vacunaEditText.setText(vacuna.vacuna)
        descripcionEditText.setText(vacuna.descripcion)

        btnVolver.setOnClickListener {
            val intent = Intent(this, VacunasActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnActualizar.setOnClickListener {
            errores = 0

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
                val vacunaActualizada = Vacuna(vacuna.id_vacuna, nombre, descripcion)

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://172.16.102.107/api-save-our-pets/public/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                val api = retrofit.create(ClientAPI::class.java)

                api.actualizarVacuna(vacunaActualizada.id_vacuna, vacunaActualizada).enqueue(object : Callback<Vacuna> {
                    override fun onResponse(call: Call<Vacuna>, response: Response<Vacuna>) {
                        if (response.isSuccessful) {
                            val ok = response.errorBody()?.string()
                            Toast.makeText(this@ActualizarVacunaActivity, resources.getString(R.string.info_actualizar_vacuna), Toast.LENGTH_SHORT).show()
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(this@ActualizarVacunaActivity, resources.getString(R.string.error_actualizar_vacuna), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Vacuna>, t: Throwable) {
                        Toast.makeText(this@ActualizarVacunaActivity, resources.getString(R.string.error_actualizar_vacuna), Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_especies -> {
                val intent = Intent(this, ListarEspeciesActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_vacunas -> {
                val intent = Intent(this, VacunasActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_razas -> {
                val intent = Intent(this, RazasActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}