package com.abir.hasan.apod.ui.apoddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel
import com.abir.hasan.apod.databinding.FragmentApodDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class APODDetailsFragment : Fragment() {

    companion object {
        const val KEY_PLANETARY_ITEM = "KEY_PLANETARY_ITEM"
    }

    private var _binding: FragmentApodDetailsBinding? = null

    private val binding: FragmentApodDetailsBinding
        get() = _binding!!

    private var planetaryUiModel: PlanetaryUiModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApodDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getItemDetails()
        setUpUi()
    }

    private fun setUpUi() {
        planetaryUiModel?.let {
            binding.ivApod.load(it.hdUrl ?: it.url) {
                crossfade(true)
            }
            binding.tvTitle.text = it.title
            binding.tvDate.text = it.date
            binding.tvExplanation.text = it.explanation
        }
    }

    private fun getItemDetails() {
        planetaryUiModel = arguments?.getParcelable(KEY_PLANETARY_ITEM)
    }
}