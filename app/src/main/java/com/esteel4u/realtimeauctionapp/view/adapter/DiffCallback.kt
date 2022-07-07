package com.esteel4u.realtimeauctionapp.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.esteel4u.realtimeauctionapp.data.model.ProductData


class DiffCallback(
    private val oldList: List<ProductData>,
    private val newList: List<ProductData>
) : DiffUtil.Callback() {

    // Method #1
    override fun getOldListSize() = oldList.size

    // Method #2
    override fun getNewListSize() = newList.size

    // Method #3
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    // Method #4
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].auctionType == newList[newItemPosition].auctionType
                && oldList[oldItemPosition].prdId == newList[newItemPosition].prdId
                && oldList[oldItemPosition].prdNo == newList[newItemPosition].prdNo
                && oldList[oldItemPosition].prdName == newList[newItemPosition].prdName
                && oldList[oldItemPosition].substSpec == newList[newItemPosition].substSpec
                && oldList[oldItemPosition].prdThk == newList[newItemPosition].prdThk
                && oldList[oldItemPosition].prdWth == newList[newItemPosition].prdWth
                && oldList[oldItemPosition].prdWgt == newList[newItemPosition].prdWgt
                && oldList[oldItemPosition].prdTotClsSeqNm == newList[newItemPosition].prdTotClsSeqNm
                && oldList[oldItemPosition].worksCode == newList[newItemPosition].worksCode
                && oldList[oldItemPosition].startDate == newList[newItemPosition].startDate
                && oldList[oldItemPosition].endDate == newList[newItemPosition].endDate
                && oldList[oldItemPosition].auctionProgressStatus == newList[newItemPosition].auctionProgressStatus
                && oldList[oldItemPosition].notifyOnUserId == newList[newItemPosition].notifyOnUserId
    }
}