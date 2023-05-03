package org.app.saveourpets

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.vacunas.Vacuna
import org.app.saveourpets.vacunas.VacunaAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VacunasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VacunasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VacunaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_vacunas, container, false)

        recyclerView = view.findViewById(R.id.list_Vacunas)
        recyclerView.layoutManager = LinearLayoutManager(context)

        llenarListado()

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

// Define la acción del FAB
        fab.setOnClickListener {
            Snackbar.make(view, "Clic en Vacunas WAZAA", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        return view
    }

    private fun llenarListado() {
        // Crea una instancia de Retrofit con el cliente OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.4/api-save-our-pets/public/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia del servicio que utiliza la autenticación HTTP básica
        val api = retrofit.create(ClientAPI::class.java)

        val call = api.getVacunas()
        call.enqueue(object : Callback<List<Vacuna>> {
            override fun onResponse(call: Call<List<Vacuna>>, response: Response<List<Vacuna>>) {
                if (response.isSuccessful) {
                    val consulta = response.body()
                    if (consulta != null) {
                        adapter = VacunaAdapter(consulta)
                        recyclerView.adapter = adapter
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener vacunas: $error")
                    Toast.makeText(
                        context,
                        "Error al obtener vacunas1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Vacuna>>, t: Throwable) {
                Log.e("API", "Error al obtener: ${t.message}")
                Toast.makeText(
                    context,
                    "Error al obtener los alumnos 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VacunasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VacunasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}