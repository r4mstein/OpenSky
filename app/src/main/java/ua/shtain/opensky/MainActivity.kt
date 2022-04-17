package ua.shtain.opensky

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ua.shtain.opensky.core.base.BaseActivity
import ua.shtain.opensky.core.navigator.NavigatorApi
import ua.shtain.opensky.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var navigator: NavigatorApi

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        navigator.showStatesFragment(
            activity = this, containerId = R.id.fragmentContainer,
        )
    }
}