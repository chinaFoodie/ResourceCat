package com.cn.clound.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.gallery.GalleryActivity;
import com.cn.clound.base.common.gallery.MyImageView;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.approval.CommonApprovalModel;
import com.cn.clound.bean.approval.WipedApprovalModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.interfaces.OnItemClickListener;
import com.cn.clound.view.ActionSheetDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 通用审批界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-16 18:02:47
 */
public class CommonApprovalActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.recycler_common_approval_picture)
    RecyclerView recyclerViewPic;
    @Bind(R.id.recycler_approval_person)
    RecyclerView recyclerViewPerson;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.et_common_approval_name)
    EditText etCommonName;
    @Bind(R.id.et_common_approval_reason)
    EditText etCommonReason;
    @Bind(R.id.rl_choose_approval_person)
    RelativeLayout rlApprovalPerson;

    private WipedPicAdapter wipedPicAdapter;
    private ApprovalMenberRecyclerAdapter approvalAdapter;
    private List<String> listWiped = new ArrayList<>();
    private CommonApprovalModel cam = new CommonApprovalModel();
    private List<BottomUserModel> listMenber = new ArrayList<>();
    private MyHttpHelper httpHelper;
    private int HTTP_SUBMIT_COMMON_APPROVAL = 168;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_SUBMIT_COMMON_APPROVAL) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(CommonApprovalActivity.this, "操作成功");
                    CommonApprovalActivity.this.finish();
                } else {
                    Toastor.showToast(CommonApprovalActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_common_approval;
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
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("通用审批");

        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("提交");
        tvBaseRight.setOnClickListener(this);
        rlApprovalPerson.setOnClickListener(this);

        recyclerViewPic.setLayoutManager(new GridLayoutManager(this, 4));
        wipedPicAdapter = new WipedPicAdapter(this, listWiped);
        recyclerViewPic.setAdapter(wipedPicAdapter);
        wipedPicAdapter.setOnItemClickListener(this);

        recyclerViewPerson.setLayoutManager(new GridLayoutManager(this, 4));
        approvalAdapter = new ApprovalMenberRecyclerAdapter(this, listMenber);
        recyclerViewPerson.setAdapter(approvalAdapter);
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
        if (requestCode == 6715) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                listWiped = (List<String>) bundle.getSerializable("wiped_pic_list");
                wipedPicAdapter = new WipedPicAdapter(this, listWiped);
                recyclerViewPic.setAdapter(wipedPicAdapter);
                wipedPicAdapter.setOnItemClickListener(this);
            }
        } else if (requestCode == 6716) {
            Bundle b = data.getExtras();
            List<BottomUserModel> temp = (List<BottomUserModel>) b.getSerializable("meeting_back_data");
            listMenber.addAll(temp);
            approvalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            case R.id.rl_choose_approval_person:
                Intent appIntent = new Intent(this, AAAAActivity.class);
                appIntent.putExtra("come_from_meeting", "buyer_approval");
                startActivityForResult(appIntent, 6716);
                break;
            case R.id.tv_base_right:
                httpHelper.postStringBack(HTTP_SUBMIT_COMMON_APPROVAL, AppConfig.SUBMIT_COMMON_APPROVAL, submit(), handler, BaseModel.class);
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> submit() {
        HashMap<String, String> submit = new HashMap<String, String>();
        submit.put("token", TelephoneUtil.getIMEI(this));
        CommonApprovalModel.Form form = new CommonApprovalModel().new Form();
        CommonApprovalModel.CommonApproval ca = new CommonApprovalModel().new CommonApproval();
        form.setsName(etCommonName.getText().toString());
        form.setsText(etCommonReason.getText().toString());
        ca.setForm(form);
        List<WipedApprovalModel.WipedApproval.User> listBu = new ArrayList<>();
        for (BottomUserModel bu : listMenber) {
            WipedApprovalModel.WipedApproval.User buyer = new WipedApprovalModel().
                    new WipedApproval().new User();
            buyer.setDeptId(bu.getUserId());
            buyer.setUserId(bu.getUserId());
            listBu.add(buyer);
        }
        ca.setUsers(listBu);
        cam.setData(ca);
        String data = "";
        String temp = GsonTools.obj2json(cam);
        try {
            JSONObject json = new JSONObject(temp);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        submit.put("data", data);
        return submit;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == listWiped.size()) {
            new ActionSheetDialog(CommonApprovalActivity.this).builder()
                    .setTitle("添加图片")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Red,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, 6714);
                                }
                            })
                    .addSheetItem("相册选取", ActionSheetDialog.SheetItemColor.Red,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent galleyIntent = new Intent(CommonApprovalActivity.this, GalleryActivity.class);
                                    startActivityForResult(galleyIntent, 6715);
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
     * 图片适配器
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
}
