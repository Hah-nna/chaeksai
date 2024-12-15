package com.jeong.sesac.sai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.LoginInfo
import com.jeong.sesac.sai.databinding.ItemLoginManagementListBinding

class LoginInfoAdapter(private val loginInfoList: List<LoginInfo>) :
    RecyclerView.Adapter<LoginInfoAdapter.LoginInfoViewHolder>() {

    inner class LoginInfoViewHolder(private val binding: ItemLoginManagementListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loginInfo: LoginInfo) {
            // 회사 이름 및 로그인 날짜 설정
            binding.loginCompany.text = loginInfo.companyName
            binding.signInDate.text = loginInfo.signInDate

            // 연결 버튼 상태 설정
            binding.connectionBtn.text = if (loginInfo.isConnected) {
                binding.connectionBtn.setBackgroundResource(R.color.primary)
                binding.connectionBtn.setStrokeColorResource(R.color.black)
                binding.connectionBtn.setTextColor(binding.root.context.getColor(R.color.accent_text))
                binding.root.context.getString(R.string.disable_connection)
            } else {
                binding.connectionBtn.setBackgroundResource(R.color.secondary)
                binding.connectionBtn.setStrokeColorResource(R.color.black)
                binding.connectionBtn.setTextColor(binding.root.context.getColor(R.color.accent_text))
                binding.root.context.getString(R.string.enable_connection)
            }

            // 연결 버튼 클릭 이벤트
            binding.connectionBtn.setOnClickListener {
                if (loginInfo.isConnected) {
                    // 연결 해제 로직
                    loginInfo.isConnected = false
                    binding.connectionBtn.text =
                        binding.root.context.getString(R.string.enable_connection)
                    binding.connectionBtn.setBackgroundResource(R.color.secondary)
                } else {
                    // 연결 활성화 로직
                    loginInfo.isConnected = true
                    binding.connectionBtn.text =
                        binding.root.context.getString(R.string.disable_connection)
                    binding.connectionBtn.setBackgroundResource(R.color.primary)
                }
            }
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

    override fun getItemCount(): Int = loginInfoList.size
}
