package com.cn.clound.appconfig;

import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.dept.UnCallocateMenberModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-29 16:51:59
 */
public class PublicDataUtil {
    public static List<BottomUserModel> listBottom = new ArrayList<BottomUserModel>();

    public static boolean isContacs(List<BottomUserModel> list, String userId) {
        boolean flag = false;
        if (list != null) {
            for (BottomUserModel b : list) {
                if (b.getUserId().equals(userId)) {
                    flag = true;
                } else {

                }
            }
        }
        return flag;
    }

    public static void delete(List<BottomUserModel> list, String userId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserId().equals(userId)) {
                list.remove(list.get(i));
            }
        }
    }

    public static UnCallocateMenberModel.CallocateMenberModel.UnCallocateMenber addMenberData = null;//选择联系人界面返回添加成员界面
    public static List<BottomUserModel> listHasCunZai = new ArrayList<BottomUserModel>();
}
