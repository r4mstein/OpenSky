package ua.shtain.opensky.core.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity?.addFragment(containerId: Int, fragment: Fragment) {
    this?.supportFragmentManager
        ?.beginTransaction()
        ?.add(containerId, fragment)
        ?.commit()
}

fun AppCompatActivity?.replaceFragment(containerId: Int, fragment: Fragment) {
    this?.supportFragmentManager
        ?.beginTransaction()
        ?.replace(containerId, fragment)
        ?.commit()
}

fun AppCompatActivity?.replaceFragmentAndAddToBackStack(containerId: Int, fragment: Fragment) {
    this?.supportFragmentManager
        ?.beginTransaction()
        ?.replace(containerId, fragment)
        ?.addToBackStack(null)
        ?.commit()
}