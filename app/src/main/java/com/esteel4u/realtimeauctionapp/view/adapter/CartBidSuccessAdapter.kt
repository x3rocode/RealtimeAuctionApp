package com.esteel4u.realtimeauctionapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.databinding.ItemCartBidSuccessListBinding
import com.esteel4u.realtimeauctionapp.view.utils.*
import com.esteel4u.realtimeauctionapp.model.datastore.DataStoreModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class CartBidSuccessAdapter(
    products: List<ProductData>,
    context: Context
): RecyclerView.Adapter<CartBidSuccessAdapter.MyViewHolder>()  {

    var productList = mutableListOf<ProductData>()

    init {
        productList.addAll(products)
    }

    private val originalBg: Int by bindColor(context, R.color.white)
    private val expandedBg: Int by bindColor(context, R.color.white)
    private val context = context

    private val listItemHorizontalPadding: Float by bindDimen(context, R.dimen.list_item_vertical_padding)
    private val listItemVerticalPadding: Float by bindDimen(context, R.dimen.list_item_vertical_padding)
    private val originalWidth = context.screenWidth - 48.dp
    private val expandedWidth = context.screenWidth - 24.dp
    private var originalHeight = -1
    private var expandedHeight = -1
    private val listItemExpandDuration: Long get() = (300L / animationPlaybackSpeed).toLong()
    private var expandedModel: ProductData? = null
    private var isScaledDown = false

    private lateinit var dataStore: DataStoreModule
    private var userId: String = ""
    private lateinit var recyclerView: RecyclerView


    // Method #5
    class MyViewHolder(
        val binding: ItemCartBidSuccessListBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(currentPrd : ProductData) {
            binding.prdlist = currentPrd
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartBidSuccessAdapter.MyViewHolder {
        // get userinfo from data store
        dataStore = DataStoreModule(parent.context)
        CoroutineScope(Dispatchers.Default).launch {
            dataStore.user.collect{
                userId = it.uid!!

            }
        }
        val binding = ItemCartBidSuccessListBinding.inflate(LayoutInflater.from(parent.context),  parent, false)
            return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val product = productList[position]
        holder.bind(productList[position])


        when (holder.binding.prdlist?.prdTotClsSeqNm){
            1 -> holder.binding.prdTotseqNm?.text = "주문외 1급"
            2 -> holder.binding.prdTotseqNm?.text = "주문외 2급"
        }

        when(holder.binding.prdlist?.auctionType){
            1 -> {
                holder.binding.prdAuctionType.text = "프리미엄"
//                holder.binding.backcard.setCardBackgroundColor(getColor(context, R.color.lblue10))
//                holder.binding.amountcard.setCardBackgroundColor(getColor(context, R.color.lblue20))
            }
            2 -> {
                holder.binding.prdAuctionType.text = "옥션"
//                holder.binding.backcard.setCardBackgroundColor(getColor(context, R.color.ered10))
//                holder.binding.amountcard.setCardBackgroundColor(getColor(context, R.color.ered20))
            }
            3 -> {
                holder.binding.prdAuctionType.text = "아울렛"
//                holder.binding.backcard.setCardBackgroundColor(getColor(context, R.color.epurple10))
//                holder.binding.amountcard.setCardBackgroundColor(getColor(context, R.color.epurple20))
            }
            4 -> {
                holder.binding.prdAuctionType.text = "패키지"
//                holder.binding.backcard.setCardBackgroundColor(getColor(context, R.color.lblue10))
//                holder.binding.amountcard.setCardBackgroundColor(getColor(context, R.color.lblue20))
            }
        }

        val myFormatter = DecimalFormat("###,###")

        var price = holder.binding.prdlist?.bidPrice!! * holder.binding.prdlist?.prdWgt!!
        val formattedWgt: String = myFormatter.format(holder.binding.prdlist!!.prdWgt) + "Kg"
        val formattedhighPrice: String = myFormatter.format(holder.binding.prdlist!!.bidPrice)
        val formattedPrice: String = myFormatter.format(price)
        holder.binding.prdBid.text = "₩$formattedhighPrice"
        holder.binding.prdPrdwgt.text = formattedWgt

        holder.binding.aucBid.text = "₩$formattedPrice"

//        expandItem(holder, product == expandedModel, animate = false)
//        scaleDownItem(holder, position, isScaledDown)

//        holder.binding.cardContainer.setOnClickListener {
//            when (expandedModel) {
//                null -> {
//                    // expand clicked view
//                    expandItem(holder, expand = true, animate = true)
//                    expandedModel = product
//                }
//                product -> {
//                    // collapse clicked view
//                    expandItem(holder, expand = false, animate = true)
//                    expandedModel = null
//                }
//                else -> {
//                    // collapse previously expanded view
//                    val expandedModelPosition = productList.indexOf(expandedModel!!)
//                    val oldViewHolder =
//                        recyclerView.findViewHolderForAdapterPosition(expandedModelPosition) as? CartListAdapter.MyViewHolder
//                    if (oldViewHolder != null) expandItem(oldViewHolder, expand = false, animate = true)
//
//                    // expand clicked view
//                    expandItem(holder, expand = true, animate = true)
//                    expandedModel = product
//                }
//            }
//        }
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    fun setData(prdData: List<ProductData>) {
        val diffCallback = DiffCallback(this.productList, prdData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.productList.clear()
        this.productList.addAll(prdData)
        diffResult.dispatchUpdatesTo(this)
    }

    //expand
//    private fun expandItem(holder: CartListAdapter.MyViewHolder, expand: Boolean, animate: Boolean) {
//
//        if (animate) {
//            val animator = getValueAnimator(
//                expand, listItemExpandDuration, AccelerateDecelerateInterpolator()
//            ) { progress -> setExpandProgress(holder, progress) }
//
//            if (expand) animator.doOnStart { holder.binding.expandView.isVisible = true }
//            else animator.doOnEnd { holder.binding.expandView.isVisible = false }
//
//            animator.start()
//        } else {
//
//            // show expandView only if we have expandedHeight (onViewAttached)
//            holder.binding.expandView.isVisible = expand && expandedHeight >= 0
//            setExpandProgress(holder, if (expand) 1f else 0f)
//        }
//    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
//    override fun onViewAttachedToWindow(holder: CartListAdapter.MyViewHolder) {
//        super.onViewAttachedToWindow(holder)
//
//        // get originalHeight & expandedHeight if not gotten before
//        if (expandedHeight < 0) {
//            expandedHeight = 0 // so that this block is only called once
//
//            holder.binding.cardContainer.doOnLayout { view ->
//                originalHeight = view.height
//
//                holder.binding.expandView.isVisible = true
//                view.doOnPreDraw {
//                    expandedHeight = view.height
//                    holder.binding.expandView.isVisible = false
//
//                }
//            }
//        }
//    }

//    private fun setExpandProgress(holder: CartListAdapter.MyViewHolder, progress: Float) {
//        if (expandedHeight > 0 && originalHeight > 0) {
//            holder.binding.cardContainer.layoutParams.height =
//                (originalHeight + (expandedHeight - originalHeight) * progress).toInt()
//        }
//        holder.binding.cardContainer.layoutParams.width =
//            (originalWidth + (expandedWidth - originalWidth) * progress).toInt()
//        holder.binding.cardContainer.requestLayout()
//    }

    private inline val LinearLayoutManager.visibleItemsRange: IntRange
        get() = findFirstVisibleItemPosition()..findLastVisibleItemPosition()

//    fun getScaleDownAnimator(isScaledDown: Boolean): ValueAnimator {
//        val lm = recyclerView.layoutManager as LinearLayoutManager
//
//        val animator = getValueAnimator(isScaledDown,
//            duration = 300L, interpolator = AccelerateDecelerateInterpolator()
//        ) { progress ->
//
//            // Get viewHolder for all visible items and animate attributes
//            for (i in lm.visibleItemsRange) {
//                val holder = recyclerView.findViewHolderForLayoutPosition(i) as CartListAdapter.MyViewHolder
//                setScaleDownProgress(holder, i, progress)
//            }
//        }
//        animator.doOnStart { this.isScaledDown = isScaledDown }
//        animator.doOnEnd {
//            repeat(lm.itemCount) { if (it !in lm.visibleItemsRange) notifyItemChanged(it) }
//        }
//        return animator
//    }


//    private fun setScaleDownProgress(holder: CartListAdapter.MyViewHolder, position: Int, progress: Float) {
//        val itemExpanded = position >= 0 && productList[position] == expandedModel
//        holder.binding.cardContainer.layoutParams.apply {
//            width = ((if (itemExpanded) expandedWidth else originalWidth) * (1 - 0.1f * progress)).toInt()
//            height = ((if (itemExpanded) expandedHeight else originalHeight) * (1 - 0.1f * progress)).toInt()
//        }
//        holder.binding.cardContainer.requestLayout()
//
//        holder.binding.scaleContainer.scaleX = 1 - 0.05f * progress
//        holder.binding.scaleContainer.scaleY = 1 - 0.05f * progress
//
//        holder.binding.scaleContainer.setPadding(
//            (listItemHorizontalPadding * (1 - 0.2f * progress)).toInt(),
//            (listItemVerticalPadding * (1 - 0.2f * progress)).toInt(),
//            (listItemHorizontalPadding * (1 - 0.2f * progress)).toInt(),
//            (listItemVerticalPadding * (1 - 0.2f * progress)).toInt()
//        )
//        holder.binding.listItemFg.alpha = progress
//    }

//    private fun scaleDownItem(holder: CartListAdapter.MyViewHolder, position: Int, isScaleDown: Boolean) {
//        setScaleDownProgress(holder, position, if (isScaleDown) 1f else 0f)
//    }


}

