package pl.wincenciuk.eurosimulator.data.model

data class Predictions(val predictions: List<EuroMatchResult>){
    constructor() : this(emptyList())
}
