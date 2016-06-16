package com.cn.clound.base.common.gallery;

/**
 * GridView的每个item的数据对象
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年6月16日 08:59:22
 */
public class ImageBean {
    private String topImagePath;
    private String folderName;
    private int imageCounts;

    public String getTopImagePath() {
        return topImagePath;
    }

    public void setTopImagePath(String topImagePath) {
        this.topImagePath = topImagePath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getImageCounts() {
        return imageCounts;
    }

    public void setImageCounts(int imageCounts) {
        this.imageCounts = imageCounts;
    }
}
