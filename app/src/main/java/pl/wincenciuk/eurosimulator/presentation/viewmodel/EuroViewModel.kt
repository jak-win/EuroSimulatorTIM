package pl.wincenciuk.eurosimulator.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.wincenciuk.eurosimulator.data.ApiService
import pl.wincenciuk.eurosimulator.data.model.GroupData
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

    init {
        loadGroupData()
    }

    private fun loadGroupData(){
        viewModelScope.launch {
            try {
                val response = groupService.getTeamData()
                _groupData.value = response
                Log.d("ViewModel", response.toString())

            } catch (e: java.lang.Exception){
                Log.e("EuroViewModel", "${e.message}")
            }
        }
    }



}