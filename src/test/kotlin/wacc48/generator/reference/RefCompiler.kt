package wacc48.generator.reference


import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RefCompiler(
    private val testFile: File,
) {
    private val service = ReferenceAPI.retrofitService

    fun run(): List<String> {
        val call = service.compileFile(
            testFile = MultipartBody.Part.createFormData(
                "testfile",
                testFile.name,
                RequestBody.create(
                    MediaType.parse("application/octet-stream"),
                    testFile
                )
            ),
            assemblyOption = MultipartBody.Part.createFormData(
                "options[]",
                "-a"
            ),
            stackOption = MultipartBody.Part.createFormData(
                "options[]",
                "-S"
            ),
            stdin = null
        )

        val result = call.execute().body()!!

        return parseOutput(result.compilerOut)
    }

    private fun parseOutput(rawOutput: String): List<String> {
        val lines = rawOutput.lines()

        val first =
            lines.indexOf("===========================================================") + 1
        val last =
            lines.lastIndexOf("===========================================================")

        return mutableListOf<String>().apply {
            for (i in first until last) {
                add(lines[i].substringAfter("\t"))
            }
        }
    }

}