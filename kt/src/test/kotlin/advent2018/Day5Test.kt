package advent2018

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day5Test {
    @Test fun testOppositeCase() {
        assertTrue { isOppositeCase("c", "C") }
        assertFalse { isOppositeCase("c", "c") }
    }

    @Test fun testReduce() {
        assertEquals("dabCBAcaDA", reduce("dabAcCaCBAcCcaDA"))
    }
}