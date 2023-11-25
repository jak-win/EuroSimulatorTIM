package pl.wincenciuk.eurosimulator.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.wincenciuk.eurosimulator.data.ApiService
import pl.wincenciuk.eurosimulator.data.model.GroupData
import pl.wincenciuk.eurosimulator.data.model.Team
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EuroViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://jak-win.github.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val groupService = retrofit.create(ApiService::class.java)

    private val _groupData = MutableStateFlow<List<GroupData>>(emptyList())
    val groupData: Flow<List<GroupData>> = _groupData.asStateFlow()

    private val _allAdvancingTeams = MutableStateFlow<List<Team>>(emptyList())
    val allAdvancingTeams: Flow<List<Team>> = _allAdvancingTeams.asStateFlow()

    fun addAdvancingTeams(teams: List<Team>) {
        val currentTeams = _allAdvancingTeams.value.toMutableList()
        currentTeams.addAll(teams)
        _allAdvancingTeams.value = currentTeams
        Log.d("EuroViewModel", "CurrentTopTeams: $currentTeams")
    }


     fun loadGroupData(){
        viewModelScope.launch {
            try {
                val response = groupService.getTeamData()
                _groupData.value = response

            } catch (e: java.lang.Exception){
                Log.e("EuroViewModel", "${e.message}")
            }
        }
    }

}