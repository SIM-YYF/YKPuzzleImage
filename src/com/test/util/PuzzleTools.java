package com.test.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;



/**
 * 
 * @author yuanwenfei
 *
 */
public class PuzzleTools {
	
	private static  PuzzleTools instance;
	private PuzzleTools(){
		
	}
	public static PuzzleTools getInstance(){
		synchronized (PuzzleTools.class) {
			if(null == instance){
				instance = new PuzzleTools();
			}
			return instance;
		}
	}
	
	int[][] threeSquareImage1AttrData = {		
			
			{2, 2, 2, 2, 250, 150, 311, -1, -1, -1, -1, -1, 0, -1},
			{2, 2, 2, 2, 125, 150, 312, -1, 311, -1, -1, 311, -1, -1},
			{2, 2, 2, 2, 125, 150, 313, -1, -1, 312, 312, -1, -1, -1}
			
	};
	int[][] threeSquareImage2AttrData = {
			
			{2, 2, 2, 2, 250, 100, 321, -1, -1, -1, -1, -1, 0, -1},
			{2, 2, 2, 2, 250, 100, 322, -1, 321, -1, -1, 321, -1, -1},
			{2, 2, 2, 2, 250, 100, 323, -1, 322, -1, -1, 322, -1, -1}
			
			
	};
	int[][] threeSquareImage3AttrData = {          
			{2, 2, 2, 2, 125, 150, 331, -1, -1, -1, -1, -1, 0, -1},
			{2, 2, 2, 2, 125, 150, 332, -1, -1, 331, 331, -1, -1, -1},
			{2, 2, 2, 2, 250, 150, 333, -1, 331, -1, -1, 331, -1, -1}
			
	};
	int[][] threeSquareImage4AttrData = {        
			{2, 2, 2, 2, 125, 300, 341, -1, -1, -1, -1, -1, 0, -1},
			{2, 2, 2, 2, 125, 150, 342, -1, -1, 341, 341, -1, -1, -1},
			{2, 2, 2, 2, 125, 150, 343, -1, 342, -1, -1, 342, -1, -1}
			
	};
	int[][] threeSquareImage5AttrData = {
			
			{2, 2, 2, 2, 82, 300, 351, -1, -1, -1, -1, -1, 0, -1},
			{2, 2, 2, 2, 82, 300, 352, -1, -1, 351, 351, -1, -1, -1},
			{2, 2, 2, 2, 82, 300, 353, -1, -1, 352, 352, -1, -1, -1}
			
			
	};
	int[][] threeSquareImage6AttrData = {
			
			{2, 2, 2, 2, 125, 150, 361, -1, -1, -1, -1, -1, 0, -1},
			{2, 2, 2, 2, 125, 150,362, -1, 361, -1, -1, 361, -1, -1 },
			{2, 2, 2, 2, 125, 300, 363, -1, -1, 361, 361, -1, -1, -1}
	};

	/**
	 * 所有三维的拼图模板属性
	 */
	private int[][][] allThreeDimensionPuzzleFormworkAttrDataSet = {
			threeSquareImage1AttrData, 
			threeSquareImage2AttrData, 
			threeSquareImage3AttrData, 
			threeSquareImage4AttrData, 
			threeSquareImage5AttrData, 
			threeSquareImage6AttrData};
	
	/**
	 * 计算并重组三维模型中宽度和高度(是否转为当前屏幕下的像素)
	 * @param index 模板id
	 * @param density 逻辑密度
	 * @return
	 */
	public int[][] getDensityPuzzleFormworkAttrDataSet(int index, float density){
		int[][] densityPuzzleFormworkAttrDataSet = allThreeDimensionPuzzleFormworkAttrDataSet[index];
		for(int i = 0; i < densityPuzzleFormworkAttrDataSet.length; i++){
			int index_13 = densityPuzzleFormworkAttrDataSet[i][13];
			if(density != 1 && index_13 == -1){//每个小图的属相中索引下标为13，来记录是否需要转化为当前屏幕下的像素， -1：需要转化
				for(int j = 0; j < 6; j++){
					densityPuzzleFormworkAttrDataSet[i][j] = (int)(densityPuzzleFormworkAttrDataSet[i][j] * density);
				}
				densityPuzzleFormworkAttrDataSet[i][13] = 0;
			}
		}
		return densityPuzzleFormworkAttrDataSet;
	}
	
	/**
	 * 获得拼图集合
	 * @param formworkId
	 * @param puzzleBitmapResIdArray
	 * @param puzzleBitmapArray
	 */
	
