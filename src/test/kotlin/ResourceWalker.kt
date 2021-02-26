import java.io.File
import kotlin.test.assertEquals

class ResourceWalker {
    private var totalTests = 0
    private var passedTests = 0

    fun walkDirectory(name: String, func: ResourceWalker.(f: File) -> Unit) {
        val dir = File(javaClass.getResource(name).file)
        dir.walk().forEach { f ->
            if (f.isFile) {
                try {
                    func(f)
                } catch (e: Error) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        println("$passedTests/$totalTests programs in $name produced expected result\n\n")
        assertEquals(totalTests, passedTests)
    }

    fun startTest(f: File) {
        println("${f.path}:")
        totalTests++
    }

    fun passTest() {
        println("PASSED")
        passedTests++
    }
}