package reference

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Part


interface WACCReferenceAPI {
    @Multipart
    @POST("/wacc_compiler/run.cgi")
    fun compileFile(
        @Part testFile: MultipartBody.Part,
        @Part options: MultipartBody.Part?,
        @Part stdin: MultipartBody.Part?,
    ): Call<CompilerResult>

    @Multipart
    @POST("/wacc_compiler/emulate.cgi")
    fun emulate(
        @Part testFile: MultipartBody.Part,
        @Part stdin: MultipartBody.Part,
    ): Call<EmulatorResult>
}