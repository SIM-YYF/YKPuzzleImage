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
	//����Сͼ������
	 private int[][] curTwoDimensionImageAttrData;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	puzzleBitmapResIdArray = new int[]{R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};
    	puzzleBitmapArray = new Bitmap[puzzleBitmapResIdArray.length];
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //�����Ļ������ʾ�������
        Display  display = getWindowManager().getDefaultDisplay();
        //�����ʾ��������
        DisplayMetrics  metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        //�����Ļ���߼��ܶ�ֵ�� ������Ļdp ת��Ϊ px
        //dip = (int)(px / density + 0.5f) 
        //px =  (int)(dip * density + 0.5f) 
        density = metrics.density;
        
        //ͼƬ���ϵ�����Χ����
        RelativeLayout out = (RelativeLayout) this.findViewById(R.id.allChildFather);
        //ͼƬ���ϵĸ��಼��   
         father = new RelativeLayout(this);
        LayoutParams params =  new LayoutParams((int)(250 * density), (int)(300 * density));
        
        father.setLayoutParams(params);
        father.setGravity(Gravity.CENTER);
        father.setBackgroundColor(Color.parseColor("#ffffffff"));
        
        //��ͼƬ���ϵĸ��಼����ӵ���Χ������
        out.addView(father, params);
        
        
       GridView gridview =  (GridView) this.findViewById(R.id.gridView);
       int gridviewWidth = (int) (getData().size() * ( (65 + 3) * density));
       int itemWidth = (int) (65* density);
       LinearLayout.LayoutParams gridviewparams = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
       gridview.setLayoutParams(gridviewparams); // ����GirdView���ֲ���,���򲼾ֵĹؼ�
       gridview.setColumnWidth(itemWidth); // �����б����
       gridview.setHorizontalSpacing(2); // �����б���ˮƽ���
       gridview.setNumColumns(getData().size()); // ����������=�б�����
       SimpleAdapter  adapter = new SimpleAdapter(this, getData(), R.layout.simple_adapter, new String[]{"img"}, new int[]{R.id.img});
       gridview.setAdapter(adapter);
       gridview.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//���㲢������άģ���п�Ⱥ͸߶�(�Ƿ�תΪ��ǰ��Ļ�µ�����)
			curTwoDimensionImageAttrData = PuzzleTools.getInstance().getDensityPuzzleFormworkAttrDataSet(position, density);
			//���ƴͼ����
			PuzzleTools.getInstance().getPuzzleImages(MainActivity.this, position, puzzleBitmapResIdArray, puzzleBitmapArray);
			//�޸�ͼƬ��λ��
			modifyPuzzleImageLocation();
		}
	});
       
       
    }
    
    /**
     * �޸�ͼƬ��λ��
     */
    private void modifyPuzzleImageLocation(){
    	//1.ɾ��ͼƬ
    	father.removeAllViews();
    	//2.��������ImageView
    	for(int i = 0; i < puzzleBitmapArray.length; i++){
    		int pw = puzzleBitmapArray[i].getWidth();
    		int ph = puzzleBitmapArray[i].getHeight();
    		//����ImageView
    		ImageView  imageView = new ImageView(this);
    		imageView.setImageBitmap(puzzleBitmapArray[i]); 
    		imageView.setScaleType(ScaleType.CENTER_CROP);
    		
    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pw,ph);
        	params.gravity = Gravity.CENTER;
    		
    		imageView.setLayoutParams(params);
    		
    		
    		//���ÿ��Сͼ������
    		int[] puzzleImageAttr = curTwoDimensionImageAttrData[i];
    		//����ÿ��Сͼ������������imageView�ĸ���
    		int layout_id = puzzleImageAttr[6];
    		LinearLayout  linearLayout = new LinearLayout(this);
    		linearLayout.setBackgroundColor(Color.parseColor("#ffffffff"));
    		linearLayout.setGravity(Gravity.CENTER);
    		linearLayout.setId(layout_id);
    		linearLayout.addView(imageView);
    		//����ÿ��Сͼ������������imageView�ĸ�������Χ�����λ��
    		//1. ����imageview����Χ������RelativeLayout,��������������imageview�ĸ�����ͼ����ĳ��λ��ʱ����Ҫ����RelativeLayout.LayoutParams
    		RelativeLayout.LayoutParams   params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    		int left = 0;
    		int top = 0;
    		int right = 0;
    		int bottom = 0;
    		
    		if(puzzleImageAttr[8] != -1) {//��ĳԪ�ص��·�  ��Ҫ�ڶ�������Ϊ ĳԪ�ص�ID
    			params1.addRule(RelativeLayout.BELOW, puzzleImageAttr[8]);
    			top = puzzleImageAttr[1];
    		}
    		if(puzzleImageAttr[9] != -1) {//��ĳԪ�ص��ұ�  ��Ҫ�ڶ�������Ϊ ĳԪ�ص�ID
    			
    			params1.addRule(RelativeLayout.RIGHT_OF, puzzleImageAttr[9]);
    			left = puzzleImageAttr[0];
    		}
    		if(puzzleImageAttr[10] != -1) {//��Ԫ�ص��ϱ�Ե��ĳԪ�صĵ��ϱ�Ե���� ��Ҫ�ڶ�������Ϊ ĳԪ�ص�ID
    			params1.addRule(RelativeLayout.ALIGN_TOP, puzzleImageAttr[10]);
    		}
    		if(puzzleImageAttr[11] != -1) {//��Ԫ�ص��ϱ�Ե��ĳԪ�صĵ����Ե���� ��Ҫ�ڶ�������Ϊ ĳԪ�ص�ID
    			params1.addRule(RelativeLayout.ALIGN_LEFT, puzzleImageAttr[11]);
    		}
    		if(puzzleImageAttr[12] != -1) {//�������ؼ����ϱ�Ե
    			params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    			left = puzzleImageAttr[0];
    			top = puzzleImageAttr[1];
    		}
    		params1.setMargins(left, top, right, bottom);
    		
    		father.addView(linearLayout, params1);
    	}
    	
    	//��Ӷ���
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
