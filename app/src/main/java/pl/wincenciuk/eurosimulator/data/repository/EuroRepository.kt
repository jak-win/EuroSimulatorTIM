package pl.wincenciuk.eurosimulator.data.repository

import pl.wincenciuk.eurosimulator.data.model.GroupData

interface EuroRepository {
    suspend fun getTeamData(): List<GroupData>
}