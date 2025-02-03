//package com.jeong.sesac.sai.ui.libraryMap.noteList
//
//import android.os.Build
//import android.os.Bundle
//import android.view.View
//import androidx.annotation.RequiresApi
//import androidx.recyclerview.widget.GridLayoutManager
//import com.jeong.sesac.sai.databinding.ItemTabRecyclerBinding
//import com.jeong.sesac.sai.recycler.GridDecoration
//import com.jeong.sesac.sai.recycler.newNote.NewNoteAdapter
//import com.jeong.sesac.sai.util.BaseFragment
//
//class SearchListFragment : BaseFragment<ItemTabRecyclerBinding>(ItemTabRecyclerBinding::inflate) {
//
//    private lateinit var searchListAdapter : NewNoteAdapter
//
//    companion object {
//        fun getInstance(position: Int) =
//            SearchListFragment().apply {
//                arguments = Bundle().apply {
//                    putInt("position", position)
//                }
//            }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        searchListAdapter = NewNoteAdapter { findNoteInfo ->
////            val action =
////                SearchFragmentDirections.actionFragmentSearchToFragmentSearchNoteDetail(findNoteInfo)
////            findNavController().navigate(action)
//        }
//
//        with(binding) {
//            rvContainer.apply {
//                layoutManager = GridLayoutManager(requireContext(), 2)
//                addItemDecoration(GridDecoration(requireContext()))
//                adapter = this@SearchListFragment.searchListAdapter
//            }
//        }
//
//        val filteredList = when(arguments?.getInt(("position")) ?: 0) {
//            0 -> LibraryNoteListMockData.sampleLibraryNotes.sortedByDescending { it.createdAt }
//            1 -> LibraryNoteListMockData.sampleLibraryNotes.sortedByDescending { it.likes }
//            else -> LibraryNoteListMockData.sampleLibraryNotes.sortedBy { it.likes }
//        }
//
//        searchListAdapter.submitList(filteredList)
//    }
//}