package com.xytsz.xytaj.ui;

/**
 * Created by admin on 2017/1/11.
 *
 */
public class SwipeLayoutManager {

	private static SwipeLayoutManager manager=new SwipeLayoutManager();
	private SwipeLayoutManager(){

	}
	public static SwipeLayoutManager getInstance(){
		return manager;
	}
	private Swipelayout swipeLayout;
	//记住被选择的条目
	public void setSwipeLayout(Swipelayout swipeLayout){
		this.swipeLayout=swipeLayout;
	}

	//获取被记录的条目
	public Swipelayout getSwipeLayout(){
			return swipeLayout;
	}

	//清除被记录的条目
	public void clearSwipeLayout() {
		swipeLayout=null;
	}
}
