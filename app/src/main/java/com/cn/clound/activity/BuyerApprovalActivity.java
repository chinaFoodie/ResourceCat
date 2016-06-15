package com.cn.clound.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.ApprovalMenberRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Check;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.approval.BuyerApprovalDetailModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.interfaces.DateCallBack;
import com.cn.clound.view.CustomProgress;
import com.cn.clound.view.dialog.CalendarDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 采购审批界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-8 15:29:56
 */
public class BuyerApprovalActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_left)
    LinearLayout llLeft;
    @Bind(R.id.tv_base_left)
    TextView tvBaseLeft;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.include_buyer_details_banner)
    View buyerBanner;
    @Bind(R.id.include_buyer_approval_first)
    View buyerFirst;
    @Bind(R.id.include_buyer_approval_next)
    View buyerNext;
    @Bind(R.id.ll_buyer_approval_card)
    LinearLayout llByCard;
    @Bind(R.id.ll_buyer_approval_cash)
    LinearLayout llByCash;
    @Bind(R.id.img_buyer_approval_cash)
    ImageView imgCash;
    @Bind(R.id.img_buyer_approval_card)
    ImageView imgCard;
    @Bind(R.id.rl_buyer_list_details)
    RelativeLayout rlBuyerDetails;
    @Bind(R.id.ll_buyer_list_details)
    LinearLayout llBuyerDetails;
    @Bind(R.id.rl_add_to_approval_details)
    RelativeLayout rlAddToDetails;
    @Bind(R.id.recycler_buyer_approval_details)
    RecyclerView recyclerViewDetails;
    @Bind(R.id.et_approval_goods_name)
    EditText etGoodsName;
    @Bind(R.id.et_approval_goods_number)
    EditText etGoodsNumber;
    @Bind(R.id.et_approval_goods_price)
    EditText etGoodsPrice;
    @Bind(R.id.et_approval_goods_spec)
    EditText etGoodsSpec;
    @Bind(R.id.et_approval_goods_unit)
    EditText etGoodsUnit;
    @Bind(R.id.tv_total_claims)
    TextView tvTotalClaims;
    @Bind(R.id.et_buyer_remark)
    EditText etBuyerRemark;
    @Bind(R.id.et_buyer_reason)
    EditText etBuyerReason;
    @Bind(R.id.et_buyer_type)
    EditText etBuyerType;
    @Bind(R.id.tv_buyer_time_value)
    TextView tvBuyerTime;
    @Bind(R.id.rl_choose_approval_person)
    RelativeLayout rlApprovalPerson;
    @Bind(R.id.recycler_approval_person)
    RecyclerView recyclerViewApproval;
    @Bind(R.id.rl_choose_approval_date)
    RelativeLayout rlChooseDate;

    private BuyerRecyclerAdapter buyerAdapter;
    private BuyerApprovalDetailModel badm;
    private List<BuyerApprovalDetailModel.BuyerApprovalDetail.BuyerApproval> listBa = new ArrayList<>();
    private String payType = "1";
    private List<BottomUserModel> listMenber = new ArrayList<>();
    private ApprovalMenberRecyclerAdapter approvalAdapter;
    private int HTTP_SUBMIT_BUYER_APPROVAL = 166;
    private MyHttpHelper httpHelper;
    private CustomProgress progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_SUBMIT_BUYER_APPROVAL) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(BuyerApprovalActivity.this, "操作成功");
                    BuyerApprovalActivity.this.finish();
                } else {
                    Toastor.showToast(BuyerApprovalActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 100) {
                int index = (int) msg.obj;
                listBa.remove(index);
                buyerAdapter.notifyDataSetChanged();
                setBuyerDetailsVisibility(listBa);
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_buyer_approval;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(this);
        progress = new CustomProgress(this, "请稍候...");
        badm = new BuyerApprovalDetailModel();
        llLeft.setVisibility(View.VISIBLE);
        llLeft.setOnClickListener(this);
        rlApprovalPerson.setOnClickListener(this);
        rlChooseDate.setOnClickListener(this);
        tvMidTitle.setText("采购审批");
        if (payType.equals("1")) {
            imgCard.setImageResource(R.mipmap.img_choosed);
            imgCash.setImageResource(R.mipmap.img_no_choose);
        } else {
            imgCash.setImageResource(R.mipmap.img_choosed);
            imgCard.setImageResource(R.mipmap.img_no_choose);
        }

        tvBaseLeft.setVisibility(View.VISIBLE);
        tvBaseLeft.setText("取消");

        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("下一步");
        tvBaseRight.setOnClickListener(this);

        llByCard.setOnClickListener(this);
        llByCash.setOnClickListener(this);
        rlBuyerDetails.setOnClickListener(this);
        setBuyerDetailsVisibility(listBa);

        rlAddToDetails.setOnClickListener(this);
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        buyerAdapter = new BuyerRecyclerAdapter(this, listBa, handler);
        recyclerViewDetails.setAdapter(buyerAdapter);

        approvalAdapter = new ApprovalMenberRecyclerAdapter(this, listMenber);
        recyclerViewApproval.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewApproval.setAdapter(approvalAdapter);
    }

    /**
     * 设置是否显示
     *
     * @param list
     */
    private void setBuyerDetailsVisibility(List<BuyerApprovalDetailModel.BuyerApprovalDetail.BuyerApproval> list) {
        if (list.size() > 0) {
            rlBuyerDetails.setVisibility(View.GONE);
            llBuyerDetails.setVisibility(View.VISIBLE);
            Double totalClaims = 0.00;
            for (BuyerApprovalDetailModel.BuyerApprovalDetail.BuyerApproval buyer : listBa) {
                totalClaims += Double.parseDouble(buyer.getpPrice());
            }
            tvTotalClaims.setText(totalClaims + "元");
        } else {
            rlBuyerDetails.setVisibility(View.VISIBLE);
            llBuyerDetails.setVisibility(View.GONE);
            tvTotalClaims.setText("0.0元");
        }
    }

    /**
     * 组装请求数据
     */
    private void assembleData() {
        BuyerApprovalDetailModel.BuyerApprovalDetail bad = new BuyerApprovalDetailModel().
                new BuyerApprovalDetail();
        BuyerApprovalDetailModel.BuyerApprovalDetail.Purchase pur = new BuyerApprovalDetailModel().
                new BuyerApprovalDetail().new Purchase();
        pur.setReason(etBuyerReason.getText().toString());
        pur.setRemark(etBuyerRemark.getText().toString());
        pur.setPayType(payType);
        pur.setpDate(tvBuyerTime.getText().toString());
        bad.setDetails(listBa);
        bad.setPurchase(pur);
        List<BuyerApprovalDetailModel.BuyerUser> listBu = new ArrayList<>();
        for (BottomUserModel bu : listMenber) {
            BuyerApprovalDetailModel.BuyerUser buyer =
                    new BuyerApprovalDetailModel().new BuyerUser();
            buyer.setDeptId(bu.getUserId());
            buyer.setUserId(bu.getUserId());
            listBu.add(buyer);
        }
        badm.setUsers(listBu);
        badm.setForm(bad);
        progress.show();
        httpHelper.postStringBack(HTTP_SUBMIT_BUYER_APPROVAL, AppConfig.SUBMIT_BUYER_APPROVAL, submit(), handler, BaseModel.class);
    }

    private HashMap<String, String> submit() {
        HashMap<String, String> submit = new HashMap<String, String>();
        submit.put("token", TelephoneUtil.getIMEI(this));
        String data = GsonTools.obj2json(badm);
        submit.put("data", data);
        return submit;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6709 && resultCode == 1008) {
            Bundle b = data.getExtras();
            List<BottomUserModel> temp = (List<BottomUserModel>) b.getSerializable("meeting_back_data");
            listMenber.addAll(temp);
            approvalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_left:
                if ("下一步".equals(tvBaseRight.getText().toString()) && "采购审批".equals(tvMidTitle.getText().toString())) {
                    this.finish();
                } else {
                    if ("采购明细".equals(tvMidTitle.getText().toString())) {
                        buyerBanner.setVisibility(View.GONE);
                        buyerFirst.setVisibility(View.VISIBLE);
                        tvMidTitle.setText("采购审批");
                        tvBaseRight.setText("下一步");
                    } else {
                        buyerNext.setVisibility(View.GONE);
                        buyerFirst.setVisibility(View.VISIBLE);
                        tvBaseRight.setText("下一步");
                    }
                }
                break;
            case R.id.tv_base_right:
                if ("下一步".equals(tvBaseRight.getText().toString())) {
                    if (Check.isEmpty(etBuyerReason.getText().toString()) ||
                            Check.isEmpty(etBuyerType.getText().toString()) ||
                            Check.isEmpty(etBuyerRemark.getText().toString())) {
                        Toastor.showToast(BuyerApprovalActivity.this, "请补全明细信息!");
                    } else {
                        buyerFirst.setVisibility(View.GONE);
                        buyerNext.setVisibility(View.VISIBLE);
                        tvBaseRight.setText("提交");
                    }
                } else if ("提交".equals(tvBaseRight.getText().toString())) {
                    assembleData();
                } else {
                    //Todo 添加报销明细
                    if (Check.isEmpty(etGoodsName.getText().toString()) ||
                            Check.isEmpty(etGoodsNumber.getText().toString()) ||
                            Check.isEmpty(etGoodsPrice.getText().toString()) ||
                            Check.isEmpty(etGoodsSpec.getText().toString()) ||
                            Check.isEmpty(etGoodsUnit.getText().toString())) {
                        Toastor.showToast(BuyerApprovalActivity.this, "请补全明细信息!");
                    } else {
                        BuyerApprovalDetailModel.BuyerApprovalDetail.BuyerApproval ba = new BuyerApprovalDetailModel().
                                new BuyerApprovalDetail().new BuyerApproval();
                        ba.setpName(etGoodsName.getText().toString());
                        ba.setpNum(etGoodsNumber.getText().toString());
                        ba.setpPrice(etGoodsPrice.getText().toString());
                        ba.setpSepc(etGoodsSpec.getText().toString());
                        ba.setpUnit(etGoodsUnit.getText().toString());
                        listBa.add(ba);
                        buyerFirst.setVisibility(View.VISIBLE);
                        buyerBanner.setVisibility(View.GONE);
                        setBuyerDetailsVisibility(listBa);
                        etGoodsName.setText("");
                        etGoodsNumber.setText("");
                        etGoodsPrice.setText("");
                        etGoodsSpec.setText("");
                        etGoodsUnit.setText("");
                        tvMidTitle.setText("采购审批");
                        tvBaseRight.setText("下一步");
                    }
                }
                break;
            case R.id.ll_buyer_approval_cash:
                imgCard.setImageResource(R.mipmap.img_choosed);
                imgCash.setImageResource(R.mipmap.img_no_choose);
                payType = "1";
                break;
            case R.id.ll_buyer_approval_card:
                imgCash.setImageResource(R.mipmap.img_choosed);
                imgCard.setImageResource(R.mipmap.img_no_choose);
                payType = "1";
                break;
            case R.id.rl_buyer_list_details:
                if ("采购审批".equals(tvMidTitle.getText().toString())) {
                    buyerBanner.setVisibility(View.VISIBLE);
                    buyerFirst.setVisibility(View.GONE);
                    tvMidTitle.setText("采购明细");
                    tvBaseRight.setText("确定");
                }
                break;
            case R.id.rl_add_to_approval_details:
                if ("采购审批".equals(tvMidTitle.getText().toString())) {
                    buyerBanner.setVisibility(View.VISIBLE);
                    tvMidTitle.setText("采购明细");
                    buyerFirst.setVisibility(View.GONE);
                    tvBaseRight.setText("确定");
                }
                break;
            case R.id.rl_choose_approval_person:
                //TODO 选择审批人
                Intent appIntent = new Intent(this, AAAAActivity.class);
                appIntent.putExtra("come_from_meeting", "buyer_approval");
                startActivityForResult(appIntent, 6709);
                break;
            case R.id.rl_choose_approval_date:
                CalendarDialog calendarDialog = new CalendarDialog(this).builder();
                calendarDialog.setDateCallBack(new DateCallBack() {
                    @Override
                    public void callBack(Object o) {
                        Toastor.showToast(BuyerApprovalActivity.this, o.toString());
                    }
                });
                calendarDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 重写返回键
     */


    public class BuyerRecyclerAdapter extends RecyclerView.Adapter {
        private Context context;
        private List<BuyerApprovalDetailModel.BuyerApprovalDetail.BuyerApproval> list;
        private Handler handler;

        public BuyerRecyclerAdapter(Context context, List<BuyerApprovalDetailModel.BuyerApprovalDetail.BuyerApproval>
                list, Handler handler) {
            this.context = context;
            this.list = list;
            this.handler = handler;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_recycler_buyer_approval_details_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((MyViewHolder) holder).tvGoodName.setText(list.get(position).getpName());
            ((MyViewHolder) holder).tvGoodDesc.setText(list.get(position).getpName() +
                    list.get(position).getpNum() + list.get(position).getpUnit());
            ((MyViewHolder) holder).tvGoodType.setText(list.get(position).getpSepc());
            ((MyViewHolder) holder).tvGoodPrice.setText(list.get(position).getpPrice());
            ((MyViewHolder) holder).tvDelGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    msg.what = 100;
                    msg.obj = position;
                    handler.sendMessage(msg);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvGoodName;
            TextView tvGoodDesc;
            TextView tvGoodType;
            TextView tvGoodPrice;
            TextView tvDelGoods;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvGoodName = (TextView) itemView.findViewById(R.id.tv_buyer_approval_details_name);
                tvGoodDesc = (TextView) itemView.findViewById(R.id.tv_approval_details_desc);
                tvGoodType = (TextView) itemView.findViewById(R.id.tv_approval_details_type);
                tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_approval_details_price);
                tvDelGoods = (TextView) itemView.findViewById(R.id.tv_del_buyer_details);
            }
        }
    }
}
