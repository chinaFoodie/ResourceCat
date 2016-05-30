package com.cn.clound.bean.choose;

import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.dept.FindDepUserListModel;

/**
 * 部门成员列表控制单选或者多选
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-28 18:22:39
 */
public class RadioOrMultDeptUser extends BaseModel {
    private boolean hadChecked;
    private FindDepUserListModel.DepUser user;

    public boolean isHadChecked() {
        return hadChecked;
    }

    public void setHadChecked(boolean hadChecked) {
        this.hadChecked = hadChecked;
    }

    public FindDepUserListModel.DepUser getUser() {
        return user;
    }

    public void setUser(FindDepUserListModel.DepUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "RadioOrMultDeptUser{" +
                "hadChecked=" + hadChecked +
                ", user=" + user +
                '}';
    }
}
