package com.jeong.sesac.sai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.LoginInfo
import com.jeong.sesac.sai.databinding.ItemLoginManagementListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class LoginInfoAdapter(private val loginInfoList: List<LoginInfo>) :
    RecyclerView.Adapter<LoginInfoAdapter.LoginInfoViewHolder>() {

    inner class LoginInfoViewHolder(private val binding: ItemLoginManagementListBinding) :
        RecyclerView.ViewHolder(binding.root), CoroutineScope {

        /**
         * 지영: 왜 이렇게 사용하는지 이해는 못했음
         */
        // ViewHolder에서 사용할 CoroutineScope 정의
        private val job = Dispatchers.Main.immediate
        override val coroutineContext = job

        fun bind(loginInfo: LoginInfo) {
            with(binding) {
                // 회사 이름 및 로그인 날짜 설정
                loginCompany.text = loginInfo.companyName
                signInDate.text = loginInfo.signInDate

                // 연결 버튼 상태 초기화
                updateConnectionButtonState(loginInfo)

                // Corbind의 Flow를 사용해 클릭 이벤트 처리
                connectionBtn.clicks()
                    .onEach {
                        // 클릭 시 연결 상태 반전
                        loginInfo.isConnected = !loginInfo.isConnected
                        updateConnectionButtonState(loginInfo) // 버튼 상태 업데이트
                    }
                    .launchIn(this@LoginInfoViewHolder) // CoroutineScope에 연결
            }
        }

        /**
         * 연결 버튼의 상태를 업데이트
         * @param loginInfo 현재 항목의 데이터 객체
         */
        private fun updateConnectionButtonState(loginInfo: LoginInfo) {
            with(binding.connectionBtn) {
                text = if (loginInfo.isConnected) {
                    setBackgroundResource(R.color.primary)
                    setStrokeColorResource(R.color.black)
                    setTextColor(binding.root.context.getColor(R.color.accent_text))
                    binding.root.context.getString(R.string.disable_connection)
                } else {
                    setBackgroundResource(R.color.secondary)
                    setStrokeColorResource(R.color.black)
                    setTextColor(binding.root.context.getColor(R.color.accent_text))
                    binding.root.context.getString(R.string.enable_connection)
                }
            }
        }

        /**
         * ViewHolder가 재활용되거나 더 이상 사용되지 않을 때 리소스 정리
         */
        fun cleanup() {
            cancel() // CoroutineScope의 job을 취소하여 리소스 정리
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginInfoViewHolder {
        val binding = ItemLoginManagementListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoginInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoginInfoViewHolder, position: Int) {
        holder.bind(loginInfoList[position])
    }

    override fun onViewRecycled(holder: LoginInfoViewHolder) {
        super.onViewRecycled(holder)
        holder.cleanup() // ViewHolder가 재활용될 때 리소스 정리
    }

    override fun getItemCount(): Int = loginInfoList.size
}