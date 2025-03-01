package ir.mahan.wikifoodia.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.adapter.PopularItemsAdapter.ViewHolder
import ir.mahan.wikifoodia.databinding.ItemRecipeBinding
import ir.mahan.wikifoodia.models.recipe.ResponseRecipes.Result
import ir.mahan.wikifoodia.utils.BaseDiffUtils
import ir.mahan.wikifoodia.utils.constants.Constants
import ir.mahan.wikifoodia.utils.convertMinToHour
import ir.mahan.wikifoodia.utils.setDynamicallyColor
import javax.inject.Inject

class RecipeItemsAdapter @Inject constructor() : RecyclerView.Adapter<RecipeItemsAdapter.ViewHolder>() {

    private lateinit var binding: ItemRecipeBinding
    private var currentItems: List<Result> = emptyList()
    private lateinit var context: Context


    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Result) {
            // Text
            binding.apply {
                recipeNameTxt.text = item.title
                val htmlCompat = HtmlCompat.fromHtml(item.summary!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
                recipeDescTxt.text = htmlCompat
                recipeTimeTxt.text = item.readyInMinutes!!.convertMinToHour()
                recipeHealthTxt.text = item.healthScore.toString()
                recipeLikeTxt.text = item.aggregateLikes.toString()
                setFoodsInfoColors(item)
                // Load Image
                val newUrl = changeUrlWithNewSize(item.image!!)
                recipeImg.load(newUrl) {
                    crossfade(true)
                    crossfade(800)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.ic_placeholder)
                }
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item.id!!) }
                }
            }// Binding Scope
        }
        fun initAnimation() {
            binding.root.animation = android.view.animation.AnimationUtils.loadAnimation(context,  R.anim.item_anim)
        }

        fun clearAnimation() {
            binding.root.clearAnimation()
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = ItemRecipeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder()
    }

    override fun getItemCount() = currentItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentItems[position])
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.initAnimation()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.clearAnimation()
    }
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

    private fun setFoodsInfoColors(item: Result) {
        binding.apply {
            //Vegan
            if (item.vegan!!) {
                recipeVeganTxt.setDynamicallyColor(R.color.caribbean_green)
            } else {
                recipeVeganTxt.setDynamicallyColor(R.color.gray)
            }
            //Healthy
            when (item.healthScore?.toInt()) {
                in 90..100 -> recipeHealthTxt.setDynamicallyColor(R.color.caribbean_green)
                in 60..89 -> recipeHealthTxt.setDynamicallyColor(R.color.chineseYellow)
                in 0..59 -> recipeHealthTxt.setDynamicallyColor(R.color.tart_orange)
            }
        }
    }
}