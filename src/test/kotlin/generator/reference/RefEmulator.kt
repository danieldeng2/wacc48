package generator.reference

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RefEmulator(
    private val testFile: File,
) {
    private val service = ReferenceAPI.retrofitService

    fun execute(stdin: String): EmulatorResult {
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
        return call.execute().body()!!
    }

}