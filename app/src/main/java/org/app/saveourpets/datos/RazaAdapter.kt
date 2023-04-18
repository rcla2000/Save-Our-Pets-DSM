package org.app.saveourpets.datos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.app.saveourpets.R

class RazaAdapter(private val razas : List<Raza>): RecyclerView.Adapter<RazaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_razas, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = razas[position]
        holder.idraza.text = item.id_raza.toString()
        holder.nombreraza.text=item.nombre
        holder.nombreespecie.text=item.especie
        if(item.imagen.isNullOrBlank()){
            holder.imgraza.setImageResource(R.drawable.catdog_icon)
        }else{Picasso.get().load(item.imagen).into(holder.imgraza)}

    }

    override fun getItemCount(): Int {
        return razas.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgraza : ImageView = view.findViewById(R.id.imgRaza)
        val idraza: TextView = view.findViewById(R.id.item_descripcion)
        val nombreraza: TextView = view.findViewById(R.id.item_raza)
        val nombreespecie: TextView = view.findViewById(R.id.item_especie)

        override fun toString(): String {
            return super.toString() + " '" + nombreraza.text + "'"
        }
    }

}