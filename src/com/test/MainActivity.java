package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.test.util.PuzzleTools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;


public class MainActivity extends Activity{
	private float density;
	private RelativeLayout father;
	private int[] puzzleBitmapResIdArray; 
	private Bitmap[] puzzleBitmapArray;
	//所有小图的属性
	 private int[][] curTwoDimensionImageAttrData;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	puzzleBitmapResIdArray = new int[]{R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};
    	puzzleBitmapArray = new Bitmap[puzzleBitmapResIdArray.length];
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获得屏幕整个显示区域对象
        Display  display = getWindowManager().getDefaultDisplay();
        //获得显示测量工具
        DisplayMetrics  metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        //获得屏幕的逻辑密度值， 用于屏幕dp 转化为 px
        //dip = (int)(px / density + 0.5f) 
        //px =  (int)(dip * density + 0.5f) 
        density = metrics.density;
        
        //图片集合的最外围布局
        RelativeLayout out = (RelativeLayout) this.findViewById(R.id.allChildFather);
        //图片集合的父类布局   
         father = new RelativeLayout(this);
        LayoutParams params =  new LayoutParams((int)(250 * density), (int)(300 * density));
        
        father.setLayoutParams(params);
        father.setGravity(Gravity.CENTER);
        father.setBackgroundColor(Color.parseColor("#ffffffff"));
        
        //将图片集合的父类布局添加到外围布局中
        out.addView(father, params);
        
        
       GridView gridview =  (GridView) this.findViewById(R.id.gridView);
       int gridviewWidth = (int) (getData().size() * ( (65 + 3) * density));
       int itemWidth = (int) (65* density);
       LinearLayout.LayoutParams gridviewparams = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
       gridview.setLayoutParams(gridviewparams); // 设置GirdView布局参数,横向布局的关键
       gridview.setColumnWidth(itemWidth); // 设置列表项宽
       gridview.setHorizontalSpacing(2); // 设置列表项水平间距
       gridview.setNumColumns(getData().size()); // 设置列数量=列表集合数
       SimpleAdapter  adapter = new SimpleAdapter(this, getData(), R.layout.simple_adapter, new String[]{"img"}, new int[]{R.id.img});
       gridview.setAdapter(adapter);
       gridview.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//计算并重组三维模型中宽度和高度(是否转为当前屏幕下的像素)
			curTwoDimensionImageAttrData = PuzzleTools.getInstance().getDensityPuzzleFormworkAttrDataSet(position, density);
			//获得拼图集合
			PuzzleTools.getInstance().getPuzzleImages(MainActivity.this, position, puzzleBitmapResIdArray, puzzleBitmapArray);
			//修改图片的位置
			modifyPuzzleImageLocation();
		}
	});
       
       
    }
    
    /**
     * 修改图片的位置
     */
    private void modifyPuzzleImageLocation(){
    	//1.删除图片
    	father.removeAllViews();
    	//2.遍历创建ImageView
    	for(int i = 0; i < puzzleBitmapArray.length; i++){
    		int pw = puzzleBitmapArray[i].getWidth();
    		int ph = puzzleBitmapArray[i].getHeight();
    		//创建ImageView
    		ImageView  imageView = new ImageView(this);
    		imageView.setImageBitmap(puzzleBitmapArray[i]); 
    		imageView.setScaleType(ScaleType.CENTER_CROP);
    		
    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pw,ph);
        	params.gravity = Gravity.CENTER;
    		
    		imageView.setLayoutParams(params);
    		
    		
    		//获得每个小图的属性
    		int[] puzzleImageAttr = curTwoDimensionImageAttrData[i];
    		//根据每个小图的属性来创建imageView的父类
    		int layout_id = puzzleImageAttr[6];
    		LinearLayout  linearLayout = new LinearLayout(this);
    		linearLayout.setBackgroundColor(Color.parseColor("#ffffffff"));
    		linearLayout.setGravity(Gravity.CENTER);
    		linearLayout.setId(layout_id);
    		linearLayout.addView(imageView);
    		//根据每个小图的属性来设置imageView的父类在外围父类的位置
    		//1. 由于imageview的外围父类是RelativeLayout,所以这里在设置imageview的父类视图处于某个位置时，需要创建RelativeLayout.LayoutParams
    		RelativeLayout.LayoutParams   params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    		int left = 0;
    		int top = 0;
    		int right = 0;
    		int bottom = 0;
    		
    		if(puzzleImageAttr[8] != -1) {//在某元素的下方  需要第二个参数为 某元素的ID
    			params1.addRule(RelativeLayout.BELOW, puzzleImageAttr[8]);
    			top = puzzleImageAttr[1];
    		}
    		if(puzzleImageAttr[9] != -1) {//在某元素的右边  需要第二个参数为 某元素的ID
    			
    			params1.addRule(RelativeLayout.RIGHT_OF, puzzleImageAttr[9]);
    			left = puzzleImageAttr[0];
    		}
    		if(puzzleImageAttr[10] != -1) {//本元素的上边缘和某元素的的上边缘对齐 需要第二个参数为 某元素的ID
    			params1.addRule(RelativeLayout.ALIGN_TOP, puzzleImageAttr[10]);
    		}
    		if(puzzleImageAttr[11] != -1) {//本元素的上边缘和某元素的的左边缘对齐 需要第二个参数为 某元素的ID
    			params1.addRule(RelativeLayout.ALIGN_LEFT, puzzleImageAttr[11]);
    		}
    		if(puzzleImageAttr[12] != -1) {//紧贴父控件的上边缘
    			params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    			left = puzzleImageAttr[0];
    			top = puzzleImageAttr[1];
    		}
    		params1.setMargins(left, top, right, bottom);
    		
    		father.addView(linearLayout, params1);
    	}
    	
    	//添加动画
    	LinearLayout  ll;
    	AnimationSet as = null;
    	Random  random = new Random();
		for(int i = 0; i < puzzleBitmapArray.length; i++) {
			float fromXDelta = 0;
			float toXDelta = random.nextInt() % 400;
			float fromYDelta = 0;
			float toYDelta = random.nextInt() % 500;
			
			TranslateAnimation trans = new TranslateAnimation(toXDelta,fromXDelta, toYDelta, fromYDelta);
			trans.setDuration(500L);
			
			as = new AnimationSet(false);
			as.addAnimation(trans);
			ll = (LinearLayout)father.getChildAt(i);
			ll.setAnimation(as);
			as.startNow();
		}
		
		
		
    }
    
    

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
 
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("img", R.drawable.threeone);
        list.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("img", R.drawable.threetwo);
        list.add(map2);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("img", R.drawable.threethree);
        list.add(map3);
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("img", R.drawable.threefour);
        list.add(map4);
        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("img", R.drawable.threefive);
        list.add(map5);
        Map<String, Object> map6 = new HashMap<String, Object>();
        map6.put("img", R.drawable.threesix);
        list.add(map6);
        
        return list;
    }

	

}
