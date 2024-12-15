package com.jeong.sesac.sai.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.Follower
import com.jeong.sesac.sai.databinding.ItemFollowerListBinding
import com.jeong.sesac.sai.util.Dialog
import com.jeong.sesac.sai.util.DialogInterface
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class FollowerAdapter(
    private val followers: MutableList<Follower>,
    private val activity: FragmentActivity
) : RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>() {

    inner class FollowerViewHolder(private val binding: ItemFollowerListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(follower: Follower) {
            with(binding) {
                followerNickname.text = follower.name

                // 버튼 텍스트 상태 설정
                followBtn.text = if (follower.isFollowing) {
                    root.context.getString(R.string.unfollow)
                } else {
                    root.context.getString(R.string.follow)
                }

                // Corbind로 followBtn 클릭 이벤트 처리
                activity.lifecycleScope.launch {
                    followBtn.clicks().collect {
                        if (follower.isFollowing) {
                            showUnfollowDialog(follower)
                        } else {
                            follower.isFollowing = true
                            notifyItemChanged(adapterPosition)
                        }
                    }
                }
            }
        }

        @SuppressLint("StringFormatInvalid")
        private fun showUnfollowDialog(follower: Follower) {
            val dialog = Dialog(
                dialogInterface = object : DialogInterface {
                    override fun onClickLeftBtn() {
                        // '아니오' 선택 시 아무 동작 없음
                    }

                    override fun onClickRightBtn() {
                        // '예' 선택 시 팔로우 취소 처리
                        follower.isFollowing = false
                        notifyItemChanged(adapterPosition)
                    }
                },
                title = activity.getString(R.string.dialog_unfollow_title, follower.name),
                leftBtnText = activity.getString(R.string.no),
                rightBtnText = activity.getString(R.string.yes)
            )
            dialog.show(activity.supportFragmentManager, "UnfollowDialog")
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