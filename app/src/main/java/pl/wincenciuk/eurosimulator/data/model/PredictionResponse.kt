package pl.wincenciuk.eurosimulator.data.model

data class PredictionResponse(
    val id: String,
    val group: String,
    val matchIndex: Int,
    val usersPredictions: List<EuroMatchResult>
)
