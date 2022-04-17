package ua.shtain.opensky.states.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ua.shtain.opensky.R
import ua.shtain.opensky.databinding.ItemStateBinding
import ua.shtain.opensky.states.models.State
import javax.inject.Inject

class StatesAdapter @Inject constructor() :
    ListAdapter<State, StatesAdapter.StatesViewHolder>(StatesItemCallback()) {

    class StatesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemStateBinding = ItemStateBinding.bind(view)

        fun bind(data: State) {
            binding.apply {
                tvTransponder.text = data.transponder
                tvCallsign.text = data.callsign
                tvCountry.text = data.country
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatesViewHolder {
        return StatesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_state, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}