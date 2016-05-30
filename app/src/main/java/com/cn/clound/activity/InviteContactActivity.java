package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.PhoneContactsAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.BaseUiListener;
import com.cn.clound.base.common.contacts.ContactModel;
import com.cn.clound.base.common.contacts.ContactsHelper;
import com.cn.clound.base.common.utils.PinyinUtils;
import com.cn.clound.bean.User.PhoneContacts;
import com.cn.clound.utils.PhoneContactsComparator;
import com.cn.clound.view.ActionSheetDialog;
import com.cn.clound.view.CustomProgress;
import com.cn.clound.view.SideBar;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Class Of InviteContact
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月14日 17:22:16
 */
public class InviteContactActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.join_contacts_list)
    ListView listViewJoin;
    @Bind(R.id.join_contacts_sidebar)
    SideBar sideBar;
    @Bind(R.id.join_contacts_floating_header)
    TextView tvFloat;
    private PhoneContactsAdapter adapter;

    private PhoneContactsComparator pinyinComparator;
    private ContactsHelper contactsHelper = new ContactsHelper(InviteContactActivity.this);
    private List<PhoneContacts> listPc = new ArrayList<PhoneContacts>();
    private int[] joinHeader = new int[]{R.string.phone_contacts_join_wechat, R.string.phone_contacts_join_qq, R.string.phone_contacts_join_phone};

    private CustomProgress progress;

    private IWXAPI api;
    private Tencent mTencent;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_invite_contact_layout;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        regToTencent();
        init();
    }

    /**
     * 注册到微信平台
     */
    private void regToTencent() {
        api = WXAPIFactory.createWXAPI(InviteContactActivity.this, AppConfig.APP_ID, true);
        boolean returnback = api.registerApp(AppConfig.APP_ID);
        if (returnback) {
        }
        mTencent = Tencent.createInstance(AppConfig.QQ_APP_ID, this.getApplicationContext());
    }

    /**
     * 初始化视图
     */
    private void init() {
        progress = new CustomProgress(InviteContactActivity.this, "加载中...");
        tvMidTitle.setText("邀请联系人");
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        sideBar.setTextView(tvFloat);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listViewJoin.setSelection(position);
                }
            }
        });
        pinyinComparator = new PhoneContactsComparator();
        new ContactTask().execute();
    }

    /**
     * 获取手机联系人
     */
    private void getPhoneContact() {
        if (listPc != null && listPc.size() > 0) {
            listPc.clear();
        }
        PhoneContacts pc;
        List<ContactModel> listModel = contactsHelper.getContactsList();
        for (int i = 0; i < listModel.size(); i++) {
            pc = new PhoneContacts();
            String name = listModel.get(i).getContactName();
            pc.setName(name);
            pc.setPhoneNo(listModel.get(i).getPhoneNumber());
            String temp = PinyinUtils.getPinYin(listModel.get(i).getContactName()).toUpperCase();
            pc.setSortLetter(temp);
//            pc.setFilter(PinyinUtils.getPinYinHeadChar(listModel.get(i).getContactName()).substring(0, 1).toUpperCase());
            listPc.add(pc);
        }
        Collections.sort(listPc, pinyinComparator);
        for (int j = 0; j < joinHeader.length; j++) {
            pc = new PhoneContacts();
            pc.setName(getResources().getString(joinHeader[j]));
            listPc.add(j, pc);
        }
        listViewJoin.setOnItemClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                new ActionSheetDialog(InviteContactActivity.this).builder()
                        .setTitle("微信分享")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("发送给好友", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        shareToFriend("深圳大腾联合专注为互联网教育提供移动资源平台", 0);
                                    }
                                })
                        .addSheetItem("发送到朋友圈", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        shareToFriend("深圳大腾联合专注为互联网教育提供移动资源平台", 1);
                                    }
                                })
                        .show();
                break;
            case 1:
                new ActionSheetDialog(InviteContactActivity.this).builder()
                        .setTitle("手机QQ分享")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("发送给QQ好友", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        shareToQQFriend("深圳大腾联合专注为互联网教育提供移动资源平台");
                                    }
                                })
                        .addSheetItem("发送到QQ动态", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        shareToQzone();
                                    }
                                })
                        .show();
                break;
            case 2:
                startActivity(new Intent(InviteContactActivity.this, PhoneJoinActivity.class));
                break;
            default:
//                new AlertDialog(InviteContactActivity.this).builder()
//                        .setMsg(listPc.get(position).getName() + ":" + listPc.get(position).getPhoneNo())
//                        .setNegativeButton("确定", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toastor.showToast(InviteContactActivity.this, "发送成功");
//                            }
//                        }).show();
                break;
        }
    }

    /**
     * 发送给QQ好友
     *
     * @param string
     */
    private void shareToQQFriend(String string) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "我们是一家有深度的公司");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "深圳大腾联合专注为互联网教育提供移动资源平台");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://news.sohu.com/20160424/n445867870.shtml");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "云在家校");
        mTencent.shareToQQ(InviteContactActivity.this, params, new BaseUiListener());
    }

    private void shareToQzone() {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "云在家校");
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "深圳大腾联合专注为互联网教育提供移动资源平台");
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://news.sohu.com/20160424/n445867870.shtml");
        ArrayList imageUrls = new ArrayList();
        imageUrls.add("http://img4.imgtn.bdimg.com/it/u=1635259526,3753247068&fm=21&gp=0.jpg");
//        imageUrls.add("http://img.wzfzl.cn/uploads/allimg/150719/co150G9101303-24.jpg");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQzone(InviteContactActivity.this, params, new BaseUiListener());
    }

    /**
     * 分享图片给好友
     *
     * @param
     */
    private void shareToFriend(String string, int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "www.baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "我们是一家有深度的公司";
        msg.description = string;
        // 这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.dt_main_work_meeting);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag;
        api.sendReq(req);
    }

    /**
     * 异步线程获取通讯录
     */
    public class ContactTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getPhoneContact();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new PhoneContactsAdapter(InviteContactActivity.this, listPc);
            listViewJoin.setAdapter(adapter);
            progress.dismiss();
        }
    }
}
