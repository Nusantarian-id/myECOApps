package id.myeco.myeco.presentation.ui.fragment.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import id.myeco.myeco.core.repository.EspDataDataSource
import id.myeco.myeco.core.source.Resource
import id.myeco.myeco.core.source.remote.response.GetDataResponse

class MainViewModel(private val mainDataSource: EspDataDataSource) : ViewModel() {
    fun getData(ssid: String, pass: String, userId: String): LiveData<Resource<String>> =
        LiveDataReactiveStreams.fromPublisher(mainDataSource.getData(ssid, pass, userId))
}