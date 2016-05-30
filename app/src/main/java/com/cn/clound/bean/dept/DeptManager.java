package com.cn.clound.bean.dept;

import com.cn.clound.bean.BaseBean;

import java.util.List;

/**
 * 部门管理整体类
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月25日 14:32:28
 */
public class DeptManager extends BaseBean {
    private FindUnitMangerListModel.UserListModel um;
    private FindDepListModel.DepModel dm;
    private boolean isCheckedHas;

    public boolean isCheckedHas() {
        return isCheckedHas;
    }

    public void setCheckedHas(boolean checkedHas) {
        isCheckedHas = checkedHas;
    }

    public FindUnitMangerListModel.UserListModel getUm() {
        return um;
    }

    public void setUm(FindUnitMangerListModel.UserListModel um) {
        this.um = um;
    }

    public FindDepListModel.DepModel getDm() {
        return dm;
    }

    public void setDm(FindDepListModel.DepModel dm) {
        this.dm = dm;
    }
}
