package com.abir.hasan.apod.ui.apodlist

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abir.hasan.apod.R
import com.abir.hasan.apod.data.api.model.PlanetaryUiModel
import com.abir.hasan.apod.databinding.FragmentApodListBinding
import com.abir.hasan.apod.ui.BaseFragment
import com.abir.hasan.apod.ui.apoddetails.APODDetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class APODListFragment : BaseFragment() {

    private var _binding: FragmentApodListBinding? = null

    private val binding: FragmentApodListBinding
        get() = _binding!!

    private val viewModel: APODListViewModel by viewModels()

    private val mAdapter = APODListAdapter(::onPlanetaryListItemClick)

    private var errorInfo: ErrorInfo? = null

    private lateinit var sortType: SortType

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApodListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
        getData()
    }

    private fun setupUi() {
        with(binding.rvAPOD) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
        binding.fabSort.setOnClickListener {
            val reorderDialog = ReorderDialog(sortType)
            reorderDialog.onReorderSelection = ::onReorderTypeSelection
            reorderDialog.show(childFragmentManager, "")
        }
    }

    private fun getData() {
        viewModel.refreshList()
    }

    private fun setupObservers() {
        viewModel.apodUiState.observe(viewLifecycleOwner) { uiState ->
            updateList(uiState.items)
            handleError(uiState.errorInfo)
            sortType = uiState.sortType
            binding.progressbar.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun updateList(items: List<PlanetaryUiModel>) {
        mAdapter.submitList(items)
        binding.fabSort.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun handleError(it: ErrorInfo?) {
        errorInfo = it
        it?.let { info ->
            binding.errorView.root.visibility = View.VISIBLE
            binding.errorView.tvErrorTitle.text = getString(info.errorTitle)
            binding.errorView.tvErrorMessage.text = getString(info.errorMessage)
            if (info.hasNetwork) {
                binding.errorView.btnErrorAction.text = getString(R.string.refresh)
            } else {
                binding.errorView.btnErrorAction.text = getString(R.string.network_settings)
            }
            binding.errorView.btnErrorAction.setOnClickListener {
                if (binding.errorView.btnErrorAction.text == getString(R.string.refresh)) {
                    viewModel.refreshList()
                } else {
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
            }
        } ?: run {
            binding.errorView.root.visibility = View.GONE
        }
    }

    private fun onReorderTypeSelection(sortType: SortType) {
        viewModel.setFiltering(sortType)
    }

    private fun onPlanetaryListItemClick(item: PlanetaryUiModel) {
        val bundle = Bundle().apply {
            putParcelable(APODDetailsFragment.KEY_PLANETARY_ITEM, item)
        }
        findNavController().navigate(R.id.action_apodListFragment_to_apodDetailsFragment, bundle)
    }

    override fun onNetworkStatusChange(isOnline: Boolean) {
        super.onNetworkStatusChange(isOnline)
        // This is done cause after network settings action
        // List has to be updated automatically
        // Also end point doesn't get called twice in the initial stage that's why
        // errorInfo is also checked
        if (isOnline && errorInfo != null)
            getData()
    }

}