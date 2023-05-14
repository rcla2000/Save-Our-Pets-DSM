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
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.especies.Especie
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
            .baseUrl("http://192.168.0.7/api-save-our-pets/public/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val especiesApi = retrofit.create(ClientAPI::class.java)

        especiesApi.getEspecies().enqueue(object : Callback<List<Especie>> {
            override fun onResponse(
                call: Call<List<Especie>>,
                response: Response<List<Especie>>
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

            override fun onFailure(call: Call<List<Especie>>, t: Throwable) {
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