package com.example.ybd.data

import com.example.ybd.R

object Repository {

    // Весь "предзагруженный" контент
    private val allItems = listOf<PhotoItem>(
        // Первый блок
        PhotoItem(null, "Смахни меня как вк клип"),
        // Остальной контент

        )

    // Сколько элементов доступно при ПЕРВОМ запуске
    const val INITIAL_ITEM_COUNT = 9

    // Сколько элементов добавлять при каждом "анлоке"
    const val NEW_ITEMS_PER_BATCH = 6

    /**
     * Возвращает ВЕСЬ список. ViewModel сам решит, сколько показать.
     */
    fun getAllItems(): List<PhotoItem> = allItems

    /**
     * Возвращает максимальное количество элементов
     */
    fun getMaxItemCount(): Int = allItems.size
}