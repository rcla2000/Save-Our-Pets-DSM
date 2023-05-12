package org.app.saveourpets.razas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.app.saveourpets.R

class RazaAdapter(private val razas : List<Raza>): RecyclerView.Adapter<RazaAdapter.ViewHolder>() {
    private var btnEliminarListener : OnBtnEliminarListener? = null
    private var btnActualizarListener : OnBtnActualizarListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_especies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = razas[position]
        holder.nombre.text = item.nombre
        holder.subitem.text = item.especie
        holder.description.text = ""

        holder.btnActualizar.setOnClickListener {
            btnActualizarListener?.onBtnActualizarClick(item)
        }

        holder.btnEliminar.setOnClickListener {
            btnEliminarListener?.onBtnEliminarClick(item)
        }
    }

    override fun getItemCount(): Int {
        return razas.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img : ImageView = view.findViewById(R.id.imgEspecie)
        val nombre: TextView = view.findViewById(R.id.item_especie)
        val subitem : TextView = view.findViewById(R.id.item_subespecie)
        val description: TextView = view.findViewById(R.id.item_descripcion)
        val btnActualizar : Button = view.findViewById<Button>(R.id.btnUpdate)
        val btnEliminar : Button = view.findViewById<Button>(R.id.btnDelete)
    }

    fun setOnBtnActualizarListener(listener: OnBtnActualizarListener?) {
        this.btnActualizarListener = listener
    }

    fun setOnBtnEliminarListener(listener: OnBtnEliminarListener?) {
        this.btnEliminarListener = listener
    }

    interface OnBtnActualizarListener {
        fun onBtnActualizarClick(raza: Raza)
    }

    interface OnBtnEliminarListener {
        fun onBtnEliminarClick(raza: Raza)
    }
}