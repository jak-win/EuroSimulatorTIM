package pl.wincenciuk.eurosimulator.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.wincenciuk.eurosimulator.R
import pl.wincenciuk.eurosimulator.data.ApiService
import pl.wincenciuk.eurosimulator.data.model.EuroMatchResult
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

    private val _selectedGroup = MutableStateFlow("A")
    val selectedGroup: StateFlow<String> = _selectedGroup


    suspend fun loadGroupData(){
        withContext(Dispatchers.IO) {
            try {
                val response = withContext(Dispatchers.IO) {
                    groupService.getTeamData()
                }
                _groupData.value = response
            } catch (e: java.lang.Exception){
                Log.e("EuroViewModel", "${e.message}")
            }
        }
    }

    fun getCountryFlag(shortName: String): Int {
        return when (shortName) {
            "POL" -> R.drawable.flag_poland
            "GER" -> R.drawable.flag_germany
            "SWE" -> R.drawable.flag_sweden
            "SLO" -> R.drawable.flag_slovakia
            "SPA" -> R.drawable.flag_spain
            "ITA" -> R.drawable.flag_italy
            "WAL" -> R.drawable.flag_wales
            "SWI" -> R.drawable.flag_switzerland
            "TUR" -> R.drawable.flag_turkey
            "BEL" -> R.drawable.flag_belgium3
            "DEN" -> R.drawable.flag_denmark
            "NOR" -> R.drawable.flag_norway
            "FIN" -> R.drawable.flag_finland
            "NED" -> R.drawable.flag_netherlands
            "AUS" -> R.drawable.flag_austria
            "UKR" -> R.drawable.flag_ukraine
            "MAC" -> R.drawable.flag_macedonia
            "ENG" -> R.drawable.flag_england
            "CRO" -> R.drawable.flag_croatia
            "CZE" -> R.drawable.flag_czech
            "SCO" -> R.drawable.flag_scotland
            "FRA" -> R.drawable.flag_france
            "POR" -> R.drawable.flag_portugal
            "HUN" -> R.drawable.flag_hungary
            else -> R.drawable.eu_flag
        }
    }

    fun addAdvancingTeams(teams: List<Team>) {
        viewModelScope.launch {
        val currentTeams = _allAdvancingTeams.value.toMutableList()
        currentTeams.addAll(teams)
        _allAdvancingTeams.value = currentTeams
        Log.d("EuroViewModel", "CurrentTopTeams: $currentTeams")
        }
    }

    suspend fun processMatchResult(
        teams: List<Team>,
        matchResult: EuroMatchResult,
        teamAIndex: Int,
        teamBIndex: Int
    ) {
        withContext(Dispatchers.IO) {
            val teamA = teams[teamAIndex]
            val teamB = teams[teamBIndex]
                // Team A win
            if (matchResult.scoreA > matchResult.scoreB) {
                updateTeamStats(teamA, teamB)
                // Team B win
            } else if (matchResult.scoreA < matchResult.scoreB) {
                updateTeamStats(teamB, teamA)
            } else {
                // It's a draw
                updateTeamStats(teamA, teamB, isDraw = true)
            }
        }
    }

    private fun updateTeamStats(winner: Team, loser: Team, isDraw: Boolean = false) {
        loser.matchesPlayed++
        winner.matchesPlayed++
        if (!isDraw) {
            winner.matchesWon++
            loser.matchesLost++
            winner.points += 3
        } else {
            winner.matchesDrawn++
            winner.points++
            loser.matchesDrawn++
            loser.points++
        }
    }

    fun setSelectedGroup(group: String) {
        _selectedGroup.value = group
    }


}