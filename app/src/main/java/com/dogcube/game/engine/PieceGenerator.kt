package com.dogcube.game.engine


import kotlin.random.Random


class PieceGenerator(seed: Long = System.currentTimeMillis()) {
    private val random = Random(seed)
    private val bag = mutableListOf<PieceType>()


    fun next(): PieceType {
        if (bag.isEmpty()) { bag.addAll(PieceType.entries); bag.shuffle(random) }
        return bag.removeAt(bag.lastIndex)
    }


    fun peek(): PieceType { val p = next(); bag.add(p); return p }
    fun reset() { bag.clear() }
}
