package com.cn.clound.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.ApprovalMenberRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.Log;
import com.cn.clound.base.common.assist.Check;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.gallery.GalleryActivity;
import com.cn.clound.base.common.gallery.MyImageView;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.approval.WipedApprovalModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.interfaces.OnItemClickListener;
import com.cn.clound.view.ActionSheetDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

/**
 * 报销审批界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-15 15:36:34
 */
public class WipedApprovalActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_left)
    LinearLayout llLeft;
    @Bind(R.id.tv_base_left)
    TextView tvBaseLeft;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.dt_include_swiped_banner)
    View wipedBanner;
    @Bind(R.id.dt_include_swiped_edit)
    View wipedEdit;
    @Bind(R.id.rl_swiped_list_details)
    RelativeLayout rlWipedDetails;
    @Bind(R.id.recycler_buyer_approval_details)
    RecyclerView recyclerViewDetails;
    @Bind(R.id.recycler_wiped_approval_picture)
    RecyclerView recyclerViewWipedPic;
    @Bind(R.id.recycler_approval_person)
    RecyclerView recyclerPerson;
    @Bind(R.id.ll_get_wiped_pic)
    LinearLayout llGetWiped;
    @Bind(R.id.et_approval_swiped_name)
    EditText etWipedName;
    @Bind(R.id.et_approval_swiped_money)
    EditText etWipedMoney;
    @Bind(R.id.et_approval_swiped_type)
    EditText etWipedType;
    @Bind(R.id.et_approval_swiped_details)
    EditText etWipedDetails;
    @Bind(R.id.ll_buyer_list_details)
    LinearLayout llListDetails;
    @Bind(R.id.rl_choose_approval_person)
    RelativeLayout rlApprovalPerson;

    private List<String> listWiped = new ArrayList<>();
    private List<WipedApprovalModel.WipedApproval.Approval> listDetails = new ArrayList<>();
    private WipedPicAdapter wipedPicAdapter;
    private WipedRecyclerAdapter detailsAdapter;
    private ApprovalMenberRecyclerAdapter approvalAdapter;
    private WipedApprovalModel wam = new WipedApprovalModel();
    private MyHttpHelper httpHelper;
    private int HTTP_SUBMIT_WIPED = 167;
    private List<BottomUserModel> listMenber = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_SUBMIT_WIPED) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(WipedApprovalActivity.this, "提交成功");
                    WipedApprovalActivity.this.finish();
                } else {
                    Toastor.showToast(WipedApprovalActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 100) {
                if (listDetails.size() > 0) {
                    detailsAdapter = new WipedRecyclerAdapter(WipedApprovalActivity.this, listDetails, handler);
                    llListDetails.setVisibility(View.VISIBLE);
                    rlWipedDetails.setVisibility(View.GONE);
                    recyclerViewDetails.setAdapter(detailsAdapter);
                } else {
                    llListDetails.setVisibility(View.GONE);
                    rlWipedDetails.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_wiped_approval;
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
        llLeft.setVisibility(View.VISIBLE);
        llLeft.setOnClickListener(this);
        tvBaseLeft.setVisibility(View.VISIBLE);
        tvBaseLeft.setText("取消");
        tvMidTitle.setText("报销审批");

        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("提交");
        tvBaseRight.setOnClickListener(this);

        rlWipedDetails.setOnClickListener(this);
        llGetWiped.setOnClickListener(this);
        rlApprovalPerson.setOnClickListener(this);

        recyclerViewWipedPic.setLayoutManager(new GridLayoutManager(this, 4));
        wipedPicAdapter = new WipedPicAdapter(this, listWiped);
        recyclerViewWipedPic.setAdapter(wipedPicAdapter);
        wipedPicAdapter.setOnItemClickListener(this);

        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        detailsAdapter = new WipedRecyclerAdapter(this, listDetails, handler);
        recyclerViewDetails.setAdapter(detailsAdapter);

        recyclerPerson.setLayoutManager(new GridLayoutManager(this, 4));
        approvalAdapter = new ApprovalMenberRecyclerAdapter(this, listMenber);
        recyclerPerson.setAdapter(approvalAdapter);
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
    public void onItemClick(View view, int position) {
        if (position == listWiped.size()) {
            new ActionSheetDialog(WipedApprovalActivity.this).builder()
                    .setTitle("添加图片")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Red,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, 6710);
                                }
                            })
                    .addSheetItem("相册选取", ActionSheetDialog.SheetItemColor.Red,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent galleyIntent = new Intent(WipedApprovalActivity.this, GalleryActivity.class);
                                    startActivityForResult(galleyIntent, 6712);
                                }
                            })
                    .show();
        } else {
            Toastor.showToast(this, "" + position);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    /**
     * 报销图片适配器
     */
    class WipedPicAdapter extends RecyclerView.Adapter {
        private Context context;
        private List<String> listPic;

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public WipedPicAdapter(Context context, List<String> listPic) {
            this.context = context;
            this.listPic = listPic;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PicViewHolder holder = new PicViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.wiped_grid_child_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (position == listPic.size() || listPic == null) {
                ((PicViewHolder) holder).myImageView.setImageResource(R.mipmap.img_choose_galley);
            } else {
                ImageLoader.getInstance().displayImage("file://" + listPic.get(position), ((PicViewHolder) holder).myImageView);
            }
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return listPic != null ? listPic.size() + 1 : 1;
        }

        class PicViewHolder extends RecyclerView.ViewHolder {
            MyImageView myImageView;

            public PicViewHolder(View itemView) {
                super(itemView);
                myImageView = (MyImageView) itemView.findViewById(R.id.child_image);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMddHHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpeg";
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            FileOutputStream b = null;
            File file = new File("/sdcard/DCIM/image/");
            file.mkdirs();// 创建文件夹
            String fileName = "/sdcard/DCIM/image/" + name;
            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toastor.showToast(this, fileName);
            listWiped.add(fileName);
            wipedPicAdapter = new WipedPicAdapter(this, listWiped);
            recyclerViewWipedPic.setAdapter(wipedPicAdapter);
            wipedPicAdapter.setOnItemClickListener(this);
            try {
//                view.setImageBitmap(bitmap);// 将图片显示在ImageView里
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (requestCode == 6712) {
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                List<String> tempList = (List<String>) bundle.getSerializable("wiped_pic_list");
                listWiped.addAll(tempList);
                wipedPicAdapter = new WipedPicAdapter(this, listWiped);
                recyclerViewWipedPic.setAdapter(wipedPicAdapter);
                wipedPicAdapter.setOnItemClickListener(this);
            }
        } else if (requestCode == 6713) {
            Bundle b = data.getExtras();
            List<BottomUserModel> temp = (List<BottomUserModel>) b.getSerializable("meeting_back_data");
            listMenber.addAll(temp);
            approvalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_swiped_list_details:
                wipedEdit.setVisibility(View.GONE);
                wipedBanner.setVisibility(View.VISIBLE);
                tvBaseRight.setText("确定");
                tvMidTitle.setText("报销明细");
                break;
            case R.id.ll_base_left:
                if ("报销审批".equals(tvMidTitle.getText().toString())) {
                    this.finish();
                } else {
                    wipedBanner.setVisibility(View.GONE);
                    wipedEdit.setVisibility(View.VISIBLE);
                    tvBaseRight.setText("提交");
                    tvMidTitle.setText("报销审批");
                }
                break;
            case R.id.tv_base_right:
                if ("提交".equals(tvBaseRight.getText().toString())) {
                    WipedApprovalModel.WipedApproval.Form form = new WipedApprovalModel()
                            .new WipedApproval().new Form();
                    form.setDetails(listDetails);
                    WipedApprovalModel.WipedApproval wa = new WipedApprovalModel().new WipedApproval();
                    wa.setForm(form);
                    List<WipedApprovalModel.WipedApproval.User> listBu = new ArrayList<>();
                    for (BottomUserModel bu : listMenber) {
                        WipedApprovalModel.WipedApproval.User buyer = new WipedApprovalModel().
                                new WipedApproval().new User();
                        buyer.setDeptId(bu.getUserId());
                        buyer.setUserId(bu.getUserId());
                        listBu.add(buyer);
                    }
                    wa.setUsers(listBu);
                    wam.setData(wa);
                    httpHelper.postStringBack(HTTP_SUBMIT_WIPED, AppConfig.SUBMIT_WIPED_APPROVAL, submit(), handler, BaseModel.class);
                } else {
                    if (!Check.isEmpty(etWipedName.getText().toString()) ||
                            !Check.isEmpty(etWipedDetails.getText().toString()) ||
                            !Check.isEmpty(etWipedMoney.getText().toString()) ||
                            !Check.isEmpty(etWipedType.getText().toString())) {
                        wipedBanner.setVisibility(View.GONE);
                        wipedEdit.setVisibility(View.VISIBLE);
                        WipedApprovalModel.WipedApproval.Approval approval = new WipedApprovalModel()
                                .new WipedApproval().new Approval();
                        approval.setName(etWipedName.getText().toString());
                        approval.setChargesDetail(etWipedDetails.getText().toString());
                        approval.setMoney(etWipedMoney.getText().toString());
                        approval.setType(etWipedType.getText().toString());
                        listDetails.add(approval);
                        tvBaseRight.setText("提交");
                        tvMidTitle.setText("报销审批");
                        handler.sendEmptyMessage(100);
                    } else {
                        Toastor.showToast(WipedApprovalActivity.this, "请补全信息");
                    }
                }
                break;
            case R.id.ll_get_wiped_pic:
                break;
            case R.id.rl_choose_approval_person:
                Intent appIntent = new Intent(this, AAAAActivity.class);
                appIntent.putExtra("come_from_meeting", "buyer_approval");
                startActivityForResult(appIntent, 6713);
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> submit() {
        HashMap<String, String> submit = new HashMap<String, String>();
        submit.put("token", TelephoneUtil.getIMEI(this));
        String temp = GsonTools.obj2json(wam);
        String data = "";
        try {
            JSONObject json = new JSONObject(temp);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        submit.put("data", data);
        return submit;
    }


    public class WipedRecyclerAdapter extends RecyclerView.Adapter {
        private Context context;
        private List<WipedApprovalModel.WipedApproval.Approval> list;
        private Handler handler;

        public WipedRecyclerAdapter(Context context, List<WipedApprovalModel.WipedApproval.Approval>
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
            ((MyViewHolder) holder).tvGoodName.setText(list.get(position).getName());
            ((MyViewHolder) holder).tvGoodDesc.setText(list.get(position).getChargesDetail());
            ((MyViewHolder) holder).tvGoodType.setText(list.get(position).getType());
            ((MyViewHolder) holder).tvGoodPrice.setText(list.get(position).getMoney());
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
