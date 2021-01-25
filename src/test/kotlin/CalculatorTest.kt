import org.junit.Test
import kotlin.test.assertEquals

class CalculatorTest {
    @Test
    fun testMultiply2By2() {
        val calculator = Calculator()
        val result = calculator.parse("2 * 2")
        assertEquals(4, result)
    }

    @Test
    fun testDivide2By2() {
        val calculator = Calculator()
        val result = calculator.parse("2 / 2")
        assertEquals(1, result)
    }
}