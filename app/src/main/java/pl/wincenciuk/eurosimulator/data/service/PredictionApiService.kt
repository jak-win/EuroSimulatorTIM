package pl.wincenciuk.eurosimulator.data.service

import pl.wincenciuk.eurosimulator.data.model.EuroMatchResult
import pl.wincenciuk.eurosimulator.data.model.PredictionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PredictionApiService {
    @GET("api/Predictions/{group}/{matchIndex}")
    suspend fun getPredictions(
        @Path("group") group: String,
        @Path("matchIndex") matchIndex: Int
    ): PredictionResponse

    @POST("api/Predictions/{group}/{matchIndex}")
    suspend fun storePrediction(
        @Path("group") group: String,
        @Path("matchIndex") matchIndex: Int,
        @Body prediction: EuroMatchResult
    )
}