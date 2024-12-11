import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRecordBinding
import com.jeong.sesac.sai.ui.adapters.RecordPagerAdapter
import com.jeong.sesac.sai.util.BaseFragment

class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayoutWithViewPager()
    }

    private fun setupTabLayoutWithViewPager() {
        val tabTitles = listOf(
            getString(R.string.my_registered_notes),
            getString(R.string.completed_finds),
            getString(R.string.bookmarkednotes)
        )

        binding.viewPager.adapter = RecordPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
