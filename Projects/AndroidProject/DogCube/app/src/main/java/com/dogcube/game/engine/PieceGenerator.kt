package com.dogcube.game.engine

import kotlin.random.Random

class PieceGenerator(seed: Long = System.currentTimeMillis()) {

    private val random = Random(seed)
    private val bag = mutableListOf<PieceType>()

    /** Reset the generator (new seed, new bag). */
    fun reset(seed: Long = System.currentTimeMillis()) {
        bag.clear()
    }

    /** Get next piece using 7-bag algorithm. */
    fun next(): PieceType {
        if (bag.isEmpty()) {
            bag.addAll(PieceType.entries)
            bag.shuffle(random)
        }
        return bag.removeAt(bag.lastIndex)
    }

    /** Peek the next piece without consuming it. */
    fun peek(): PieceType {
        val p = next()
        bag.add(p)
        return p
    }
}
