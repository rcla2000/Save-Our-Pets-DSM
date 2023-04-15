package org.app.saveourpets.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.app.saveourpets.databinding.FragmentHomeBinding
import org.app.saveourpets.datos.Especies
import org.app.saveourpets.datos.EspeciesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        val retrofit = Retrofit.Builder()
            .baseUrl("https://saveourpets.probalosv.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val especiesApi = retrofit.create(EspeciesApi::class.java)

        especiesApi.obtenerEspecies().enqueue(object : Callback<List<Especies>> {
            override fun onResponse(
                call: Call<List<Especies>>,
                response: Response<List<Especies>>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Entramo")
                    val mascotas = response.body()
                    Log.d(TAG, mascotas.toString())
                } else {
                    Log.d(TAG, "Error 22 mi chuchito")
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener las especies: $error")
                    Toast.makeText(
                        context,
                        "Error al obtener especies 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Especies>>, t: Throwable) {
                // manejar el error
                Log.d(TAG, "Error mi chuchito ${t.toString()}")
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}