package com.example.a4ads_11_04


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel : ViewModel() {

    var isLoading = MutableLiveData(false)
        private set

    var isSuccess = MutableLiveData<List<Musica>>(emptyList())
        private set

    var isError = MutableLiveData(false)
        private set

    var errorMessage = MutableLiveData("")
        private set


    fun getAllMusicas() {

        viewModelScope.launch {
            try {
                isLoading.value = true
                val musicaRepository = ApiConfig.getInstance().create(MusicasService::class.java)
                val response = musicaRepository.getAll()
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    isSuccess.value = list
                    isLoading.value = false
                    isError.value = false
                } else {
                    throw Exception("Erro desconhecido")
                }

            } catch (e: HttpException) {
                val message = when (e.code()) {
                    400 -> "Musica não encontrada"
                    404 -> "Parametros incorretos"
                    else -> "Erro desconhecido"
                }
                isError.value = true
                errorMessage.value = message
                isLoading.value = false
            } catch (e: Exception) {
                isError.value = true
                errorMessage.value = e.message
                isLoading.value = false
            }
        }
    }

}