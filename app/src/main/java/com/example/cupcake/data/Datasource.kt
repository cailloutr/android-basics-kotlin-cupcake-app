package com.example.cupcake.data

import com.example.cupcake.R
import com.example.cupcake.model.Flavor

object Datasource {

    fun loadFlavor(): List<Flavor> {
        return listOf(
            Flavor(1,"Vanilla",0),
            Flavor(2,"Chocolate", 0),
            Flavor(3,"Red Velvet", 0),
            Flavor(4,"Salted Caramel", 0),
            Flavor(5,"Coffee", 0))
    }
}