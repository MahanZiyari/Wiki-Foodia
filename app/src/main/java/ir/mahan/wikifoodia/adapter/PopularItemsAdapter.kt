package ir.mahan.wikifoodia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.google.android.material.animation.AnimationUtils
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.ItemPopularBinding
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes.Result
import ir.mahan.wikifoodia.utils.BaseDiffUtils
import ir.mahan.wikifoodia.utils.constants.Constants
import timber.log.Timber
import javax.inject.Inject

class PopularItemsAdapter @Inject constructor(): RecyclerView.Adapter<PopularItemsAdapter.ViewHolder>() {

    private lateinit var binding: ItemPopularBinding
    private var currentItems: List<Result> = emptyList()
    private lateinit var context: Context

    inner class ViewHolder(): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Result) {
            // Text
            binding.apply {
                popularName.text = item.title
                priceText.text = "${item.pricePerServing} $"
                // Load Image
                val newUrl = changeUrlWithNewSize(item.image!!)
                popularImg.load(newUrl)  {
                    crossfade(true)
                    crossfade(800)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.ic_placeholder)
                }
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item.id!!) }
                }
            }
        }


    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = ItemPopularBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder()
    }

    override fun getItemCount() = currentItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentItems[position])
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    fun setData(newItems: List<Result>) {
        val adapterDiffUtils = BaseDiffUtils(currentItems, newItems)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        currentItems = newItems
        diffUtils.dispatchUpdatesTo(this)

    }



    // utility Functions
    private fun changeUrlWithNewSize(imageUrl: String): String {
        val splittedUrl = imageUrl.split("-")
        val newSize = splittedUrl[1].replace(Constants.OLD_IMAGE_SIZE, Constants.NEW_IMAGE_SIZE)
        return "${splittedUrl[0]}-$newSize"
    }
}