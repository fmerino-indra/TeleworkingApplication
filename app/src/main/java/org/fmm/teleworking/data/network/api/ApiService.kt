package org.fmm.teleworking.data.network.api

import org.fmm.teleworking.domain.model.DayDto
import org.fmm.teleworking.domain.model.StatsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/years/open")
    suspend fun openYear(@Body req: OpenYearRequest)

    @GET("api/months/{year}/{month}")
    suspend fun getMonth(@Path("year") year: Int, @Path("month") month: Int): List<DayDto>

    @POST("api/years/{year}/festive")
    suspend fun toggleFestive(@Path ("year")year: Int, @Body req: FestiveRequest)

    @POST("api/day")
    suspend fun setModality(@Body req: ModalityRequest): DayDto

    @GET("api/stats/{year}/quarter/{q}")
    suspend fun quarterStats(@Path("year")year: Int, @Path("q")q:Int): StatsDto

    @GET("api/stats/{year}/annual")
    suspend fun annualStats(@Path("year")year: Int): StatsDto
}