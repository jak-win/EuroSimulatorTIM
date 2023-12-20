package pl.wincenciuk.eurosimulator.data.service

import pl.wincenciuk.eurosimulator.data.model.GroupData
import retrofit2.http.GET

interface ApiService {
    @GET("jsonapi/teams.json")
    suspend fun getTeamData(): List<GroupData>
}