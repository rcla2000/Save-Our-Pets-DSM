package org.app.saveourpets.datos

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.app.saveourpets.R

class VacunaAdapter(private val elementos : List<Vacuna>): RecyclerView.Adapter<VacunaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_vacunas, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = elementos[position]
        holder.title.text=item.vacuna
        holder.subtitle.text = ""
        if(item.descripcion.length>90){
            holder.description.text = item.descripcion.substring(0,87) + "..."
        }else{
            holder.description.text = item.descripcion
        }
        holder.imagen.setImageResource(R.drawable.vacuna_icon)

    }

    override fun getItemCount(): Int {
        return elementos.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen : ImageView = view.findViewById(R.id.imgViewer)
        val title: TextView = view.findViewById(R.id.item_title)
        val subtitle : TextView = view.findViewById(R.id.item_subtitle)
        val description: TextView = view.findViewById(R.id.item_descripcion)


    }

}