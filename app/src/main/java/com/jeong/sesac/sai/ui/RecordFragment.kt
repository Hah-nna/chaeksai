import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRecordBinding
import com.jeong.sesac.sai.ui.adapters.RecordPagerAdapter
import com.jeong.sesac.sai.util.BaseFragment

class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupButtonGroup()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = RecordPagerAdapter(this)
    }

    private fun setupButtonGroup() {
        val buttons = listOf(
            binding.buttonRegisteredNotes,
            binding.buttonCompletedFinds,
            binding.buttonBookmarkedNotes
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                binding.viewPager.currentItem = index
            }
        }

        // ViewPager 페이지 변경에 따라 버튼 활성화 상태 업데이트
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                buttons.forEachIndexed { index, button ->
                    button.isSelected = index == position
                    button.setBackgroundColor(
                        if (index == position) requireContext().getColor(R.color.primary)
                        else requireContext().getColor(R.color.black)
                    )
                }
            }
        })
    }
}
