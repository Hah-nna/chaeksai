import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeong.sesac.sai.data.Review
import com.jeong.sesac.sai.databinding.ItemReviewListBinding

class ReviewAdapter(
    private val reviews: List<Review>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: ItemReviewListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            with(binding) {

                // 데이터 바인딩
                profileImage.setImageResource(review.profileImageResId)
                nicknameText.text = review.nickname
                content.text = review.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size
}