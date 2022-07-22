package com.example.cupcake.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cupcake.R
import com.example.cupcake.databinding.FlavorItemBinding
import com.example.cupcake.databinding.FragmentStartBinding
import com.example.cupcake.model.Flavor
import kotlin.contracts.contract

class FlavorAdapter(
    private val dataset: List<Flavor>,
) : RecyclerView.Adapter<FlavorAdapter.FlavorViewHolder>() {

    class FlavorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)
        val name: TextView = view.findViewById(R.id.tv_flavor)
        val flavorQuality: TextView = view.findViewById(R.id.flavor_quantity)
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
        holder.flavorQuality.text = item.quantity.toString()
    }

    override fun getItemCount() = dataset.size

}