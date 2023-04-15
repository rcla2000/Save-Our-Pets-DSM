package org.app.saveourpets.ui.especies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.app.saveourpets.databinding.FragmentEspeciesBinding

class EspeciesFragment : Fragment() {

    private var _binding: FragmentEspeciesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(EspeciesViewModel::class.java)

        _binding = FragmentEspeciesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSpecies
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}