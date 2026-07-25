package com.dogcube.game.engine

import com.dogcube.game.model.DogBreed

enum class PieceType(
    val breed: DogBreed,
    /** Rotation states: list of 4 matrices, each as List<IntArray> [y][x], 1=filled */
    val shapes: List<Array<IntArray>>
) {
    I(DogBreed.DACHSHUND, listOf(
        arrayOf(intArrayOf(0,0,0,0), intArrayOf(1,1,1,1), intArrayOf(0,0,0,0), intArrayOf(0,0,0,0)),
        arrayOf(intArrayOf(0,0,1,0), intArrayOf(0,0,1,0), intArrayOf(0,0,1,0), intArrayOf(0,0,1,0)),
        arrayOf(intArrayOf(0,0,0,0), intArrayOf(0,0,0,0), intArrayOf(1,1,1,1), intArrayOf(0,0,0,0)),
        arrayOf(intArrayOf(0,1,0,0), intArrayOf(0,1,0,0), intArrayOf(0,1,0,0), intArrayOf(0,1,0,0))
    )),
    O(DogBreed.CORGI, listOf(
        arrayOf(intArrayOf(1,1), intArrayOf(1,1)),
        arrayOf(intArrayOf(1,1), intArrayOf(1,1)),
        arrayOf(intArrayOf(1,1), intArrayOf(1,1)),
        arrayOf(intArrayOf(1,1), intArrayOf(1,1))
    )),
    T(DogBreed.FRENCHIE, listOf(
        arrayOf(intArrayOf(0,1,0), intArrayOf(1,1,1), intArrayOf(0,0,0)),
        arrayOf(intArrayOf(0,1,0), intArrayOf(0,1,1), intArrayOf(0,1,0)),
        arrayOf(intArrayOf(0,0,0), intArrayOf(1,1,1), intArrayOf(0,1,0)),
        arrayOf(intArrayOf(0,1,0), intArrayOf(1,1,0), intArrayOf(0,1,0))
    )),
    S(DogBreed.SHIBA, listOf(
        arrayOf(intArrayOf(0,1,1), intArrayOf(1,1,0), intArrayOf(0,0,0)),
        arrayOf(intArrayOf(0,1,0), intArrayOf(0,1,1), intArrayOf(0,0,1)),
        arrayOf(intArrayOf(0,0,0), intArrayOf(0,1,1), intArrayOf(1,1,0)),
        arrayOf(intArrayOf(1,0,0), intArrayOf(1,1,0), intArrayOf(0,1,0))
    )),
    Z(DogBreed.HUSKY, listOf(
        arrayOf(intArrayOf(1,1,0), intArrayOf(0,1,1), intArrayOf(0,0,0)),
        arrayOf(intArrayOf(0,0,1), intArrayOf(0,1,1), intArrayOf(0,1,0)),
        arrayOf(intArrayOf(0,0,0), intArrayOf(1,1,0), intArrayOf(0,1,1)),
        arrayOf(intArrayOf(0,1,0), intArrayOf(1,1,0), intArrayOf(1,0,0))
    )),
    J(DogBreed.GOLDEN, listOf(
        arrayOf(intArrayOf(1,0,0), intArrayOf(1,1,1), intArrayOf(0,0,0)),
        arrayOf(intArrayOf(0,1,1), intArrayOf(0,1,0), intArrayOf(0,1,0)),
        arrayOf(intArrayOf(0,0,0), intArrayOf(1,1,1), intArrayOf(0,0,1)),
        arrayOf(intArrayOf(0,1,0), intArrayOf(0,1,0), intArrayOf(1,1,0))
    )),
    L(DogBreed.BORDER_COLLIE, listOf(
        arrayOf(intArrayOf(0,0,1), intArrayOf(1,1,1), intArrayOf(0,0,0)),
        arrayOf(intArrayOf(0,1,0), intArrayOf(0,1,0), intArrayOf(0,1,1)),
        arrayOf(intArrayOf(0,0,0), intArrayOf(1,1,1), intArrayOf(1,0,0)),
        arrayOf(intArrayOf(1,1,0), intArrayOf(0,1,0), intArrayOf(0,1,0))
    ))
}
