package reference

import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

class RefEmulator(
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

    fun execute(stdin: String): String {
        val call = service.emulate(
            testFile = MultipartBody.Part.createFormData(
                "testfile",
                testFile.name,
                RequestBody.create(MediaType.parse("application/octet-stream"), testFile)
            ),
            stdin = MultipartBody.Part.createFormData(
                "stdin",
                stdin,
            )
        )
        return call.execute().body()!!.toString()
    }

}