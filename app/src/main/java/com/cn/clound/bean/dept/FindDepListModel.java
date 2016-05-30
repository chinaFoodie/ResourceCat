package com.cn.clound.bean.dept;

import com.cn.clound.bean.BaseModel;

import java.util.ArrayList;
import java.util.List;


public class FindDepListModel extends BaseModel {
	
	private FindDepListData data;
	
	
	public FindDepListData getData() {
		return data;
	}


	public void setData(FindDepListData data) {
		this.data = data;
	}


	public static class FindDepListData{
		List<DepModel> dep=new ArrayList<DepModel>();

		public List<DepModel> getDep() {
			return dep;
		}

		public void setDep(List<DepModel> dep) {
			this.dep = dep;
		}
	}
	
	public static class DepModel{
		private String depNo;
		private String depName;
		private String depUserCount;
		public String getDepNo() {
			return depNo;
		}
		public void setDepNo(String depNo) {
			this.depNo = depNo;
		}
		public String getDepName() {
			return depName;
		}
		public void setDepName(String depName) {
			this.depName = depName;
		}
		public String getDepUserCount() {
			return depUserCount;
		}
		public void setDepUserCount(String depUserCount) {
			this.depUserCount = depUserCount;
		}
	}
}
