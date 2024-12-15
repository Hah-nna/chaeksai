package com.jeong.sesac.sai.ui.myPage

import ReviewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.Review
import com.jeong.sesac.sai.databinding.FragmentReceivedReviewsBinding
import com.jeong.sesac.sai.util.RECEIVED_REVIEW_TOOLBAR_TITLE

class ReceivedReviewsFragment : Fragment() {

    private var _binding: FragmentReceivedReviewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceivedReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.toolbar.toolbarView){
            title = RECEIVED_REVIEW_TOOLBAR_TITLE
        setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        // Set up RecyclerView
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewsRecyclerView.adapter = ReviewAdapter(getDummyReviews())
    }

    private fun getDummyReviews(): List<Review> {
        // Replace with actual data fetching logic
        return listOf(
            Review(R.drawable.ic_profile, "User1", "Great app!"),
            Review(R.drawable.ic_profile, "User2", "Loved it!"),
            Review(R.drawable.ic_profile, "User3", "Needs improvement.")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
