package com.cn.clound.bean.share;

public class ShareModel {
	private ShareData data;
	
	
	public ShareData getData() {
		return data;
	}
	public void setData(ShareData data) {
		this.data = data;
	}
	
	public static class ShareData{
		private Share share;

		public Share getShare() {
			return share;
		}

		public void setShare(Share share) {
			this.share = share;
		}
	}
	public static class Share{
		private String title;
		private String content;
		private String url;
		private String image;
		private String appName;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getAppName() {
			return appName;
		}
		public void setAppName(String appName) {
			this.appName = appName;
		}
		
	}
}
