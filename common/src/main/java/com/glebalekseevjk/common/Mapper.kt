package com.glebalekseevjk.common

interface Mapper<ITEM, ANOTHER_ITEM> {
    fun mapItemToAnotherItem(item: ITEM): ANOTHER_ITEM
    fun mapAnotherItemToItem(anotherItem: ANOTHER_ITEM): ITEM
}