package com.abir.hasan.apod.ui.apodlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel
import com.abir.hasan.apod.databinding.AdapterApodItemBinding

class APODItemViewHolder(
    private val binding: AdapterApodItemBinding,
    private val onItemClick: (PlanetaryUiModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: PlanetaryUiModel

    init {
        binding.root.setOnClickListener { onItemClick.invoke(item) }
    }

    fun onBind(model: PlanetaryUiModel) {
        item = model
        binding.tvApodDate.text = model.date
        binding.tvApodTitle.text = model.title
        binding.ivApod.load(model.hdUrl ?: model.url) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (PlanetaryUiModel) -> Unit): APODItemViewHolder {
            val binding = AdapterApodItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return APODItemViewHolder(binding, onItemClick)
        }
    }

}