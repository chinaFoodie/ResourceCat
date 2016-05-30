package com.cn.clound.bean.singed;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.cn.clound.bean.BaseModel;

/**
 * 签到POI模型
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-10 10:23:53
 */
public class SingedPoiModel extends BaseModel {
    private Poi poi;
    private BDLocation location;
    private boolean isChoosed;

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public Poi getPoi() {
        return poi;
    }

    public void setPoi(Poi poi) {
        this.poi = poi;
    }

    public BDLocation getLocation() {
        return location;
    }

    public void setLocation(BDLocation location) {
        this.location = location;
    }
}
