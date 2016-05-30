package com.cn.clound.bean.hierarchy;

import com.cn.clound.bean.BaseModel;

import java.util.ArrayList;
import java.util.List;
public class FindUtilListModel extends BaseModel {
	private UnitData data;
	
	
	public UnitData getData() {
		return data;
	}

	public void setData(UnitData data) {
		this.data = data;
	}

	public static class UnitData{
		List<Unit> unit=new ArrayList<Unit>();

		public List<Unit> getUnit() {
			return unit;
		}

		public void setUnit(List<Unit> unit) {
			this.unit = unit;
		}
		
	}
	
	public static class Unit{
		private String unitNo;
		private String unitName;
		private String depCount;
		public String getUnitNo() {
			return unitNo;
		}
		public void setUnitNo(String unitNo) {
			this.unitNo = unitNo;
		}
		public String getUnitName() {
			return unitName;
		}
		public void setUnitName(String unitName) {
			this.unitName = unitName;
		}
		public String getDepCount() {
			return depCount;
		}
		public void setDepCount(String depCount) {
			this.depCount = depCount;
		}
	}
}
