package com.abir.hasan.apod.ui.apodlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel

class APODListAdapter(private val onItemClick: (PlanetaryUiModel) -> Unit) :
    ListAdapter<PlanetaryUiModel, APODItemViewHolder>(APOD_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): APODItemViewHolder {
        return APODItemViewHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: APODItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        val APOD_DIFF = object : DiffUtil.ItemCallback<PlanetaryUiModel>() {
            override fun areItemsTheSame(
                oldItem: PlanetaryUiModel,
                newItem: PlanetaryUiModel
            ): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(
                oldItem: PlanetaryUiModel,
                newItem: PlanetaryUiModel
            ): Boolean = oldItem == newItem

        }
    }

}
