package pl.wincenciuk.eurosimulator.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.wincenciuk.eurosimulator.data.service.ApiService
import pl.wincenciuk.eurosimulator.data.repository.EuroRepository
import pl.wincenciuk.eurosimulator.data.repository.EuroRepositoryImpl
import pl.wincenciuk.eurosimulator.data.service.PredictionApiService
import pl.wincenciuk.eurosimulator.presentation.viewmodel.EuroViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module {
    single<ApiService> {
        Retrofit.Builder()
        .baseUrl("https://jak-win.github.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
    }

    single<PredictionApiService> {
        Retrofit.Builder()
        .baseUrl("http://developmenteuroapp.eba-bv7dbuqb.eu-north-1.elasticbeanstalk.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PredictionApiService::class.java)
    }

    single<EuroRepository> {
        EuroRepositoryImpl(get(), get())
    }
    viewModel {
        EuroViewModel(get())
    }
}