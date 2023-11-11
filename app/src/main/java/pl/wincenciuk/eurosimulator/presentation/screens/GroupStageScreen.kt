package pl.wincenciuk.eurosimulator.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.wincenciuk.eurosimulator.R
import pl.wincenciuk.eurosimulator.components.ScoreInput
import pl.wincenciuk.eurosimulator.components.background_color
import pl.wincenciuk.eurosimulator.components.green_check
import pl.wincenciuk.eurosimulator.components.little_green_check
import pl.wincenciuk.eurosimulator.data.model.EuroMatchResult
import pl.wincenciuk.eurosimulator.data.model.Team
import pl.wincenciuk.eurosimulator.presentation.viewmodel.EuroViewModel

@Composable
fun GroupStageScreen(viewModel: EuroViewModel) {
    val groups = listOf("A", "B", "C", "D", "E", "F")
    val (matchResult, setMatchResult) = remember { mutableStateOf(MutableList(6) { EuroMatchResult(0, 0) })  }
    val groupData by viewModel.groupData.collectAsState(emptyList())
    val (selectedGroup, setSelectedGroup) = remember { mutableStateOf(groups[0]) }

//    val matchResults = groups.map { EuroMatchResult(0, 0) }
//    val (currentGroupMatchResult, setCurrentGroupMatchResult) = remember {
//        mutableStateOf(matchResults.first())
//    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = background_color
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Buttons for changing groups
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                groups.forEach { groupInfo ->
                    Button(
                        onClick = { setSelectedGroup(groupInfo)
//                            val index = groups.indexOf(groupInfo)
//                            setCurrentGroupMatchResult(matchResults[index])
                                  },
                        modifier = Modifier.padding(2.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = groupInfo)
                    }
                }
            }
            val selectedGroupData = groupData.find { it.group == selectedGroup }
            selectedGroupData?.let {groupInfo ->
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
                    TeamTable(groupInfo.teams)
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
                    .padding(bottom = 30.dp)
            ) {
                for (i in 0 until groupInfo.teams.size - 1) {
                    for (j in i + 1 until groupInfo.teams.size) {
                        val teamA = groupInfo.teams[i]
                        val teamB = groupInfo.teams[j]
                        val result = matchResult[i * 2 + j - (i * 1)]
//                        val result = currentGroupMatchResult

                        MatchResultInput(
                            teamA = teamA.shortName,
                            teamB = teamB.shortName,
                            matchResult = result,
                            onResultChanged = { newResult ->
                                val updatedMatchResult = matchResult.toMutableList()
                                updatedMatchResult[i * 2 + j - (i + 1)] = newResult
                                setMatchResult(updatedMatchResult)

//                                val updatedMatchResult = matchResults.toMutableList()
//                                updatedMatchResult[i * 2 + j - (i + 1)] = newResult
//                                setCurrentGroupMatchResult(newResult)

                                if (newResult.scoreA > newResult.scoreB) {
                                    teamA.matchesPlayed++
                                    teamA.matchesWon++
                                    teamA.points += 3
                                    teamB.matchesPlayed++
                                    teamB.matchesLost++
                                } else if (newResult.scoreA < newResult.scoreB) {
                                    teamA.matchesPlayed++
                                    teamA.matchesLost++
                                    teamB.matchesPlayed++
                                    teamB.matchesWon++
                                    teamB.points += 3
                                } else {
                                    // It's a draw
                                    teamA.matchesPlayed++
                                    teamA.matchesDrawn++
                                    teamA.points++
                                    teamB.matchesPlayed++
                                    teamB.matchesDrawn++
                                    teamB.points++
                                }
                            },
                            selectedGroup = selectedGroup,
                            onGroupChange = { setSelectedGroup(groupInfo.group) })
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
    onGroupChange: () -> Unit) {

    val scoreA = rememberSaveable { mutableStateOf(matchResult.scoreA.toString()) }
    val scoreB = rememberSaveable { mutableStateOf(matchResult.scoreB.toString()) }
    val showPredictions = remember { mutableStateOf(false) }
    val buttonEnabled = remember { mutableStateOf(true) }


    Surface(
        modifier = Modifier
            .padding(start = 7.dp, end = 7.dp, top = 20.dp),
        color = Color.Gray,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp), //30
        border = BorderStroke(3.dp, Color.White),
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
                    painterResource(id = R.drawable.flag_poland),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp) //70
                )
                Text(
                    text = teamA,
                    fontSize = 19.sp
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
                    painterResource(id = R.drawable.flag_germany),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = teamB,
                    fontSize = 19.sp
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

                Text(text = "33%", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "34%", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "33%", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
fun TeamTable(teams: List<Team>) {
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
                        painterResource(id = R.drawable.flag_poland),
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
