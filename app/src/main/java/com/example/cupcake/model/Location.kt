package com.example.cupcake.model

class Location(){
    var address: String = ""
    var city: String = ""
    var state: String = ""
    var zipCode: String = ""

    constructor(address: String, city: String, state: String, zipCode: String) : this() {
        this.address = address
        this.city = city
        this.state = state
        this.zipCode = zipCode
    }

    override fun toString(): String {
        return "$address, $city - $state, CEP: $zipCode"
    }
}