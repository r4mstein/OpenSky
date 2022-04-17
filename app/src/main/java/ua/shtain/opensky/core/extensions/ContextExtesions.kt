package ua.shtain.opensky.core.extensions

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ua.shtain.opensky.R

fun Context.makeSnackbar(
    message: Int,
    view: View,
    length: Int = Snackbar.LENGTH_SHORT,
    backgroundTint: Int = R.color.purple_500,
    textColor: Int = R.color.white,
    actionText: Int = -1,
    actionTextColor: Int = R.color.white,
    action: (() -> Unit)? = null,
): Snackbar {
    return Snackbar.make(view, this.getString(message), length)
        .setBackgroundTint(ContextCompat.getColor(this, backgroundTint))
        .setTextColor(ContextCompat.getColor(this, textColor))
        .setActionTextColor(ContextCompat.getColor(this, actionTextColor))
        .also {
            if (action != null) {
                it.setAction(this.getString(actionText)) {
                    action.invoke()
                }.setActionTextColor(
                    ContextCompat.getColor(this, actionTextColor)
                )
            }
        }
}

