package org.fmm.teleworking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import org.fmm.teleworking.ui.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import org.fmm.teleworking.ui.MainScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm = hiltViewModel<MainViewModel>()
            MainScreen(vm)
        }
    }
}