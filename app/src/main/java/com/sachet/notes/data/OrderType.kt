package com.sachet.notes.data

sealed class OrderType{
    object Ascending : OrderType()
    object Descending: OrderType()
}
