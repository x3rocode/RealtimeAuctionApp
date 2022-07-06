package com.esteel4u.realtimeauctionapp.view.adapter

import android.animation.ValueAnimator
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.UserData
import com.esteel4u.realtimeauctionapp.databinding.ItemProductListBinding
import com.esteel4u.realtimeauctionapp.view.utils.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.auth.User
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule.Companion.uid
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule.Companion.userName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.log

var animationPlaybackSpeed: Double = 0.8

class ProductListAdapter(context: Context
): RecyclerView.Adapter<ProductListAdapter.MyViewHolder>()  {

    var productList = mutableListOf<ProductData>()
    private val context = context
    private val originalBg: Int by bindColor(context, R.color.white)
    private val expandedBg: Int by bindColor(context, R.color.white)
    private lateinit var dataStore: DataStoreModule
    private var userId: String = ""

    private val listItemHorizontalPadding: Float by bindDimen(context, R.dimen.list_item_vertical_padding)
    private val listItemVerticalPadding: Float by bindDimen(context, R.dimen.list_item_vertical_padding)
    private val originalWidth = context.screenWidth - 48.dp
    private val expandedWidth = context.screenWidth - 24.dp
    private var originalHeight = -1 // will be calculated dynamically
    private var expandedHeight = -1 // will be calculated dynamically

    private val listItemExpandDuration: Long get() = (300L / animationPlaybackSpeed).toLong()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private lateinit var recyclerView: RecyclerView
    private var expandedModel: ProductData? = null
    private var isScaledDown = false

    // Method #5
    class MyViewHolder(val binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentPrd : ProductData) {
            binding.prdlist = currentPrd
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            // get userinfo from data store
        dataStore = DataStoreModule(parent.context)
        CoroutineScope(Dispatchers.Default).launch {
            dataStore.user.collect{
                userId = it.uid!!
                Log.d(ContentValues.TAG, "uid11 " + it.uid)
                Log.d(ContentValues.TAG, "uid11 " + userId)
            }
        }


        val binding = ItemProductListBinding.inflate(LayoutInflater.from(parent.context),  parent, false)
            return MyViewHolder(binding)
    }


    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(productList[position])


        when (holder.binding.prdlist?.auctionProgressStatus){
            1 -> holder.binding.prdStatus.text = "진행 예정"
            2 -> holder.binding.prdStatus.text = "진행중"
            3 -> holder.binding.prdStatus.text = "종료"
        }

        if(holder.binding.prdlist?.notifyOnUserId!!.contains(userId)){
            holder.binding.sparkButton.isChecked = true
        }

        expandItem(holder, product == expandedModel, animate = false)
        scaleDownItem(holder, position, isScaledDown)

        holder.binding.cardContainer.setOnClickListener {
            if (expandedModel == null) {

                // expand clicked view
                expandItem(holder, expand = true, animate = true)
                expandedModel = product
            } else if (expandedModel == product) {

                // collapse clicked view
                expandItem(holder, expand = false, animate = true)
                expandedModel = null
            } else {

                // collapse previously expanded view
                val expandedModelPosition = productList.indexOf(expandedModel!!)

                val oldViewHolder =
                    recyclerView.findViewHolderForAdapterPosition(expandedModelPosition) as? MyViewHolder
                if (oldViewHolder != null) expandItem(oldViewHolder, expand = false, animate = true)

                // expand clicked view
                expandItem(holder, expand = true, animate = true)
                expandedModel = product
            }
        }
        holder.binding.executePendingBindings()
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        return productList.size
    }

    // Method #7
    interface Interaction {
        fun onItemSelected(position: Int, item: ProductData)
    }
    // Method #4
    fun setData(prdData: List<ProductData>) {
        this.productList.clear()
        this.productList.addAll(prdData)

        notifyDataSetChanged()
    }




    //expand
    private fun expandItem(holder: MyViewHolder, expand: Boolean, animate: Boolean) {

        if (animate) {
            val animator = getValueAnimator(
                expand, listItemExpandDuration, AccelerateDecelerateInterpolator()
            ) { progress -> setExpandProgress(holder, progress) }
            holder.binding.expandView.isVisible = true
            if (expand) animator.doOnStart { holder.binding.expandView.isVisible = true }
            else animator.doOnEnd { holder.binding.expandView.isVisible = false }

            animator.start()
        } else {

            // show expandView only if we have expandedHeight (onViewAttached)
            holder.binding.expandView.isVisible = expand && expandedHeight >= 0
            setExpandProgress(holder, if (expand) 1f else 0f)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)

        // get originalHeight & expandedHeight if not gotten before
        if (expandedHeight < 0) {
            expandedHeight = 0 // so that this block is only called once

            holder.binding.cardContainer.doOnLayout { view ->
                originalHeight = view.height

                view.doOnPreDraw {
                    expandedHeight = view.height
                    holder.binding.expandView.isVisible = false

                }
            }
        }
    }

    private fun setExpandProgress(holder: MyViewHolder, progress: Float) {
        if (expandedHeight > 0 && originalHeight > 0) {
            holder.binding.cardContainer.layoutParams.height =
                (originalHeight + (expandedHeight - originalHeight) * progress).toInt()
        }
        holder.binding.cardContainer.layoutParams.width =
            (originalWidth + (expandedWidth - originalWidth) * progress).toInt()

//        if(holder.binding.joborder?.joborderEmg == 0)
//            holder.binding.cardContainer.setBackgroundColor(blendColors(originalBg, expandedBg, progress))
//        else if(holder.binding.joborder?.joborderEmg == 1)
//            holder.binding.cardContainer.setBackgroundColor(blendColors(emergencyBg, expandedBg, progress))

        holder.binding.cardContainer.requestLayout()
        //holder.binding.chevron.rotation = 90 * progress
    }

    private inline val LinearLayoutManager.visibleItemsRange: IntRange
        get() = findFirstVisibleItemPosition()..findLastVisibleItemPosition()

    fun getScaleDownAnimator(isScaledDown: Boolean): ValueAnimator {
        val lm = recyclerView.layoutManager as LinearLayoutManager

        val animator = getValueAnimator(isScaledDown,
            duration = 300L, interpolator = AccelerateDecelerateInterpolator()
        ) { progress ->

            // Get viewHolder for all visible items and animate attributes
            for (i in lm.visibleItemsRange) {
                val holder = recyclerView.findViewHolderForLayoutPosition(i) as MyViewHolder
                setScaleDownProgress(holder, i, progress)
            }
        }

        // Set adapter variable when animation starts so that newly binded views in
        // onBindViewHolder will respect the new size when they come into the screen
        animator.doOnStart { this.isScaledDown = isScaledDown }

        // For all the non visible items in the layout manager, notify them to adjust the
        // view to the new size
        animator.doOnEnd {
            repeat(lm.itemCount) { if (it !in lm.visibleItemsRange) notifyItemChanged(it) }
        }
        return animator
    }


    private fun setScaleDownProgress(holder: MyViewHolder, position: Int, progress: Float) {
        val itemExpanded = position >= 0 && productList[position] == expandedModel
        holder.binding.cardContainer.layoutParams.apply {
            width = ((if (itemExpanded) expandedWidth else originalWidth) * (1 - 0.1f * progress)).toInt()
            height = ((if (itemExpanded) expandedHeight else originalHeight) * (1 - 0.1f * progress)).toInt()
            //log("width=$width, height=$height [${"%.2f".format(progress)}]")
        }
        holder.binding.cardContainer.requestLayout()

        holder.binding.scaleContainer.scaleX = 1 - 0.05f * progress
        holder.binding.scaleContainer.scaleY = 1 - 0.05f * progress

        holder.binding.scaleContainer.setPadding(
            (listItemHorizontalPadding * (1 - 0.2f * progress)).toInt(),
            (listItemVerticalPadding * (1 - 0.2f * progress)).toInt(),
            (listItemHorizontalPadding * (1 - 0.2f * progress)).toInt(),
            (listItemVerticalPadding * (1 - 0.2f * progress)).toInt()
        )

    }

    private fun scaleDownItem(holder: MyViewHolder, position: Int, isScaleDown: Boolean) {
        setScaleDownProgress(holder, position, if (isScaleDown) 1f else 0f)
    }


}

