package com.cn.clound.bean.User;

import com.cn.clound.bean.BaseModel;

import java.util.ArrayList;
import java.util.List;

/****
 * 我的联系人列表
 * @author onion
 *
 */
public class FindTopContactsModel extends BaseModel {
	
	private TopContactsData data;
	
	public TopContactsData getData() {
		return data;
	}
	public void setData(TopContactsData data) {
		this.data = data;
	}
	public static class TopContactsData{
		List<TopContactsModel> result=new ArrayList<TopContactsModel>();; 
		public List<TopContactsModel> getResult() {
			return result;
		}

		public void setResult(List<TopContactsModel> result) {
			this.result = result;
		}
	}
	public static class TopContactsModel{
		private String userNo;
		private String name;
		private String unitName;
		private String depName;
		private String head;
		public String getUserNo() {
			return userNo;
		}
		public void setUserNo(String userNo) {
			this.userNo = userNo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUnitName() {
			return unitName;
		}
		public void setUnitName(String unitName) {
			this.unitName = unitName;
		}
		public String getDepName() {
			return depName;
		}
		public void setDepName(String depName) {
			this.depName = depName;
		}
		public String getHead() {
			return head;
		}
		public void setHead(String head) {
			this.head = head;
		}
	}
}
