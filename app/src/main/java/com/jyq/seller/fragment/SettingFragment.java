package com.jyq.seller.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyq.seller.R;
import com.jyq.seller.activity.BaseHandler;
import com.jyq.seller.activity.MainActivity;
import com.jyq.seller.activity.ModifyPwdActivity;
import com.jyq.seller.activity.UserDetailActivity;
import com.jyq.seller.http.DataRequest;
import com.jyq.seller.http.HttpRequest;
import com.jyq.seller.http.IRequestListener;
import com.jyq.seller.json.ResultHandler;
import com.jyq.seller.utils.APPUtils;
import com.jyq.seller.utils.ConfigManager;
import com.jyq.seller.utils.ConstantUtil;
import com.jyq.seller.utils.NetWorkUtil;
import com.jyq.seller.utils.ToastUtil;
import com.jyq.seller.utils.Urls;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingFragment extends BaseFragment implements View.OnClickListener, IRequestListener
{

    @BindView(R.id.rl_user)
    RelativeLayout rlUser;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @BindView(R.id.rl_cache)
    RelativeLayout rlCache;
    @BindView(R.id.iv_switch)
    ImageView ivSwitch;
    @BindView(R.id.iv_automatic_switch)
    ImageView ivAutomaticSwitch;

    @BindView(R.id.rl_opened)
    RelativeLayout mOpenedLayout;

    @BindView(R.id.tv_version)
    TextView mVersionTv;
    private View rootView = null;
    private Unbinder unbinder;
    private boolean isOpened = false;

    private boolean isAutomatic = false;

    private static final String STORE_CHANGE_STATUS = "store_change_status";
    private static final String STORE_OPERATE = "store_operate";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int STORE_CHANGE_SUCCESS = 0x02;
    private static final int REQUEST_FAIL = 0x03;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    if (isOpened)
                    {
                        isOpened = false;
                        ivSwitch.setImageResource(R.drawable.ic_switch_off);
                        ConfigManager.instance().setIsClose("0");
                    }
                    else
                    {
                        isOpened = true;
                        ivSwitch.setImageResource(R.drawable.ic_switch_on);
                        ConfigManager.instance().setIsClose("1");
                    }
                    ToastUtil.show(getActivity(), "操作成功");

                    break;

                case STORE_CHANGE_SUCCESS:

                    if (isAutomatic)
                    {
                        isAutomatic = false;
                        ConfigManager.instance().setIsCloseOffline(isAutomatic);
                        ivAutomaticSwitch.setImageResource(R.drawable.ic_switch_off);
                        //ConfigManager.instance().setIsClose("0");
                        mOpenedLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        isAutomatic = true;
                        ConfigManager.instance().setIsCloseOffline(isAutomatic);
                        ivAutomaticSwitch.setImageResource(R.drawable.ic_switch_on);
                        //ConfigManager.instance().setIsClose("1");
                        mOpenedLayout.setVisibility(View.GONE);
                    }
                    ToastUtil.show(getActivity(), "操作成功");

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());
                    break;


            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_setting, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        rlUser.setOnClickListener(this);
        rlPwd.setOnClickListener(this);
        ivSwitch.setOnClickListener(this);
        ivAutomaticSwitch.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        mVersionTv.setText("当前版本号:"+APPUtils.getVersionName(getActivity()));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if ("0".equals(ConfigManager.instance().getIsClose()))
        {
            isOpened = false;
            ivSwitch.setImageResource(R.drawable.ic_switch_off);
        }
        else
        {
            isOpened = true;
            ivSwitch.setImageResource(R.drawable.ic_switch_on);
        }

        if (!ConfigManager.instance().getIsCloseOffline())
        {
            isAutomatic = false;
            ivAutomaticSwitch.setImageResource(R.drawable.ic_switch_off);
            //ConfigManager.instance().setIsClose("0");
            mOpenedLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            isAutomatic = true;
            ivAutomaticSwitch.setImageResource(R.drawable.ic_switch_on);
            //ConfigManager.instance().setIsClose("1");
            mOpenedLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v)
    {
        if (v == rlUser)
        {
            startActivity(new Intent(getActivity(), UserDetailActivity.class));
        }
        else if (v == rlPwd)
        {
            startActivityForResult(new Intent(getActivity(), ModifyPwdActivity.class), 9001);
        }
        else if (v == ivSwitch)
        {
            if (isOpened)
            {
                //                isOpened = false;
                //                ivSwitch.setImageResource(R.drawable.ic_switch_off);
                operate("2");
            }
            else
            {
                //                isOpened = true;
                //                ivSwitch.setImageResource(R.drawable.ic_switch_on);
                operate("1");
            }
        }
        else if (v == ivAutomaticSwitch)
        {
            //自动
            if(isAutomatic)
            {
                changeStore(false);
            }
            else
            {
                changeStore(true);
            }
        }
    }


    private void changeStore(boolean status)
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            NetWorkUtil.showNoNetWorkDlg(getActivity());
            return;
        }
        showProgressDialog(getActivity());
        Map<String, Object> valuePairs = new HashMap<>();
        valuePairs.put("storeId", ConfigManager.instance().getUserID());
        valuePairs.put("isCloseOffline", status);
        valuePairs.put("isOffline", !status);

        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getChangeStoreUrl(), this, HttpRequest.POST, STORE_CHANGE_STATUS, postMap, new ResultHandler());

    }


    private void operate(String operateType)
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            NetWorkUtil.showNoNetWorkDlg(getActivity());
            return;
        }
        showProgressDialog(getActivity());
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("storeId", ConfigManager.instance().getUserID());
        valuePairs.put("operateType", operateType);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getStoreOperateUrl(), this, HttpRequest.POST, STORE_OPERATE, postMap, new ResultHandler());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 9001)
            {
                ((MainActivity) getActivity()).finish();
            }
        }

    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog(getActivity());
        if (STORE_OPERATE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if(STORE_CHANGE_STATUS.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(STORE_CHANGE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}
