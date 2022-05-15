package com.sachet.notes.model

sealed class OrderType{
    object Ascending : OrderType()
    object Descending: OrderType()
}
