package com.esteel4u.realtimeauctionapp.view.adapter

import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.databinding.ItemSlideModelBinding
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

class HomeBannerAdapter(dimensionPixelOffset: Int) : BaseBannerAdapter<Any>()  {

    private var mRoundCorner = dimensionPixelOffset

    override fun bindData(holder: BaseViewHolder<Any>?, data: Any?, position: Int, pageSize: Int) {
        val viewBinding: ItemSlideModelBinding? = holder?.let { ItemSlideModelBinding.bind(it.itemView) }
        viewBinding?.bannerImage?.setRoundCorner(mRoundCorner)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_slide_model
    }
}

