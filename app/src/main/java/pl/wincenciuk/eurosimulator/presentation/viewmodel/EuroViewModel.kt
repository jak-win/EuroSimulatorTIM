package pl.wincenciuk.eurosimulator.presentation.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pl.wincenciuk.eurosimulator.R
import pl.wincenciuk.eurosimulator.data.model.EuroMatchResult
import pl.wincenciuk.eurosimulator.data.model.GroupData
import pl.wincenciuk.eurosimulator.data.model.Predictions
import pl.wincenciuk.eurosimulator.data.model.Team
import pl.wincenciuk.eurosimulator.data.repository.EuroRepository

class EuroViewModel(private val repository: EuroRepository) : ViewModel() {


    private val auth: FirebaseAuth = Firebase.auth
    private val firestoreRef = FirebaseFirestore.getInstance()

    private val _loading = MutableStateFlow(false)
    val loading: Flow<Boolean> = _loading

    private val _groupData = MutableStateFlow<List<GroupData>>(emptyList())
    val groupData: Flow<List<GroupData>> = _groupData.asStateFlow()

    private val _allAdvancingTeams = MutableStateFlow<List<Team>>(emptyList())
    val allAdvancingTeams: Flow<List<Team>> = _allAdvancingTeams.asStateFlow()

    private val _selectedGroup = MutableStateFlow("A")
    val selectedGroup: StateFlow<String> = _selectedGroup

    private val _winnersFirstRound = MutableStateFlow(listOf("QF1", "QF2", "QF3", "QF4", "QF5", "QF6", "QF7", "QF8"))
    val winnersFirstRound: StateFlow<List<String>> = _winnersFirstRound

    private val _winnersSecondRound = MutableStateFlow(listOf("SF1", "SF2", "SF3", "SF4"))
    val winnersSecondRound: StateFlow<List<String>> = _winnersSecondRound

    private val _winnersThirdRound = MutableStateFlow(listOf("F1", "F2"))
    val winnersThirdRound: StateFlow<List<String>> = _winnersThirdRound

    private val _champion = MutableStateFlow("winner")
    val champion: StateFlow<String> = _champion


    fun createUserWithEmailAndPassword(email: String, password: String, context: Context, navigate: () -> Unit)
    = viewModelScope.launch {
        if (!_loading.value) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { task ->
                    Log.d("Firebase", "signInWithEmailAndPassword: User logged in! $task")
                    navigate()
                }
                .addOnFailureListener { task ->
                    Toast.makeText(context, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show()
                    Log.d("Firebase", "signInWithEmailAndPassword: User not logged in! $task")

                }
                    _loading.value = false
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, context: Context, navigate: () -> Unit)
    = viewModelScope.launch {

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { task ->
                    Log.d("Firebase", "signInWithEmailAndPassword: User logged in! $task")
                    navigate()
                }
                .addOnFailureListener { task ->
                    Toast.makeText(context, "Incorrect login details", Toast.LENGTH_SHORT).show()
                    Log.d("Firebase", "signInWithEmailAndPassword: User not logged in! $task")

                }
    }
    suspend fun loadGroupData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getTeamData()
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
            "ALB" -> R.drawable.flag_albania
            "SLO" -> R.drawable.flag_slovakia
            "SPA" -> R.drawable.flag_spain
            "ITA" -> R.drawable.flag_italy
            "GRE" -> R.drawable.flag_greece
            "SWI" -> R.drawable.flag_switzerland
            "TUR" -> R.drawable.flag_turkey
            "BEL" -> R.drawable.flag_belgium3
            "DEN" -> R.drawable.flag_denmark
            "ROM" -> R.drawable.flag_romania
            "SRB" -> R.drawable.flag_serbia
            "NED" -> R.drawable.flag_netherlands
            "AUS" -> R.drawable.flag_austria
            "UKR" -> R.drawable.flag_ukraine
            "SVN" -> R.drawable.flag_slovenia
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

    fun storePrediction(
        group: String,
        matchIndex: Int,
        prediction: EuroMatchResult
    ) {
        val matchRef = firestoreRef.collection("predictions").document(group).collection("matches").document("match$matchIndex")
        matchRef.update("predictions", FieldValue.arrayUnion(prediction))
//        matchRef.set(prediction)
    }

    suspend fun getMatchPredictions(group: String, matchIndex: Int): List<EuroMatchResult> {
        return try {
            val snapShot = firestoreRef.collection("predictions").document(group).collection("matches").document("match$matchIndex").get().await()
            val predictions = snapShot.toObject(Predictions::class.java)?.predictions ?: emptyList()
            Log.d("VM Firestore", "$predictions")
            predictions

        } catch (e: Exception) {
            Log.e("VM Firestore", "Error getting predictions: ${e.message}")
            emptyList()
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
        viewModelScope.launch {
            _selectedGroup.value = group
        }
    }


    fun updateWinnersFirstRound(index: Int, winner: String) {
        viewModelScope.launch {
            _winnersFirstRound.value =
                winnersFirstRound.value.toMutableList().apply { set(index, winner) }
        }
    }

    fun updateWinnersSecondRound(index: Int, winner: String) {
        viewModelScope.launch {
            _winnersSecondRound.value =
                winnersSecondRound.value.toMutableList().apply { set(index, winner) }
        }
    }

    fun updateWinnersThirdRound(index: Int, winner: String) {
        viewModelScope.launch {
            _winnersThirdRound.value =
                winnersThirdRound.value.toMutableList().apply { set(index, winner) }
        }
    }

    fun updateChampion(winner: String) {
        viewModelScope.launch {
            _champion.value = winner
        }
    }
     fun getWinner(scoreA: String, scoreB: String, teamA: String, teamB: String): String {
        return if (scoreA.toInt() > scoreB.toInt()){
            teamA
        } else if (scoreA.toInt() < scoreB.toInt()) {
            teamB
        } else {
            throw java.lang.IllegalArgumentException("Mus")
        }

    }

}