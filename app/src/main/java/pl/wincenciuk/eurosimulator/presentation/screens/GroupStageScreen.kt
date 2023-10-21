package pl.wincenciuk.eurosimulator.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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

@Composable
fun GroupStageScreen() {
    val groups = listOf("A", "B", "C", "D", "E", "F")

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
                groups.forEach { group ->
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(2.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = group)
                    }
                }
            }
            // Group Table
            Text(
                text = "Group A",
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
                Column() {
                    TeamTable(
                        teams = listOf(
                            Team("Poland", 3, 2, 1, 0, 7),
                            Team("Germany", 3, 2, 0, 1, 6),
                            Team("France", 3, 1, 1, 1, 4),
                            Team("Spain", 3, 0, 0, 3, 0)
                        )
                    )
                }
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
                MatchResultInput(teamA = "POL", teamB = "SPA")
                MatchResultInput(teamA = "GER", teamB = "FRA")
                MatchResultInput(teamA = "POL", teamB = "SPA")
                MatchResultInput(teamA = "GER", teamB = "FRA")
                MatchResultInput(teamA = "POL", teamB = "SPA")
                MatchResultInput(teamA = "GER", teamB = "FRA")
            }
        }
    }
}

data class Team(
    val name: String,
    val matchesPlayed: Int,
    val matchesWon: Int,
    val matchesDrawn: Int,
    val matchesLost: Int,
    val points: Int
)

@Composable
fun TeamTable(teams: List<Team>) {
    Column(modifier = Modifier.padding(3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Country",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(150.dp)
            )
            Text(
                "M",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(50.dp)
            )
            Text(
                "W",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(50.dp)
            )
            Text(
                "D",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(50.dp)
            )
            Text(
                "L",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(50.dp)
            )
            Text(
                "Pts",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(50.dp)
            )
        }


        teams.forEachIndexed { index, team ->
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
                    Text(
                        team.matchesPlayed.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(50.dp)
                    )
                    Text(
                        team.matchesWon.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(50.dp)
                    )
                    Text(
                        team.matchesDrawn.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(50.dp)
                    )
                    Text(
                        team.matchesLost.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(50.dp)
                    )
                    Text(
                        team.points.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(50.dp)
                    )
                }
                Divider(thickness = 2.dp)

            }
        }
    }
}

@Composable
fun MatchResultInput(teamA: String, teamB: String) {
    val scoreA = rememberSaveable() { mutableStateOf("") }
    val scoreB = rememberSaveable() { mutableStateOf("") }
    Surface(
        modifier = Modifier
            .padding(7.dp)
            .padding(top = 10.dp),
        color = Color.LightGray,
        shape = RoundedCornerShape(40.dp), //30
        border = BorderStroke(3.dp, Color.White),
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 30.dp)
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
                enabled = true
            )
            Text(text = ":", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            ScoreInput(scoreState = scoreB)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 30.dp)
            ) {

                Image(
                    painterResource(id = R.drawable.flag_poland),
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
}