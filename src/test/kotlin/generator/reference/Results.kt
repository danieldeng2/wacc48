package generator.reference

import com.squareup.moshi.Json

data class EmulatorResult(
    @Json(name = "test") val test: String,
    @Json(name = "upload") val upload: String,
    @Json(name = "emulator_out") val emulatorOut: String,
    @Json(name = "emulator_exit") val emulatorExit: Int
)

data class CompilerResult(
    @Json(name = "test") val test: String,
    @Json(name = "upload") val upload: String,
    @Json(name = "compiler_out") val compilerOut: String,
)