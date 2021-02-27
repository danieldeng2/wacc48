package analyser

import analyser.nodes.expr.BoolLiteral
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SymbolTableTest {
    private val symbolTable = SymbolTable(null)

    @Test
    fun addedNodeIsReturnedByGet() {
        val node = BoolLiteral(true, null)
        symbolTable.add("Boolean True", node)

        assertEquals(symbolTable["Boolean True"], node)
    }

    @Test
    fun getReturnsNullForUnknownKey() {
        assertNull(symbolTable["Boolean True"])
    }

    @Test
    fun containsCurrentScopeReturnsTrueForAddedKey() {
        val node = BoolLiteral(true, null)
        symbolTable.add("Boolean True", node)
        assertTrue(symbolTable.containsInCurrentScope("Boolean True"))
    }

    @Test
    fun containsCurrentScopeReturnsFalseForUnknownKey() {
        assertFalse(symbolTable.containsInCurrentScope("Boolean True"))
    }

    @Test
    fun containsAnyScopeReturnsTrueForKeyInParent() {
        val childTable = SymbolTable(symbolTable)
        val node = BoolLiteral(true, null)
        symbolTable.add("Boolean True", node)

        assertTrue(childTable.containsInAnyScope("Boolean True"))
    }

    @Test
    fun containsCurrentScopeReturnsFalseForKeyInParent() {
        val childTable = SymbolTable(symbolTable)
        val node = BoolLiteral(true, null)
        symbolTable.add("Boolean True", node)

        assertFalse(childTable.containsInCurrentScope("Boolean True"))
    }
}