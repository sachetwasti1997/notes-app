package com.sachet.notes.util

sealed class OrderType{
    object Ascending : OrderType()
    object Descending: OrderType()
}
