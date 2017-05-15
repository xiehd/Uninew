package com.qr.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

public class AnimationTools {

	/**
	 * 缩放动画
	 * 
	 * @param view
	 */
	public static void StartSetAnimation(View view,float fromX, float toX, float fromY, float toY) {
		AnimationSet animationSet = new AnimationSet(true);
		/*
		 * 参数解释： 第一个参数：X轴水平缩放起始位置的大小（fromX）。1代表正常大小
		 * 第二个参数：X轴水平缩放完了之后（toX）的大小，0代表完全消失了 第三个参数：Y轴垂直缩放起始时的大小（fromY）
		 * 第四个参数：Y轴垂直缩放结束后的大小（toY） 第五个参数：pivotXType为动画在X轴相对于物件位置类型
		 * 第六个参数：pivotXValue为动画相对于物件的X坐标的开始位置 第七个参数：pivotXType为动画在Y轴相对于物件位置类型
		 * 第八个参数：pivotYValue为动画相对于物件的Y坐标的开始位置
		 * 
		 * （第五个参数，第六个参数），（第七个参数,第八个参数）是用来指定缩放的中心点 0.5f代表从中心缩放
		 */
//		ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
		ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 3秒完成动画
		scaleAnimation.setDuration(300);
		// 将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
		animationSet.addAnimation(scaleAnimation);
		// 启动动画
		view.startAnimation(animationSet);
	}
}
