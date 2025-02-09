package com.jeong.sesac.sai.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jeong.sesac.sai.databinding.CommentModalBottomSheetBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class CommentModalBottomSheet(
    private val context: LifecycleCoroutineScope,
    private val isCommentUser: Boolean,
    private val onEditClick: () -> Unit,
    private val onDeleteClick: () -> Unit,
    private val onReportClick: () -> Unit,
) : BottomSheetDialogFragment() {

    private var _binding: CommentModalBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CommentModalBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // comment 작성자인 경우
            if (isCommentUser) {
                tvEditComment.isVisible = true
                tvDeleteComment.isVisible = true
                tvReportComment.isVisible = false

                tvEditComment.clicks().onEach {
                    onEditClick()
                    dismiss()
                }.launchIn(context)

                tvDeleteComment.clicks().onEach {
                    onDeleteClick()
                    dismiss()
                }.launchIn(context)

            } else {
                tvEditComment.isVisible = false
                tvDeleteComment.isVisible = false
                tvReportComment.isVisible = true

                tvReportComment.clicks().onEach {
                    onReportClick()
                    dismiss()
                }.launchIn(context)

            }
            tvClose.clicks().onEach {
                dismiss()
            }.launchIn(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
