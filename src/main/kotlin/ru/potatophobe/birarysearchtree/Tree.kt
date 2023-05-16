package ru.potatophobe.birarysearchtree

interface Tree<T : Comparable<T>> {

    fun put(element: T)

    fun contains(element: T): Boolean

    fun remove(element: T): Boolean
}
