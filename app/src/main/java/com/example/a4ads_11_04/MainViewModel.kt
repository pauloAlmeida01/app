package com.example.a4ads_11_04


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: MusicasService) : ViewModel() {

    var state = MutableLiveData<MainScreenState>(MainScreenState.Loading)
        private set


    fun getAllMusicas() {
        viewModelScope.launch {
            try {
                state.value = MainScreenState.Loading
                val musicaRepository = ApiConfig.getInstance().create(MusicasService::class.java)
                val response = musicaRepository.getAll()
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    state.value = MainScreenState.Success(data = list)
                } else {
                    throw Exception("Erro desconhecido")
                }

            } catch (e: HttpException) {
                val message = when (e.code()) {
                    400 -> "Musica não encontrada"
                    404 -> "Parametros incorretos"
                    else -> "Erro desconhecido"
                }
                state.value = MainScreenState.Error(message)
            } catch (e: Exception) {
                state.value = MainScreenState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }

}