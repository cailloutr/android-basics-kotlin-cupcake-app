package com.example.cupcake.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cupcake.R
import com.example.cupcake.model.Flavor

class FlavorAdapter(
    private val dataset: List<Flavor>,
) : RecyclerView.Adapter<FlavorAdapter.FlavorViewHolder>() {

    class FlavorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)
        val name: TextView = view.findViewById(R.id.tv_flavor)
        val flavorQuantity: TextView = view.findViewById(R.id.flavor_quantity)
        val removeButton: ImageButton = view.findViewById(R.id.remove_button)
        val addButton: ImageButton = view.findViewById(R.id.add_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlavorViewHolder {

        val adapterLayout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.flavor_item, parent, false)
        return FlavorViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: FlavorViewHolder, position: Int) {
        val item = dataset[position]
        holder.image.setImageResource(R.drawable.cupcake)
        holder.name.text = item.name
        holder.flavorQuantity.text = item.quantity.toString()

        var txtQuantity = item.quantity
        holder.removeButton.setOnClickListener {

            // Min number of cupcakes per order
            if (txtQuantity > 0) {
                txtQuantity--
                holder.flavorQuantity.text = (txtQuantity).toString()
            }
        }

        holder.addButton.setOnClickListener {

            // Max number of cupcakes per order
            if (txtQuantity < 20) {
                txtQuantity++
                holder.flavorQuantity.text = txtQuantity.toString()
            }
        }
    }

    override fun getItemCount() = dataset.size

}