package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.cn.clound.R;
import com.cn.clound.interfaces.OnItemClickListener;
import com.cn.clound.adapter.SingedLocationRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.Log;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.singed.SingedPoiModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 签到定位界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-9 20:00:59
 */
public class SingedLocationActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.map_singed_location)
    MapView mMapView;
    @Bind(R.id.recycler_singed_loacation_list)
    RecyclerView recyclerView;
    @Bind(R.id.ll_bottom_submit)
    LinearLayout llSubmit;
    private BaiduMap mBaidumap;
    private CustomProgress progress;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private SingedLocationRecyclerAdapter adapter;
    private List<SingedPoiModel> listPoi;
    private int index;
    private BDLocation bdLocation;
    private int HTTP_USER_SIGNIN = 132;
    private int HTTP_MEETING_SIGN = 154;
    private MyHttpHelper httpHelper;
    private String timeId;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_USER_SIGNIN) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(SingedLocationActivity.this, "签到成功");
                    SingedLocationActivity.this.finish();
                } else {
                    Toastor.showToast(SingedLocationActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_MEETING_SIGN) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(SingedLocationActivity.this, "签到成功");
                    SingedLocationActivity.this.finish();
                } else {
                    Toastor.showToast(SingedLocationActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 100) {
                bdLocation = (BDLocation) msg.obj;
                if (bdLocation != null) {
                    LatLng cenpt = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    //定义地图状态
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(cenpt).zoom(18).build();
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    mBaidumap.setMapStatus(mMapStatusUpdate);
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.mipmap.icon_openmap_mark);
                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions()
                            .position(cenpt)
                            .icon(bitmap);
                    //在地图上添加Marker，并显示
                    mBaidumap.addOverlay(option);
                    List<Poi> temp = bdLocation.getPoiList();
                    listPoi = new ArrayList<SingedPoiModel>();
                    for (Poi poi : temp) {
                        SingedPoiModel spm = new SingedPoiModel();
                        spm.setLocation(bdLocation);
                        spm.setPoi(poi);
                        spm.setChoosed(false);
                        listPoi.add(spm);
                    }
                    adapter = new SingedLocationRecyclerAdapter(SingedLocationActivity.this, listPoi);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickLitener(SingedLocationActivity.this);
                    mLocationClient.stop();
                } else {
                    Toastor.showToast(SingedLocationActivity.this, "败获取定位信息失败!");
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_singed_location;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        llSubmit.setOnClickListener(this);
        tvMidTitle.setText("签到定位");
        progress = new CustomProgress(this, "请稍候...");
        progress.show();
        settingsMap();
        httpHelper = MyHttpHelper.getInstance(this);
        mBaidumap = mMapView.getMap();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();
        timeId = this.getIntent().getStringExtra("timeId");
    }

    /**
     * 设置百度地图
     */
    private void settingsMap() {
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        mMapView.onResume();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mMapView.onPause();
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
        mMapView.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                finish();
                break;
            case R.id.ll_bottom_submit:
                progress.show();
                if (timeId != null && !timeId.equals("")) {
                    httpHelper.postStringBack(HTTP_MEETING_SIGN, AppConfig.MEETING_SIGN, meetingSign(timeId), handler, BaseModel.class);
                } else {
                    httpHelper.postStringBack(HTTP_USER_SIGNIN, AppConfig.USER_SIGNIN, signIn(), handler, BaseModel.class);
                }
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> meetingSign(String timeId) {
        HashMap<String, String> sign = new HashMap<String, String>();
        sign.put("token", TelephoneUtil.getIMEI(this));
        sign.put("signLoc", listPoi.get(index).getPoi().getName());
        sign.put("signLat", bdLocation.getLatitude() + "");
        sign.put("signLon", bdLocation.getLongitude() + "");
        sign.put("timeId", timeId);
        return sign;
    }

    /**
     * @return
     */
    private HashMap<String, String> signIn() {
        HashMap<String, String> sign = new HashMap<String, String>();
        sign.put("token", TelephoneUtil.getIMEI(this));
        sign.put("signLat", bdLocation.getLatitude() + "");
        sign.put("signLon", bdLocation.getLongitude() + "");
        sign.put("signAddress", listPoi.get(index).getPoi().getName());
        return sign;
    }

    /**
     * 初始化设置百度定位
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onItemClick(View view, int position) {
        boolean isShowSubmit = false;
        for (int i = 0; i < listPoi.size(); i++) {
            if (position == i && !listPoi.get(position).isChoosed()) {
                listPoi.get(i).setChoosed(true);
            } else {
                listPoi.get(i).setChoosed(false);
            }
        }
        for (int j = 0; j < listPoi.size(); j++) {
            if (listPoi.get(j).isChoosed()) {
                isShowSubmit = true;
                index = j;
            }
        }
        if (isShowSubmit) {
            llSubmit.setVisibility(View.VISIBLE);
        } else {
            llSubmit.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Message msg = new Message();
            msg.what = 100;
            msg.obj = location;
            handler.sendMessage(msg);
            Log.e("BaiduLocationApiDem", sb.toString());
//            PoiSearch mPoiSearch = PoiSearch.newInstance();
        }
    }
}
