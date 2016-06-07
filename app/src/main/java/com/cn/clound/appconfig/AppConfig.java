package com.cn.clound.appconfig;

import com.cn.clound.bean.choose.RadioOrMultDeptUser;

import java.util.ArrayList;
import java.util.List;

/**
 * APP全局配置文件
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年3月29日12:12:32
 */
public class AppConfig {
    //请求主地址
    public static String HOST_URL = "http://192.168.0.107:7070";//http://192.168.0.112:8080/interface_server
    //WeChatA PP_ID
    public static String APP_ID = "wx6c1b5dd66261416e";
    // Tencent QQ APP_ID
    public static String QQ_APP_ID = "1105267193";
    //应用包名
    public static String PACKAGE_NAME = "com.cn.clound";
    //APPKEY
    public static String APP_KEY = "SZDT#$2016";
    //是否是上下级单位路径进入
    public static boolean IS_HIERARCHY = true;
    /**********************
     * 接口地址配置 start
     ***********************/
    //登录接口名
    public static String USER_LOGIN = HOST_URL + "/common/login";
    // 获取用户信息
    public static String GET_USER_INFO = HOST_URL + "/user/findUser";
    // 获取验证码
    public static String GET_SMS_CAPTCHA = HOST_URL + "/common/applyCode";
    //根据单位Id获取部门列表
    public static String GET_DEPT_LIST_BY_ID = HOST_URL + "/user/findDepList";
    //根据单位Id查询单位负责人接口
    public static String GET_DEPT_MANAGER_LISTBY_ID = HOST_URL + "/user/findUnitMangerList";
    //根据部门Id获取部门成员接口
    public static String GET_DEPT_MENBERS_BY_ID = HOST_URL + "/user/findDepUserList";
    //根据手机号添加部门成员接口
    public static String INSERT_DEP_USER_BY_PHONE = HOST_URL + "/user/insertDepUser";
    //删除部门用户接口
    public static String DELETE_DEPT_MENBER = HOST_URL + "/user/delDepUser";
    //转移部门用户接口
    public static String TRANSFER_DEPT_MENBER = HOST_URL + "/user/transferDepUser";
    //添加单位负责人接口
    public static String ADD_RESPONBILE = HOST_URL + "/user/insertUnitSupervise";
    //获取上下级单位接口
    public static String GET_HIERARHCY_LIST = HOST_URL + "/user/findUnitList";
    //超级管理添加部门接口
    public static String ADD_DEPT = HOST_URL + "/user/insertDep";
    //超级管理员删除部门接口
    public static String DELETE_DEPT = HOST_URL + "/user/delDep";
    //分享数据接口
    public static String GET_SHARE_MSG = HOST_URL + "/common/share";
    //获取常用联系人接口
    public static String GET_TOP_CONTACTS_LIST = HOST_URL + "/user/findTopContactsList";
    //添加常用联系人接口
    public static String ADD_TOP_CONTACTS = HOST_URL + "/user/insertTopContacts";
    //删除常用联系人接口
    public static String DELETE_TOP_CONTACTS = HOST_URL + "/user/delTopContacts";
    //删除单位负责人接口
    public static String DELETE_DEPT_MANAGER = HOST_URL + "/user/delUnitSupervise";
    //根据手机号添加部门主管接口
    public static String ADD_DEPT_MANAGER_BY_PHONE = HOST_URL + "/user/insertDepManager";
    //获取群组列表接口
    public static String GET_CHAT_ROOM_LIST = HOST_URL + "/user/findGroupList";
    //获取群组列表接口
    public static String CREATE_CHAT_ROOM = HOST_URL + "/user/insertGroup";
    //根据环信群组Id查询群组群组信息
    public static String QUERY_CHAT_GROUP_BY_HXNO = HOST_URL + "/user/findGroupModelByImGroupId";
    //根据群组编号查询群组成员
    public static String QUERY_CHAT_MENBER_BY_GROUP_NO = HOST_URL + "/user/findGroupUsers";
    //打开和关闭保存到通讯录接口
    public static String IS_SAVE_TO_CONTACTS = HOST_URL + "/user/settingSaveState";
    //添加群组成员接口
    public static String ADD_CHAT_GROUP_MENBER = HOST_URL + "/user/insertGroupUser";
    //删除群组成员接口
    public static String DEL_CHAT_GROUP_MENBER = HOST_URL + "/user/delGroupUser";
    //修改群组名称
    public static String UPDATE_CHAT_GROUP_NAME = HOST_URL + "/user/updateGroupName";
    // 退出群聊接口
    public static String EXIT_GROUP = HOST_URL + "/user/exitGroup";
    // 删除群组接口
    public static String DELETE_GROUP = HOST_URL + "/user/delGroup";
    //根据单位Id查询未分配部门的用户
    public static String GET_UN_ALLOCATED_MENBER = HOST_URL + "/user/findUnitlUserNoDep";
    //根据单位Id查询所有单位用户接口
    public static String GET_ALL_UNIT_MENBER_BY_UNITID = HOST_URL + "/user/findUnitUserList";
    //用户签到
    public static String USER_SIGNIN = HOST_URL + "/signIn/set";
    //查询我的签到
    public static String QUERY_USER_SIGNIN_LIST = HOST_URL + "/signIn/get";
    //查询组织签到
    public static String QUERY_GROUP_SIGNIN_LIST = HOST_URL + "/signIn/getGroup";
    //设置签到提醒
    public static String SET_MY_SIGN_TIP = HOST_URL + "/signIn/remind/set";
    //查询签到提醒
    public static String QUERY_MY_SIGN_TIP = HOST_URL + "/signIn/remind/get";
    //删除提醒
    public static String DEL_MY_SIGN_TIP = HOST_URL + "/signIn/remind/remove";
    //删除未分配部门的成员
    public static String DEL_UN_ALLOCATED_MENBER = HOST_URL + "/user/delNoDepUser";
    //发布会议
    public static String CREATE_MEETING = HOST_URL + "/meetingMng/publish";
    //查询我的当前会议列表
    public static String GET_MINE_MEETING = HOST_URL + "/meeting/get";
    //查询我的历史会议
    public static String QUERY_MINE_HISTORY_MEETING = HOST_URL + "/meeting/getHistory";
    //删除我的历史会议
    public static String DELETE_MINE_HISTORY_MEETING = HOST_URL + "/meeting/remove";
    //进入会场
    public static String ENTER_MEETING = HOST_URL + "/meeting/enter";
    //会议管理-发布中的会议
    public static String GET_PUBLISH_MEETING = HOST_URL + "/meetingMng/getTop";
    //会议管理-历史会议
    public static String GET_MANAGER_HISTORY_MEETING = HOST_URL + "/meetingMng/getHistory";
    //查询会议详情
    public static String QUERY_MEETING_DETAILS = HOST_URL + "/meeting/getDetail";
    //获取会议发布人员
    public static String GET_PUBLISH_MEETING_PERSON = HOST_URL + "/meetingMng/getManagers";
    //添加会议发布人
    public static String ADD_MEETING_PUBLISH_PERSON = HOST_URL + "/meetingMng/setManager";
    //会议考情统计
    public static String GET_ABSENT_AND_LATE_MEETING_MENBER = HOST_URL + "/meetingMng/attendStatistics";
    //删除会议发布人
    public static String DEL_MEETING_PUBLISH_PERSON = HOST_URL + "/meetingMng/deleteManager";
    //会议签到
    public static String MEETING_SIGN = HOST_URL + "/meeting/sign";
    //开始会议
    public static String START_MEETING = HOST_URL + "/meeting/begin";
    //结束会议
    public static String END_MEETING = HOST_URL + "/meeting/end";
    //用户退出会场
    public static String LEAVE_MEETING = HOST_URL + "/meeting/leave";
    //修改会议
    public static String UPDATE_MEETING = HOST_URL + "/meetingMng/update";
    //终止会议
    public static String DROPPED_MEETING = HOST_URL + "/meetingMng/cancel";
    //获取自己的所有角色
    public static String GET_MINE_ALL_ROLE = HOST_URL + "/common/findRoleList";

    /**********************接口地址配置 end************************/

    /***************部门管理静态变量*************/
    public static String DEPT_MANAGER_NAME = "";
    public static String DEPT_MANAGER_PHONE = "";
    public static String DEPT_NAMAGER_DUTY = "";
    public static String DEPT_TRANSFER_FLAG = "";
    public static String DEPT_TRANSFER_DEPTNAME = "";
    public static String DEPT_TRANSFER_USER = "";

    public static String CHAT_GROUP_REFUSER = "";

    public static boolean DEPT_MENBER_MUTIL = false;
    public static List<RadioOrMultDeptUser> listTemp = new ArrayList<RadioOrMultDeptUser>();

    public static boolean BACK_SHUA_XIN = false;
    public static boolean IS_DT_CHAT_BACK = false;
    /**********************
     * 接口错误码返回 start
     ***********************/
    public static String SUCCESS = "200";//服务器返回成功
    public static String HADLOGIN = "203";//已经在其他设备登录
    public static String SYSTEMBUSY = "500";//服务器繁忙
    /**********************接口错误码返回 end************************/
}
