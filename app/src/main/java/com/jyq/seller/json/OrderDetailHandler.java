package com.jyq.seller.json;


import com.jyq.seller.bean.DeliverSupply;
import com.jyq.seller.bean.GoodsInfo;
import com.jyq.seller.bean.OrderDetailInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：一句话简单描述
 */
public class OrderDetailHandler extends JsonHandler
{
    private OrderDetailInfo orderDetailInfo;

    public OrderDetailInfo getOrderDetailInfo()
    {
        return orderDetailInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObject) throws Exception
    {
        try
        {

            if (null != jsonObject)
            {
                orderDetailInfo = new OrderDetailInfo(jsonObject);

                if (null != orderDetailInfo)
                {
                    JSONArray array = jsonObject.optJSONArray("orderGoodsInfoResultVOList");

                    List<GoodsInfo> goodsInfoList = new ArrayList<>();
                    if (null != array)
                    {
                        for (int i = 0; i < array.length(); i++)
                        {
                            goodsInfoList.add(new GoodsInfo(array.optJSONObject(i)));
                        }
                    }

                    orderDetailInfo.setGoodsInfoList(goodsInfoList);


                    JSONObject deliverSupplyObj = jsonObject.optJSONObject("deliverSupply");

                    if (null !=deliverSupplyObj)
                    {
                        DeliverSupply mDeliverSupply = new DeliverSupply(deliverSupplyObj);

                        orderDetailInfo.setDeliverSupply(mDeliverSupply);
                    }
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}