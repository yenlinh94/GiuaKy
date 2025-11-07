package com.example.giuaky.data


data class Phone(
    val name: String = "",
    val category: String = "",
    val price: String = "",
    val image: String? = null
)

data class PhoneItem(
    val id: String,
    val name: String,
    val category: String,
    val price: String,
    val image: String?
)

