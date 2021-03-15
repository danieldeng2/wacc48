import java.io.File
import kotlin.test.assertEquals

class WalkDirectory(private val name: String) {
    private var totalTests = 0
    private var passedTests = 0

    fun run(func: WalkDirectory.(f: File) -> Unit) {
        val dir = File(javaClass.getResource(name).file)
        dir.walk().forEach { f ->
            if (f.isFile && f.name.endsWith(".wacc")) {
                startTest(f)
                try {
                    func(f)
                    passTest()
                } catch (e: AssertionError) {
                    System.err.println(e.message)
                } catch (e: Error) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                println("")
            }
        }
        println("$passedTests/$totalTests programs in $name produced expected result\n\n")
        assertEquals(totalTests, passedTests)
    }

    private fun startTest(f: File) {
        println("${f.path}:")
        totalTests++
    }

    private fun passTest() {
        println("PASSED")
        passedTests++
    }
}