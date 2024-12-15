import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRecordBinding
import com.jeong.sesac.sai.ui.adapters.RecordPagerAdapter
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            setupViewPager()
            setupButtonGroup()
        }
    }

    private fun FragmentRecordBinding.setupViewPager() {
        viewPager.adapter = RecordPagerAdapter(this@RecordFragment)
    }

    private fun FragmentRecordBinding.setupButtonGroup() {
        val buttons = listOf(buttonRegisteredNotes, buttonCompletedFinds, buttonBookmarkedNotes)

        // Corbind를 사용하여 버튼 클릭 이벤트 처리
        val buttonClicks = buttons.mapIndexed { index, button ->

            // 클릭 시 해당 버튼의 index 반환
            button.clicks().map { index }

            // 여러 클릭 이벤트 Flow 병합
        }.merge()

        lifecycleScope.launch {
            buttonClicks.collect { index ->

                // 클릭된 버튼의 index에 따라 ViewPager 페이지 변경
                viewPager.currentItem = index
            }
        }

        // ViewPager 페이지 변경에 따른 버튼 상태 업데이트
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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