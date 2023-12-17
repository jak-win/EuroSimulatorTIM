package pl.wincenciuk.eurosimulator.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.getViewModel
import pl.wincenciuk.eurosimulator.R
import pl.wincenciuk.eurosimulator.components.PoInputField
import pl.wincenciuk.eurosimulator.components.background_color
import pl.wincenciuk.eurosimulator.components.background_color2
import pl.wincenciuk.eurosimulator.presentation.viewmodel.EuroViewModel

@Composable
fun PlayoffScreen(viewModel: EuroViewModel) {

    val winnersFirstRound by viewModel.winnersFirstRound.collectAsState()
    val winnersSecondRound by viewModel.winnersSecondRound.collectAsState()
    val winnersThirdRound by viewModel.winnersThirdRound.collectAsState()
    val champion by viewModel.champion.collectAsState()

    val allAdvancedTeams by viewModel.allAdvancingTeams.collectAsState(emptyList())
    Log.d("PlayoffScreen", "TopTeams: $allAdvancedTeams")

    LaunchedEffect(winnersFirstRound, winnersSecondRound, winnersThirdRound) {

    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = background_color
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Brush.linearGradient(colors = listOf(
                background_color, background_color2)))
        ) {
            Image(painterResource(id = R.drawable.knockout), contentDescription = "Playoff header")
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                //   1/8
                if (allAdvancedTeams.isNotEmpty()) {
                    Column(modifier = Modifier.padding(start = 15.dp)) {
                        SingleMatchComponent(allAdvancedTeams[0].shortName, allAdvancedTeams[5].shortName) { winner ->
                            viewModel.updateWinnersFirstRound(index = 0, winner) }

                        SingleMatchComponent(allAdvancedTeams[1].shortName, allAdvancedTeams[4].shortName) { winner ->
                            viewModel.updateWinnersFirstRound(index = 1, winner) }

                        SingleMatchComponent(allAdvancedTeams[2].shortName, allAdvancedTeams[3].shortName) { winner ->
                            viewModel.updateWinnersFirstRound(index = 2, winner) }

                        SingleMatchComponent(allAdvancedTeams[6].shortName, allAdvancedTeams[11].shortName) { winner ->
                            viewModel.updateWinnersFirstRound(index = 3, winner) }

                        SingleMatchComponent(allAdvancedTeams[7].shortName, allAdvancedTeams[10].shortName) { winner ->
                            viewModel.updateWinnersFirstRound(index = 4, winner) }

                        SingleMatchComponent(allAdvancedTeams[8].shortName, allAdvancedTeams[9].shortName) { winner ->
                            viewModel.updateWinnersFirstRound(index = 5, winner) }

                        SingleMatchComponent(allAdvancedTeams[12].shortName, allAdvancedTeams[16].shortName) { winner ->
                            viewModel.updateWinnersFirstRound(index = 6, winner) }

                        SingleMatchComponent(allAdvancedTeams[13].shortName, allAdvancedTeams[15].shortName) { winner ->
                            viewModel.updateWinnersFirstRound(index = 7, winner) }
                        }
                    }

                // 1/4
                Column(modifier = Modifier.padding(start = 15.dp)) {
                    for (i in 0 until 8 step 2) {
                        val index = i / 2
                        SingleMatchComponent(
                            teamA = winnersFirstRound[i],
                            teamB = winnersFirstRound[i + 1],
                            onNextRound = { winner ->
                                viewModel.updateWinnersSecondRound(index, winner)
                            }
                        )
                    }
                }
                // 1/2
                Column(
                    modifier = Modifier.padding(start = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(id = R.drawable.medals),
                        contentDescription = "medals",
                        modifier = Modifier.size(80.dp)
                    )
                    for (i in 0 until 4 step 2) {
                        val index = i / 2
                        SingleMatchComponent(
                            teamA = winnersSecondRound[i],
                            teamB = winnersSecondRound[i + 1],
                            onNextRound = { winner ->
                                viewModel.updateWinnersThirdRound(index, winner)
                            }
                        )
                    }
                }
                //  Final
                Column(
                    modifier = Modifier.padding(start = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(id = R.drawable.trophy_drawing),
                        contentDescription = "trophy",
                        modifier = Modifier.size(60.dp)
                    )
                    SingleMatchComponent(teamA = winnersThirdRound[0], teamB = winnersThirdRound[1]) {winner -> viewModel.updateChampion(winner)}
                }
            }
        }
    }
}

@Composable
private fun SingleMatchComponent(
    teamA: String,
    teamB: String,
    onNextRound: (String) -> Unit,
) {

    val scoreA = rememberSaveable() { mutableStateOf("") }
    val scoreB = rememberSaveable() { mutableStateOf("") }
    val viewModel = getViewModel<EuroViewModel>()
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .size(width = 140.dp, height = 77.dp)
            .padding(3.dp),
        color = Color.White,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, Color.Gray),
        elevation = 5.dp
    ) {
        Column() {
            Row(
                modifier = Modifier.padding(3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painterResource(id = viewModel.getCountryFlag(teamA)),
                    contentDescription = "flag",
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = teamA,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.size(width = 5.dp, height = 1.dp))
                PoInputField(
                    valueState = scoreA,
                    modifier = Modifier.padding(end = 10.dp),
                )
            }
            Row(
                modifier = Modifier.padding(3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painterResource(id = viewModel.getCountryFlag(teamB)),
                    contentDescription = "flag",
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = teamB,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.size(width = 5.dp, height = 1.dp))
                PoInputField(
                    valueState = scoreB,
                    modifier = Modifier.padding(end = 10.dp),
                )
            }
        }
    }
    LaunchedEffect(scoreA.value, scoreB.value, context) {
        if (scoreA.value.isNotEmpty() && scoreB.value.isNotEmpty()) {
            try {
                val winner = viewModel.getWinner(scoreA.value, scoreB.value, teamA, teamB)
                onNextRound(winner)
            } catch (e: java.lang.IllegalArgumentException) {
                Toast.makeText(context, "You must declare the winner of the match", Toast.LENGTH_SHORT).show()
            }

        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}