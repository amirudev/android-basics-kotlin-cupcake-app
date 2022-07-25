package com.example.cupcake.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2
private const val COST_DELIVERY_SAMEDAY = 3
private const val COST_DELIVERY = 3

class OrderViewModel : ViewModel() {
    private val _quantity = MutableLiveData<Int>()
    private val _flavor = MutableLiveData<String>()
    private val _date = MutableLiveData<String>()
    private val _price = MutableLiveData<Int>()
    private val _dateOptions = getPickupOptions()

    val quantity: LiveData<Int> = _quantity
    val flavor: LiveData<String> = _flavor
    val date: LiveData<String> = _date
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }
    val dateOptions: List<String> = _dateOptions

    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()

        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        Log.d("OrderViewModel", calendar.toString())

        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return options
    }

    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[1]
        _price.value = 0
    }

    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE + COST_DELIVERY

        if (_date.value == dateOptions[0]) {
            calculatedPrice += COST_DELIVERY_SAMEDAY
        }

        _price.value = calculatedPrice
    }
}