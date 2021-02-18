package reference

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

class RefCompiler(
    private val testFile: File,
) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://teaching.doc.ic.ac.uk/")
        .build()
    private val service = retrofit.create(WACCReferenceAPI::class.java)

    fun run(): List<String> {
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

        val result = call.execute().body()!!

        return parseOutput(result.compilerOut)
    }

    private fun parseOutput(rawOutput: String): List<String> {
        val lines = rawOutput.lines()

        val first = lines.indexOf("===========================================================") + 1
        val last = lines.lastIndexOf("===========================================================")
        val output = mutableListOf<String>()

        for (i in first until last) {
            output.add(lines[i].substringAfter("\t"))
        }

        return output
    }

}