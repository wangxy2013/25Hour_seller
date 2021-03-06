package com.jyq.seller.holder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyq.seller.R;
import com.jyq.seller.bean.OrderInfo;
import com.jyq.seller.listener.MyOnClickListener;
import com.jyq.seller.utils.StringUtils;


/**
 */
public class OrderHolder1 extends RecyclerView.ViewHolder
{
    private TextView mIndexTv;
    private TextView mShopNameTv;
    private TextView mTimeTv;
    private TextView mNumberTv;
    private TextView mPhoneTv;
    private TextView mNameTv;
    private TextView mAddressTv;
    private TextView mGetTv;
    private TextView mPayStyleTv;
    private TextView mRemainingTimeTv;
    private LinearLayout mItemLayout;
    private MyOnClickListener.OnClickCallBackListener listener;
    private Context context;


    public OrderHolder1(View rootView, Context context, MyOnClickListener.OnClickCallBackListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mIndexTv = (TextView) rootView.findViewById(R.id.tv_index);
        mNumberTv = (TextView) rootView.findViewById(R.id.tv_code);
        mShopNameTv = (TextView) rootView.findViewById(R.id.tv_shop_name);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
        mPhoneTv = (TextView) rootView.findViewById(R.id.tv_customer_phone);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_customer_name);
        mAddressTv = (TextView) rootView.findViewById(R.id.tv_customer_address);
        mGetTv = (TextView) rootView.findViewById(R.id.tv_submit);
        mPayStyleTv = (TextView) rootView.findViewById(R.id.tv_pay_style);
        mRemainingTimeTv = (TextView) rootView.findViewById(R.id.tv_remaining_time);
        mItemLayout= (LinearLayout) rootView.findViewById(R.id.ll_item);
    }


    public void setOrderInfo(OrderInfo mOrderInfo, final int p)
    {

        mIndexTv.setText(StringUtils.getIndex(p));
        mNumberTv.setText(mOrderInfo.getId());
        mShopNameTv.setText(mOrderInfo.getStoreName());
        mTimeTv.setText(mOrderInfo.getAddTime());
        mPhoneTv.setText( mOrderInfo.getPhone());
        mNameTv.setText(mOrderInfo.getName());
        mAddressTv.setText("客户地址:" + mOrderInfo.getAddress());
        mPayStyleTv.setText("offline".equals(mOrderInfo.getPayType()) ? "货到付款" : "微信支付");
        if ("offline".equals(mOrderInfo.getPayType()))
        {
            mPayStyleTv.setTextColor(ContextCompat.getColor(context,R.color.redA));
        }
        else
        {
            mPayStyleTv.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        String currentTime = StringUtils.getTimestamp();
        String addTime = mOrderInfo.getAddTime();
       // addTime = "2018-12-16 21:10:50";
        if (!StringUtils.stringIsEmpty(addTime))
        {
            int time = StringUtils.differentDaysByMillisecond(currentTime, StringUtils.getOrderEndTime(addTime));

            if (time <= 0)
            {
                mRemainingTimeTv.setText("订单已失效");
            }
            else
            {
                mRemainingTimeTv.setText(StringUtils.formatTime(time));
            }


            //mRemainingTimeTv.setText();

        }


        mGetTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onSubmit(p, 0);
            }
        });
        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onSubmit(p, 1);
            }
        });
    }


}
