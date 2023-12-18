package pl.wincenciuk.eurosimulator.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pl.wincenciuk.eurosimulator.data.ApiService
import pl.wincenciuk.eurosimulator.data.model.EuroMatchResult
import pl.wincenciuk.eurosimulator.data.model.GroupData
import pl.wincenciuk.eurosimulator.data.model.Predictions

class EuroRepositoryImpl(private val groupService: ApiService): EuroRepository {

    private val firestoreRef = FirebaseFirestore.getInstance()
    override suspend fun getTeamData(): List<GroupData> {
        return withContext(Dispatchers.IO) {
            groupService.getTeamData()
        }
    }

    override suspend fun storePrediction(
        group: String,
        matchIndex: Int,
        prediction: EuroMatchResult
    ) {
        val matchRef = firestoreRef
            .collection("predictions")
            .document(group)
            .collection("matches")
            .document("match$matchIndex")
        matchRef.update("predictions", FieldValue.arrayUnion(prediction))
//        matchRef.set(prediction)
    }

    override suspend fun getMatchPredictions(
        group: String,
        matchIndex: Int
    ): List<EuroMatchResult> {
        return try {
            val documentSnapshot = firestoreRef
                .collection("predictions")
                .document(group)
                .collection("matches")
                .document("match$matchIndex")
                .get()
                .await()

            val predictions = documentSnapshot.toObject(Predictions::class.java)?.predictions ?: emptyList()
            predictions
        } catch (e: Exception) {
            Log.e("EuroRepositoryImpl", "Error getting match predictions: ${e.message}")
            emptyList()
        }
    }
}