	public void getPuzzleImages(Context context, int formworkId, int[] puzzleBitmapResIdArray, Bitmap[] puzzleBitmapArray){
		//1.获得某个三维模型图片属性
		int[][] oneThreeFormworkAttrDataSet = allThreeDimensionPuzzleFormworkAttrDataSet[formworkId];
		int[] puzzImageAttrDataSet;
		Bitmap bitmap;
		for(int i = 0; i < puzzleBitmapResIdArray.length; i++){
			
			//2.获得三维模型图片属相中，每个小图的属性
			puzzImageAttrDataSet = oneThreeFormworkAttrDataSet[i];
			//3.从指定的文件路径或者resid生成bitmap,这里从puzzleBitmapResIdArray获得图片的id。也可以使用其他方式进行取得，例如Uri = content:///   或者  file:/// 
			Bitmap  sourceBitmap = loadSourceImage(context, puzzleBitmapResIdArray[i], 1280, 1280);
			//4.根据上面生成的图片和三维模型图中的小图属性，进行图片缩放。生成能够进行拼图的资源
			bitmap = scaleImage(sourceBitmap, puzzImageAttrDataSet);
			puzzleBitmapArray[i] = sourceBitmap;
		}
	}
	
	/**
	 * 根据小图属性对原图进行等比缩放
	 * @param bitmap
	 * @param puzzImageAttrDataSet
	 * @return
	 */
	private Bitmap scaleImage(Bitmap bitmap, int[] puzzImageAttrDataSet) {
		// TODO Auto-generated method stub
		float w = bitmap.getWidth();//原图的宽度
		float h = bitmap.getHeight();//原图的高度
		
		float pw = puzzImageAttrDataSet[4];//拼图宽度
		float ph = puzzImageAttrDataSet[5];//拼图高度
		
		float sw = pw / w;
		float sh = ph / h;
		
		
		Matrix  matrix = new Matrix();
		matrix.setScale(sw, sh);
		
		Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int)w, (int)h, matrix, true);
		
		bitmap.recycle();
		bitmap = null;
		
		return scaleBitmap;
	}
	/**
	 * 加载图片
	 * @param resid  图片的资源id
	 * @param critical_width： 临界宽度
	 * @param critical_height：临界高度
	 * @return
	 */
	private Bitmap loadSourceImage(Context context, int resid, int req_width, int req_height){
		//1.根据resid生成缩略图
		Bitmap  srcBitmap = null;
		BitmapFactory.Options  options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//生成缩略图
		
		BitmapFactory.decodeResource(context.getResources(), resid, options);
		
		int ow = options.outWidth;
		int oh = options.outHeight;
		
		//2.如果图片的缩略图的宽度和高度大于临界值，则进行图片压缩,否则的话直接生成原图，然后返回
		if(ow <= req_width && oh <= req_height){//小于临界值
			options.inJustDecodeBounds = false;//生成原图
			options.inSampleSize = 1;//不进行压缩，原图生成
			srcBitmap = BitmapFactory.decodeResource(context.getResources(), resid, options);
			//记得回收缩略图
			return srcBitmap;
		}else{//图片过大，进行图片压缩
			//下面是压缩简单算法
			//获得原图的宽与高
			float width = options.outWidth;
			float height = options.outHeight;

			//第一种方案：
			
			//分别计算图片的实际宽高和目标宽高的比率
			float widthRatio = (width / req_width);
			float heightRatio = (height / req_height);
			//选择宽和高中最小的比率
			float minRatio = widthRatio > heightRatio ? heightRatio : widthRatio;
			
			
		//3.根据生成目标bitmap
			options.inSampleSize = (int)minRatio;
			options.inJustDecodeBounds = false;
			srcBitmap = BitmapFactory.decodeResource(context.getResources(), resid, options);
			
//			srcBitmap = getImageThumbnail(context, resid, 800);
			
			return srcBitmap;
	}
	}
	
	
	public static Bitmap getImageThumbnail(Context context, int  resid, int minLen) {  
	    int width = 0;
	    int height = 0;
	    Bitmap bitmap = null;  
	    BitmapFactory.Options options = new BitmapFactory.Options();
	     
	    options.inJustDecodeBounds = true;  
	    bitmap = BitmapFactory.decodeResource(context.getResources(),resid, options);  
	    options.inJustDecodeBounds = false; 
	     
	    if(options.outHeight > options.outWidth) {
	        width = minLen;
	        options.inSampleSize = options.outWidth / width;
	        height = options.outHeight * width / options.outWidth;
	        options.outHeight = height;
	        options.outWidth = width;
	    }
	    else {
	        height = minLen;
	        options.inSampleSize = options.outHeight / height;
	        width = options.outWidth * height / options.outHeight;
	        options.outWidth = width;
	        options.outHeight = height;
	    }
	 
	    options.inPreferredConfig = Bitmap.Config.ARGB_4444;
	     
	    bitmap = BitmapFactory.decodeResource(context.getResources(),resid, options);  
	    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
	            ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
	    return bitmap;  
	}
	
	
}
