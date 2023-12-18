package pl.wincenciuk.eurosimulator.presentation.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pl.wincenciuk.eurosimulator.components.*
import pl.wincenciuk.eurosimulator.data.model.EuroMatchResult
import pl.wincenciuk.eurosimulator.data.model.Team
import pl.wincenciuk.eurosimulator.presentation.navigation.AppScreens
import pl.wincenciuk.eurosimulator.presentation.viewmodel.EuroViewModel
import kotlin.math.roundToInt

@SuppressLint("SuspiciousIndentation")
@Composable
fun GroupStageScreen(viewModel: EuroViewModel, navController: NavController) {
    val groups = listOf("A", "B", "C", "D", "E", "F")
    val (matchResult, setMatchResult) = remember { mutableStateOf(MutableList(6) { EuroMatchResult(0, 0) })  }
    val groupData by viewModel.groupData.collectAsState(emptyList())
    val selectedGroup by viewModel.selectedGroup.collectAsState("A")

    LaunchedEffect(key1 = Unit) {
        viewModel.loadGroupData()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = background_color
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(
                Brush.linearGradient(
                    colors = listOf(
                        background_color, background_color2
                    )
                )
            )
        ) {
            if (groupData.isEmpty()) {
                EuroLoader()
            } else {
                Card(
                    modifier = Modifier
                        .padding(9.dp)
                        .padding(bottom = 10.dp, top = 10.dp),
                    backgroundColor = Color.Gray,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(3.dp, Color.White),
                    elevation = 15.dp
                ) {
                    Text(
                        text = "Below there are all 6 matches of a given group.\n Please, complete them all, then click the next button to move on",
                        modifier = Modifier.padding(4.dp),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                val selectedGroupData = groupData.find { it.group == selectedGroup }
                selectedGroupData?.let { groupInfo ->
                    // Group Table
                    Text(
                        text = "Group ${groupInfo.group}",
                        color = Color.White,
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center
                    )
                    Surface(
                        modifier = Modifier
                            .padding(7.dp)
                            .padding(top = 10.dp),
                        color = Color.Gray,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp, Color.Gray),
                        elevation = 10.dp
                    ) {
                        TeamTable(groupInfo.teams, viewModel)
                    }
                    // Fields to fill the results
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Enter the match results: ",
                        color = Color.White,
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (i in 0 until groupInfo.teams.size - 1) {
                            for (j in i + 1 until groupInfo.teams.size) {
                                val teamA = groupInfo.teams[i]
                                val teamB = groupInfo.teams[j]
                                val matchIndex = (i * (2 * 4 - i - 1)) / 2 + j - i - 1
                                val result = matchResult[matchIndex]

                                MatchResultInput(
                                    teamA = teamA.shortName,
                                    teamB = teamB.shortName,
                                    matchResult = result,
                                    onResultChanged = { newResult ->
                                        val updatedMatchResult = matchResult.toMutableList()
                                        updatedMatchResult[(i * (2 * 4 - i - 1)) / 2 + j - i - 1] = newResult
                                        setMatchResult(updatedMatchResult)

                                        viewModel.viewModelScope.launch {
                                        viewModel.processMatchResult(teams = groupInfo.teams, matchResult = newResult, teamAIndex =  i, teamBIndex =  j)
                                            viewModel.storePrediction(groupInfo.group, matchIndex, newResult)
                                        }
                                    },
                                    selectedGroup = selectedGroup,
                                    viewModel = viewModel,
                                    matchIndex = matchIndex,
                                    onGroupChange = { viewModel.setSelectedGroup(groupInfo.group) })
                            }
                        }

                        Button(
                            onClick = {
                                val currentIndex = groups.indexOf(selectedGroup)
                                val currentGroupTeams =
                                    groupData.find { it.group == selectedGroup }?.teams
                                val top3Teams =
                                    currentGroupTeams?.sortedByDescending { it.points }?.take(3)

                                if (currentIndex < groups.size - 1) {
                                    val nextGroup = groups[currentIndex + 1]
                                    if (top3Teams != null) {
                                        viewModel.addAdvancingTeams(top3Teams)
                                    }
                                    viewModel.setSelectedGroup(nextGroup)

                                } else {
                                    if (top3Teams != null) {
                                        viewModel.addAdvancingTeams(top3Teams)
                                    }
                                    navController.navigate(AppScreens.PlayoffScreen.name)
                                }
                            },
                            modifier = Modifier.padding(top = 30.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Gray,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Next", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MatchResultInput(
    teamA: String,
    teamB: String,
    matchResult: EuroMatchResult,
    onResultChanged: (EuroMatchResult) -> Unit,
    selectedGroup: String,
    matchIndex: Int,
    viewModel: EuroViewModel,
    onGroupChange: () -> Unit) {

    val scoreA = rememberSaveable { mutableStateOf(matchResult.scoreA.toString()) }
    val scoreB = rememberSaveable { mutableStateOf(matchResult.scoreB.toString()) }
    val showPredictions = remember { mutableStateOf(false) }
    val buttonEnabled = remember { mutableStateOf(true) }

    val teamAPercentageState = remember { mutableStateOf(0) }
    val teamBPercentageState = remember { mutableStateOf(0) }
    val drawPercentageState = remember { mutableStateOf(0) }

    LaunchedEffect(selectedGroup ) {
        val predictions = viewModel.getMatchPredictions(selectedGroup, matchIndex)
        val totalPredictions = predictions.size
        val teamAPercentage = ((predictions.count { it.scoreA > it.scoreB }.toFloat() / totalPredictions) * 100).roundToInt()
        val teamBPercentage = ((predictions.count { it.scoreA < it.scoreB }.toFloat() / totalPredictions) * 100).roundToInt()
        val drawPercentage = ((predictions.count { it.scoreA == it.scoreB }.toFloat() / totalPredictions) * 100).roundToInt()

        teamAPercentageState.value = teamAPercentage
        teamBPercentageState.value = teamBPercentage
        drawPercentageState.value = drawPercentage

        Log.d("Percentage", "total: $totalPredictions, A: $teamAPercentage, B: $teamBPercentage, Draw: $drawPercentage")
    }

    Surface(
        modifier = Modifier
            .padding(start = 7.dp, end = 7.dp, top = 20.dp),
        color = Color.LightGray,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp), //30
        border = BorderStroke(4.dp, Color.White),
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(start = 15.dp, end = 15.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Image(
                    painterResource(id =  viewModel.getCountryFlag(teamA)),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp) //70
                )
                Text(
                    text = teamA,
                    fontSize = 20.sp
                )
            }
            ScoreInput(
                scoreState = scoreA,
                onAction = KeyboardActions { FocusRequester.Default.requestFocus() },
                enabled = true,
                onScoreChanged = {newScore ->
                    scoreA.value = newScore
                }
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = ":",
                    modifier = Modifier.padding(top = 40.dp,bottom = 12.dp),
                    textAlign = TextAlign.Center)
                Button(
                    onClick = {
                              onResultChanged(EuroMatchResult(scoreA.value.toIntOrNull() ?: 0, scoreB.value.toIntOrNull() ?: 0))
                              showPredictions.value = true
                              buttonEnabled.value = false
                              },
                    modifier = Modifier.size(34.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = green_check, contentColor = Color.White, disabledBackgroundColor = little_green_check),
                    enabled = buttonEnabled.value) {
                    Text(text = "✓", fontSize = 12.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }

            ScoreInput(scoreState = scoreB,
                onScoreChanged = { newScore ->
                    scoreB.value = newScore
                })
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 8.dp)
            ) {

                Image(
                    painterResource(id = viewModel.getCountryFlag(teamB)),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = teamB,
                    fontSize = 20.sp
                )
            }
        }
    }
    Surface(modifier = Modifier.padding(start = 7.dp, end = 7.dp),
        color = Color.White,
        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp), //30
        elevation = 10.dp) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        if (!showPredictions.value){
        Text(
            text = "Confirm the result to see other players predictions",
            modifier = Modifier.padding(7.dp),
            textAlign = TextAlign.Center)
        } else {

                Text(text = "${teamAPercentageState.value}%", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "${drawPercentageState.value}%", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "${teamBPercentageState.value}%", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
    DisposableEffect(selectedGroup) {
        onDispose {
            // Reset score and enable button when the group changes
            scoreA.value = "0"
            scoreB.value = "0"
            showPredictions.value = false
            buttonEnabled.value = true
        }
    }
}
@Composable
fun TeamTable(teams: List<Team>, viewModel: EuroViewModel) {
    val sortedTeams = teams.sortedByDescending { it.points }

    Column(modifier = Modifier.padding(3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val headerTitles = listOf("M", "W", "D", "L", "Pts")
            Text(
                "Country",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(150.dp)
            )
            headerTitles.forEach { titles ->
                Text(
                    text = titles,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(50.dp)
                )
            }
        }

        sortedTeams.forEachIndexed { index, team ->
            Surface(color = Color.White) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(id = viewModel.getCountryFlag(team.shortName)),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        team.name,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .width(120.dp)
                            .padding(start = 10.dp)
                    )
                    listOf(
                        team.matchesPlayed,
                        team.matchesWon,
                        team.matchesDrawn,
                        team.matchesLost,
                        team.points).forEach { value ->
                        Text(
                            text = value.toString(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(50.dp)
                        )
                    }
                }
                Divider(thickness = 2.dp)
            }
        }
    }
}