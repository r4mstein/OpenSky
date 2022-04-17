package ua.shtain.opensky.states.adapters

import androidx.recyclerview.widget.DiffUtil
import ua.shtain.opensky.states.models.State

class StatesItemCallback : DiffUtil.ItemCallback<State>() {

    override fun areItemsTheSame(oldItem: State, newItem: State): Boolean {
        return oldItem.transponder == newItem.transponder
    }

    override fun areContentsTheSame(oldItem: State, newItem: State): Boolean {
        return oldItem.transponder == newItem.transponder &&
                oldItem.callsign == newItem.callsign &&
                oldItem.country == newItem.country
    }
}