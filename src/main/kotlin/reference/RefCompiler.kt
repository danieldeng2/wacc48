package reference

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File

class RefCompiler(
    private val testFile: File,
) {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl("https://teaching.doc.ic.ac.uk/")
        .build()
    private val service = retrofit.create(WACCReferenceAPI::class.java)

    fun compile(): String {
        val call = service.compileFile(
            testFile = MultipartBody.Part.createFormData(
                "testfile",
                testFile.name,
                RequestBody.create(MediaType.parse("application/octet-stream"), testFile)
            ),
            options = MultipartBody.Part.createFormData(
                "options[]",
                "-a"
            ),
            stdin = null
        )

        val result = call.execute()

        return result.message()
    }

    fun execute(stdin: String): String {
        val call = service.compileFile(
            testFile = MultipartBody.Part.createFormData(
                "testfile",
                testFile.name,
                RequestBody.create(MediaType.parse("application/octet-stream"), testFile)
            ),
            options = MultipartBody.Part.createFormData(
                "options[]",
                "-x",
            ),
            stdin = MultipartBody.Part.createFormData(
                "stdin",
                stdin,
            )
        )


        return call.execute().body()!!
    }

}

fun main() {
    val testFile = File("src/test/resources/valid/while/fibonacciIterative.wacc")
    val message = RefCompiler(testFile).execute("")

    println(message)
}