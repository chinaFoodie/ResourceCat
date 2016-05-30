package com.cn.clound.view;
 

import android.content.Context;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cn.clound.application.MyApplication;

public class MyFloatView extends ImageView {
	private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    
    
    private WindowManager wm=(WindowManager)getContext().getApplicationContext().getSystemService("window");
    
    //此wmParams为获取的全局变量，用以保存悬浮窗口的属性
    private WindowManager.LayoutParams wmParams = MyApplication.getInstance().getMywmParams();

	public MyFloatView(Context context) {
		super(context);		
		// TODO Auto-generated constructor stub
	}
	
	  @Override
	 public boolean onTouchEvent(MotionEvent event) {
		 
		 
		 //获取相对屏幕的坐标，即以屏幕左上角为原点		 
	     x = event.getRawX();   
	     y = event.getRawY()-25;   //25是系统状态栏的高度
	     switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	        	//获取相对View的坐标，即以此View左上角为原点
	        	mTouchStartX =  event.getX();  
                mTouchStartY =  event.getY();
                
	            break;
	        case MotionEvent.ACTION_MOVE:	            
	            updateViewPosition();
	            break;

	        case MotionEvent.ACTION_UP:
	        	updateViewPosition();
	        	mTouchStartX=mTouchStartY=0;
	        	break;
	        }
	        return true;
		} 
	 
	 private void updateViewPosition(){
		//更新浮动窗口位置参数
		wmParams.x=(int)( x-mTouchStartX);
		wmParams.y=(int) (y-mTouchStartY);
	    wm.updateViewLayout(this, wmParams);
	 }

}
