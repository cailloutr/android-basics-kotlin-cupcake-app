/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.cupcake.data.Datasource
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Price for a single cupcake */
const val PRICE_PER_CUPCAKE = 2.00

/** Additional cost for same day pickup of an order or delivery */
const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

/** Pickup option for in store pickup */
const val IN_STORE_OPTION = 1

/** Pickup option for delivery */
const val DELIVERY_OPTION = 2

/** Base location for comparison */
val EMPTY_LOCATION = Location("", "", "", "")



/**
 * [OrderViewModel] holds information about a cupcake order in terms of quantity, flavor, and
 * pickup date. It also knows how to calculate the total price based on these order details.
 */
class OrderViewModel : ViewModel() {

    // TODO: especial edition: animation in the layout
    // TODO: update the layout's codes that don't use data binding


    // Name of the person making the order
    private val _clientName = MutableLiveData<String>()
    val clientName: LiveData<String> = _clientName

    // Phone number of the person making the order
    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    // Clients address for delivery option
    private val _address = MutableLiveData<Location>()
    val address: LiveData<Location> = _address

    // Quantity of cupcakes in this order
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    // Cupcake flavor for this order
    private val _flavors = MutableLiveData<List<Flavor>>()
    val flavors: LiveData<List<Flavor>> = _flavors

    // Order's observations
    private val _observations = MutableLiveData<String>()
    val observations: LiveData<String> = _observations

    // Possible date options
    val dateOptions: List<String> = getPickupOptions()

    // Pickup date
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    // Price of the order so far
    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        // Format the price into the local currency and return this as LiveData<String>
        NumberFormat.getCurrencyInstance().format(it)
    }

    // Delivery = 2 or in store pickup = 1
    private val _pickupOption = MutableLiveData<Int>()
    val pickupOption: LiveData<Int> = _pickupOption

    // Order list containing exclusively the cupcakes ordered
    private val _orderList = MutableLiveData<List<Flavor>>()
    val orderList: LiveData<List<Flavor>> = _orderList

    init {
        // Set initial values for the order
        resetOrder()
    }


    /**
     * Populate the list with only the item ordered
     */
    fun setOrderList() {
        val list = mutableListOf<Flavor>()

        for (flavor in flavors.value!!) {
            if (flavor in list) {
                if (flavor.quantity == 0) {
                    list.remove(flavor)
                }
            } else {
                if (flavor.quantity > 0) {
                    list.add(flavor)
                }
            }
        }

        _orderList.value = list
    }

    /**
     * Set the pickup option for this order.
     *
     * @param address to order delivery option
     * @param city to order delivery option
     * @param state to order delivery option
     * @param zipCode to order delivery option
     */
    fun setLocation(address: String, city: String, state: String, zipCode: String) {
        //_address.value = Location(address, city, state, zipCode)
        _address.value?.address = address
        _address.value?.city = city
        _address.value?.state = state
        _address.value?.zipCode = zipCode
    }

    /**
     * Set the pickup option for this order.
     *
     * @param option to order
     */
    fun setPickupOption(option: Int) {
        _pickupOption.value = option
        updatePrice()
    }

    /**
     * Set the observations for this order.
     *
     * @param observations to order
     */
    fun setObservations(observations: String) {
        _observations.value = observations
    }

    /**
     * Set the name of the client for this order.
     *
     * @param nameOfClient to order
     */
    fun setName(nameOfClient: String) {
        _clientName.value = nameOfClient
    }

    /**
     * Set the phone number of the client for this order.
     *
     * @param phoneNumber to client
     */
    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    /**
     * Set the quantity of cupcakes for this order.
     *
     * @param numberCupcakes to order
     */
    fun setUnitQuantity(flavor: Flavor, numberCupcakes: Int) {
        flavor.quantity = numberCupcakes
        updatePrice()
    }


    /**
     * Set the total quantity of cupcakes for this order.
     */
    private fun setTotalQuantity() {
        var quantity = 0
        for (flavor in flavors.value!!) {
            quantity += flavor.quantity
        }
        _quantity.value = quantity
    }

    /**
     * Set the pickup date for this order.
     *
     * @param pickupDate is the date for pickup as a string
     */
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    /**
     * Returns true if a flavor has not been selected for the order yet. Returns false otherwise.
     */
    fun hasNoFlavorSet(): Boolean {
        return _flavors.value.isNullOrEmpty()
    }

    /**
     * Reset the order by using initial default values for the quantity, flavor, date, and price.
     */
    fun resetOrder() {
        _quantity.value = 0
        _flavors.postValue(Datasource.loadFlavor())
        _date.value = dateOptions[0]
        _price.value = 0.0
        _clientName.value = ""
        _phoneNumber.value = ""
        _pickupOption.value = IN_STORE_OPTION
        _address.value = Location()
    }

    /**
     * Updates the price based on the order details.
     */
    private fun updatePrice() {

        setTotalQuantity()
        var calculatedPrice: Double = quantity.value?.times(PRICE_PER_CUPCAKE) ?: 0.0
        Log.d("OrderViewModel", "${quantity.value}")

        if (calculatedPrice != 0.0) {
            // If the user selected the first option (today) for pickup or delivery, add the surcharge
            if ((dateOptions[0] == _date.value) || pickupOption.value == 2) { // 2 = Delivery
                calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
            }
        }

        _price.value = calculatedPrice
    }

    /**
     * Returns a list of date options starting with the current date and the following 3 dates.
     */
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    fun verifyPickupOption(): Int {
        return if (pickupOption.value == DELIVERY_OPTION)
            DELIVERY_OPTION
        else
            IN_STORE_OPTION
    }
}