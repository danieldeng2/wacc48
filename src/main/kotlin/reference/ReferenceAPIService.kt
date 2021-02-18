package reference

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ReferenceAPIService {
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

private const val BASE_URL = "https://teaching.doc.ic.ac.uk/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object ReferenceAPI {
    val retrofitService: ReferenceAPIService by lazy { retrofit.create(ReferenceAPIService::class.java) }
}