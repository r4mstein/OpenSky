package ua.shtain.opensky.core.extensions

import android.view.View

fun View.showView() {
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
}

fun View.hideView() {
    if (this.visibility != View.GONE) {
        this.visibility = View.GONE
    }
}