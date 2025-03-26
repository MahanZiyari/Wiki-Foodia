package ir.mahan.wikifoodia.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.mahan.wikifoodia.databinding.ItemStepBinding
import ir.mahan.wikifoodia.models.detail.ResponseDetail.AnalyzedInstruction.Step
import ir.mahan.wikifoodia.utils.BaseDiffUtils
import ir.mahan.wikifoodia.utils.constants.Constants
import ir.mahan.wikifoodia.utils.convertMinToHour
import javax.inject.Inject

class StepsAdapter @Inject constructor() : RecyclerView.Adapter<StepsAdapter.ViewHolder>() {

    private lateinit var binding: ItemStepBinding
    private var items = emptyList<Step>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = Constants.STEPS_COUNT

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Step) {
            binding.apply {
                //Text
                stepTxt.text = "${adapterPosition + 1}"
                item.length?.let {
                    timeTxt.text = item.length.number!!.convertMinToHour()
                }
                infoTxt.text = item.step
            }
        }
    }

    fun setData(data: List<Step>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}