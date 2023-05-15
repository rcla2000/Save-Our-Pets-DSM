package org.app.saveourpets.especies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.app.saveourpets.R
import org.app.saveourpets.mascotas.Mascota

class MascotasAdapter(private val mascot : List<Mascota>): RecyclerView.Adapter<MascotasAdapter.ViewHolder>() {
    private var listener : OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_mascotas, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mascot[position]
        holder.nombreespecie.text=item.nombre_mascota
        holder.subitem.text = "${item.especie} - ${item.raza}"
        holder.description.text = "Color: ${item.color_pelo}\nPeso: ${item.peso}\nEsterilizado:${item.esterilizado}\nFecha Nacimiento: ${if(item.fecha_nacimiento.isNullOrBlank()) "No disponible" else item.fecha_nacimiento}\n"

        if(item.especie == "Gato"){
            holder.imgespecie.setImageResource(R.drawable.cat_icon)
        }else if(item.especie == "Perro"){
            holder.imgespecie.setImageResource(R.drawable.dog_icon)
        }else{
            holder.imgespecie.setImageResource(R.drawable.catdog_icon)
        }

        holder.btnActualizar.setOnClickListener {
            listener?.onItemClick(item)
        }


    }

    override fun getItemCount(): Int {
        return mascot.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgespecie : ImageView = view.findViewById(R.id.imgEspecie)
        val nombreespecie: TextView = view.findViewById(R.id.item_mascota)
        val subitem : TextView = view.findViewById(R.id.item_subitem)
        val description: TextView = view.findViewById(R.id.item_descripcion)
        val btnActualizar : Button = view.findViewById<Button>(R.id.btnUpdate)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(especie: Mascota)
    }


}