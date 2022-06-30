package com.esteel4u.realtimeauctionapp.view.adapter;


import com.esteel4u.realtimeauctionapp.R;
import com.esteel4u.realtimeauctionapp.databinding.ItemSlideModelBinding;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

/**
 * @author DBoy
 * @date 2020/12/11
 * Class 描述 : 使用ViewBinding示例
 */
public class ViewBindingSampleAdapter extends BaseBannerAdapter<Integer> {

  private final int mRoundCorner;

  public ViewBindingSampleAdapter(int roundCorner) {
    mRoundCorner = roundCorner;
  }

  @Override
  protected void bindData(BaseViewHolder<Integer> holder, Integer data, int position,
      int pageSize) {
    //示例使用ViewBinding
    ItemSlideModelBinding viewBinding = ItemSlideModelBinding.bind(holder.itemView);
    viewBinding.bannerImage.setRoundCorner(mRoundCorner);
    viewBinding.bannerImage.setImageResource(data);
  }

  @Override
  public int getLayoutId(int viewType) {
    return R.layout.item_slide_model;
  }
}

