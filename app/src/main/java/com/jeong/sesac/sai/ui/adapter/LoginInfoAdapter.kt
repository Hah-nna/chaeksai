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
            binding.loginCompany.text = loginInfo.companyName
            binding.signInDate.text = loginInfo.signInDate
            binding.connectionBtn.setImageResource(
                if (loginInfo.isConnected) R.drawable.enable_connection else R.drawable.disable_connection
            )

            // 연결 버튼 클릭 이벤트
            binding.connectionBtn.setOnClickListener {
                if (loginInfo.isConnected) {
                    // 연결 해제 로직
                    binding.connectionBtn.setImageResource(R.drawable.disable_connection)
                } else {
                    // 연결 활성화 로직
                    binding.connectionBtn.setImageResource(R.drawable.enable_connection)
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