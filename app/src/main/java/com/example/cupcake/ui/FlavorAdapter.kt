package com.example.cupcake.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cupcake.R
import com.example.cupcake.model.OrderViewModel

class FlavorAdapter(
    private val viewModel: OrderViewModel,
    private val fragmentCaller: Int,
    private val context: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val FLAVOR_CALLER = 1
        const val SUMMARY_CALLER = 2
    }


    class FlavorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)
        val name: TextView = view.findViewById(R.id.tv_flavor)
        val flavorQuantity: TextView = view.findViewById(R.id.flavor_quantity)
        val removeButton: ImageButton = view.findViewById(R.id.remove_button)
        val addButton: ImageButton = view.findViewById(R.id.add_button)
    }

    class SummaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView)
        val name: TextView = view.findViewById(R.id.tv_flavor)
        val flavorQuantity: TextView = view.findViewById(R.id.tv_flavor_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (fragmentCaller == 2) {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.summary_item, parent, false)
            SummaryViewHolder(view)
        } else {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.flavor_item, parent, false)
            FlavorViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (fragmentCaller == FLAVOR_CALLER) {
            val item = viewModel.flavors.value?.get(position)
            val viewHolder = FlavorViewHolder(holder.itemView)
            viewHolder.image.setImageResource(R.drawable.cupcake)
            viewHolder.name.text = item?.name
            viewHolder.flavorQuantity.text = item?.quantity.toString()

            viewHolder.removeButton.setOnClickListener {

                // Min number of cupcakes per order
                if (item?.quantity!! > 0) {
                    item.quantity--
                    viewHolder.flavorQuantity.text = (item.quantity).toString()
                    //viewModel.flavors.value?.get(position)?.quantity = item.quantity
                    viewModel.flavors.value?.get(position)?.let { it1 ->
                        viewModel.setUnitQuantity(it1, item.quantity)
                    }
                }
            }

            viewHolder.addButton.setOnClickListener {

                // Max number of cupcakes per order
                if (item?.quantity!! < 20) {
                    item.quantity++
                    viewHolder.flavorQuantity.text = item.quantity.toString()
                    viewModel.flavors.value?.get(position)?.quantity = item.quantity
                    viewModel.flavors.value?.get(position)?.let { it1 ->
                        viewModel.setUnitQuantity(it1, item.quantity)
                    }
                }
            }
        } else {
            val item = viewModel.orderList.value?.get(position)
            val viewHolder = SummaryViewHolder(holder.itemView)

            viewHolder.image.setImageResource(R.drawable.cupcake)
            viewHolder.name.text = item?.name
            viewHolder.flavorQuantity.text =
                context.getString(R.string.summary_unit_count, item?.quantity.toString())
        }

    }

    override fun getItemCount(): Int {
        return if (fragmentCaller == FLAVOR_CALLER) {
            viewModel.flavors.value?.size!!
        } else {
            viewModel.orderList.value?.size!!
        }
    }

}