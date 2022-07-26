package com.abir.hasan.apod.ui.apodlist

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.abir.hasan.apod.R
import com.abir.hasan.apod.databinding.DialogReorderBinding

class ReorderDialog(
    private val sortType: SortType = SortType.INITIAL_RESULT
) : DialogFragment() {

    private var _binding: DialogReorderBinding? = null

    private val binding: DialogReorderBinding
        get() = _binding!!

    private var selectedOrder: SortType? = null

    var onReorderSelection: ((SortType) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogReorderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullScreen()

        when (sortType) {
            SortType.INITIAL_RESULT -> {}
            SortType.REORDER_BY_DATE_DESCENDING -> binding.rbSortByDate.isChecked = true
            SortType.REORDER_BY_TITLE_ASCENDING -> binding.rbSortByTitle.isChecked = true
        }

        binding.rgSort.setOnCheckedChangeListener { _, id ->
            selectedOrder = when (id) {
                R.id.rbSortByDate -> SortType.REORDER_BY_DATE_DESCENDING
                R.id.rbSortByTitle -> SortType.REORDER_BY_TITLE_ASCENDING
                else -> null
            }
        }

        binding.btnApply.setOnClickListener {
            onReorderSelection?.invoke(selectedOrder ?: SortType.INITIAL_RESULT)
            dismiss()
        }

        binding.btnReset.setOnClickListener {
            onReorderSelection?.invoke(SortType.INITIAL_RESULT)
            dismiss()
        }

        binding.rootLayout.setOnClickListener {
            dismiss()
        }
    }

    private fun fullScreen() {
        try {
            val window = dialog?.window!!
            window.setBackgroundDrawableResource(android.R.color.transparent)
            val layoutParams = window.attributes
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            window.attributes = layoutParams
            // To make Status-bar transparent
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}