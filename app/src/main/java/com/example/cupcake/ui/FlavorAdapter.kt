package com.example.cupcake.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.cupcake.R
import com.example.cupcake.model.Flavor
import com.example.cupcake.model.OrderViewModel

class FlavorAdapter(
    private val viewModel: OrderViewModel
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
        var item = viewModel.flavors.value?.get(position)
        holder.image.setImageResource(R.drawable.cupcake)
        holder.name.text = item?.name
        holder.flavorQuantity.text = item?.quantity.toString()

        holder.removeButton.setOnClickListener {

            // Min number of cupcakes per order
            if (item?.quantity!! > 0) {
                item.quantity--
                holder.flavorQuantity.text = (item.quantity).toString()
                viewModel.flavors.value?.get(position)?.quantity = item.quantity
            }
        }

        holder.addButton.setOnClickListener {

            // Max number of cupcakes per order
            if (item?.quantity!! < 20) {
                item.quantity++
                holder.flavorQuantity.text = item.quantity.toString()
                viewModel.flavors.value?.get(position)?.quantity = item.quantity
            }
        }
    }

    override fun getItemCount(): Int = viewModel.flavors.value?.size!!

}