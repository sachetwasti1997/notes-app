package com.sachet.notes.data

sealed class NotesOrder(var orderType: OrderType){
    class Title(orderType: OrderType): NotesOrder(orderType)
    class Date(orderType: OrderType): NotesOrder(orderType)
    class Color(orderType: OrderType): NotesOrder(orderType)

    fun copy(orderType: OrderType): NotesOrder {
        return when(this){
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Color -> Color(orderType)
        }
    }
}
