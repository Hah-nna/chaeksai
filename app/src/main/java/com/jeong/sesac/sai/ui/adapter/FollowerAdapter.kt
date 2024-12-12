package com.jeong.sesac.sai.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jeong.sesac.sai.data.Follower
import com.jeong.sesac.sai.databinding.ItemFollowerListBinding

class FollowerAdapter(private val followers: List<Follower>) :
    RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>() {

    inner class FollowerViewHolder(private val binding: ItemFollowerListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(follower: Follower) {
            binding.followBtn.setImageResource(follower.profileImageResId)
            binding.followerNickname.text = follower.name

            // followBtn 클릭 이벤트 추가
            binding.followBtn.setOnClickListener {
                showUnfollowDialog(binding.root.context, follower.name)
            }
        }
        private fun showUnfollowDialog(context: Context, followerName: String) {
            AlertDialog.Builder(context)
                .setTitle("팔로우 취소")
                .setMessage("팔로우를 취소하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    Toast.makeText(context, "$followerName 님의 팔로우를 취소했습니다.", Toast.LENGTH_SHORT).show()
                    // 여기서 데이터 삭제 동작 추가 가능
                }
                .setNegativeButton("아니오", null)
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val binding = ItemFollowerListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        holder.bind(followers[position])
    }

    override fun getItemCount(): Int = followers.size
}