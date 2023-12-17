package pl.wincenciuk.eurosimulator.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.wincenciuk.eurosimulator.data.ApiService
import pl.wincenciuk.eurosimulator.data.model.GroupData

class EuroRepositoryImpl(private val groupService: ApiService): EuroRepository {
    override suspend fun getTeamData(): List<GroupData> {
        return withContext(Dispatchers.IO) {
            groupService.getTeamData()
        }
    }
}