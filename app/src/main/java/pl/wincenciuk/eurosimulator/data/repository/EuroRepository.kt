package pl.wincenciuk.eurosimulator.data.repository

import pl.wincenciuk.eurosimulator.data.model.EuroMatchResult
import pl.wincenciuk.eurosimulator.data.model.GroupData

interface EuroRepository {
    suspend fun getTeamData(): List<GroupData>
    suspend fun storePrediction(group: String, matchIndex: Int, prediction: EuroMatchResult)
    suspend fun getMatchPredictions(group: String, matchIndex: Int): List<EuroMatchResult>
}