package com.dogcube.game.engine

import org.junit.Assert.*
import org.junit.Test

class PieceTest {

    @Test
    fun allSevenPiecesExist() {
        assertEquals(7, PieceType.entries.size)
    }

    @Test
    fun eachPieceHasFourRotationStates() {
        PieceType.entries.forEach { piece ->
            assertEquals("${piece.name} should have 4 rotation states", 4, piece.shapes.size)
        }
    }

    @Test
    fun oPieceAllStatesIdentical() {
        val o = PieceType.O
        val first = o.shapes[0]
        for (i in 1..3) {
            assertArrayEquals(first, o.shapes[i])
        }
    }

    @Test
    fun eachPieceHasDogBreed() {
        PieceType.entries.forEach { piece ->
            assertNotNull(piece.breed)
        }
    }
}

private fun assertArrayEquals(a: Array<IntArray>, b: Array<IntArray>) {
    assertEquals("Row count", a.size, b.size)
    a.indices.forEach { i ->
        assertArrayEquals("Row $i", a[i], b[i])
    }
}
