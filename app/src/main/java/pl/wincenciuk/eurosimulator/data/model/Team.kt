package pl.wincenciuk.eurosimulator.data.model

data class Team(
    val name: String,
    val shortName: String,
    var matchesPlayed: Int = 0,
    var matchesWon: Int = 0,
    var matchesDrawn: Int = 0,
    var matchesLost: Int = 0,
    var points: Int = 0,
)
