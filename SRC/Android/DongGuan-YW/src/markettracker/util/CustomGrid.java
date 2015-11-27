package markettracker.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import markettracker.util.Constants.ControlType;
import markettracker.util.Constants.AlertType;
import markettracker.data.Fields;
import markettracker.data.FieldsList;
import markettracker.data.Sqlite;
import markettracker.data.UIItem;
import orient.champion.business.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

@SuppressLint("ResourceAsColor")
public class CustomGrid extends View implements OnGestureListener
{
	
	private Rect mRect = new Rect();
	private Paint mPaint = new Paint();
	
	private Handler mHandler;
	private Hashtable<String, Fields> mHashtable;
	
	private int mOffsetX;
	private int mOffsetY;
	
	/**
	 * 当前手指触摸的坐标
	 */
	private int mCurrX;
	private int mCurrY;
	
	/** 当前选中第几列 */
	private int mSelectedX = -1;
	/** 当前选中第几行 */
	private int mSelectedY = -1;
	
	private EditText mEditText;
	private AlertDialog mEditDialog;
	private AlertDialog mSingleChoiceDialog;
	
	private AlertDialog mMultiChoiceDialog;
	private NumericKeyboard mNumericKeyboard;
	
	private GestureDetector mGestureDetector;
	
	/** 整个表格全部内容的宽度 */
	private int mTotalWidth;
	/** 整个表格全部内容的高度 */
	private int mTotalHeight;
	
	/** 所有锁定列的总宽度 */
	private int mTotalLockedColumnWidth;
	
	/** 行的高度 */
	private int mCellHeight = Tool.dip2px(getContext(), 40);
	
	/** 每一列的宽度 */
	private int[] mColumnWidth;
	/** 辅助作用，用来计算选中哪一列，用来保存每一列距表格最左边的距离 */
	private int[] mCellsWidth;
	
	/** 锁定的列数，从第1列开始 */
	private int mLockedColumnsCount = 1;
	
	// private static float mStrokeWidth = 0;// (float) 0.5
	private static float mWholeStrokeWidth = 0;
	
	/** 列的样式 */
	private ColumnStyle[] mColumnStyles;
	
	/** 数据源，根据此数据源来绘制表格 */
	// private String[][] mDataSource;
	
	private List<UIItem> mList;
	
	private int mCustomGridType = Constants.CustomGridType.OTHER;
	
	private FieldsList dataList;
	private Fields mSelectedData;
	
	private int mTotalRows;
	private int mTotalColums;
	private String mTemplateType;
	
	/** 行的颜色背景 */
	
	/** 表格title的背景 */
	private Drawable mTitleBackground;
	private Drawable mSelectBackground;
	
	/*
	 * 表格内容的背景，隔行交错显示
	 */
	// private Drawable mContentBackground1;
	private Drawable mContentBackground2;
	
	private boolean mIsLongPressed = false;
	private boolean mIsSingleTapUp = false;
	
	private boolean mInitialized = false;
	
	private Bitmap mUnSelectedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radio_bt_unselected);
	private Bitmap mSelectedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radio_bt_selected);
	private Bitmap cameraBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera2);
	
	public CustomGrid(Context context)
	{
		super(context);
	}
	
	public int getViewWidth()
	{
		return getWidth();
	}
	
	public CustomGrid(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	private float getWholeStrokeWidth()
	{
		return mWholeStrokeWidth;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private void drawText(Canvas canvas, String str, String isKeyStone, String isStarProduct, int left, int top, int column, int row)
	{
		ColumnStyle style = mColumnStyles[column];
		mPaint.setTextSize(style.getTextSize());
		mPaint.setTypeface(Typeface.DEFAULT);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		Paint.Align align = style.getAlign();
		if (row == 0)
			align = Paint.Align.CENTER;
		else
			align = style.getAlign();
		float textLen = mPaint.measureText(str);
		float l = left;
		if (align == Paint.Align.LEFT)
		{
			l += 5;
		}
		else if (align == Paint.Align.CENTER)
		{
			float deltaX = 0;
			if (style.getControlType() == ControlType.SELECTED && row != 0)
				deltaX = (mColumnWidth[column] - mSelectedBitmap.getWidth()) / 2;
			else
				deltaX = (mColumnWidth[column] - textLen) / 2;
			l += deltaX;
		}
		else if (align == Paint.Align.RIGHT)
		{
			float deltaX = mColumnWidth[column] - textLen - 2;
			l += deltaX;
		}
		FontMetrics fw = mPaint.getFontMetrics();
		mPaint.setStrokeWidth(0);
		if (row == 0 || (row != 0 && mSelectedY == row))
		{
			mPaint.setColor(Color.BLACK);
		}else if(column == 1){
			if("1".equals(isKeyStone)){	//必备产品
				mPaint.setColor(Color.BLUE);
			}else if("1".equals(isStarProduct)){	//主推产品
				mPaint.setColor(Color.RED);
			}
		}
		
		if (row == 0){
			canvas.drawText(str, l, top + mCellHeight / 2 + fw.bottom, mPaint);
		}else{
			if (style.getControlType() == ControlType.SELECTED)
			{
				if (str.equals("1"))
					canvas.drawBitmap(mSelectedBitmap, l, top + mCellHeight / 4, mPaint);
				else
					canvas.drawBitmap(mUnSelectedBitmap, l, top + mCellHeight / 4, mPaint);
			}
			else if (style.getControlType() == ControlType.PICKPHOTO)
			{
				// if (dataList.getFields(row -
				// 1).getStrValue("IsPhoto").equals("1"))
				// {
//				getProductPhoto(row);
				if (str.equals(""))
					canvas.drawBitmap(cameraBitmap, l - cameraBitmap.getWidth() / 2, top + (mCellHeight - cameraBitmap.getHeight()) / 2, mPaint);
				else 
					canvas.drawText(str, l, top + mCellHeight / 2 + fw.bottom, mPaint);
				// }
			}
			else
				canvas.drawText(str, l, top + mCellHeight / 2 + fw.bottom, mPaint);
		}
	}
	
	public int GetCurrentIndex()
	{
		return mSelectedY;
	}
	
	private void drawRect(Canvas canvas, Rect rect, int column, int row)
	{
		// mPaint.setStyle(Paint.Style.STROKE);
		// mPaint.setStrokeWidth(getStrokeWidth());
		if (row != 0 && mSelectedX == column && mSelectedY == row)
		{// 当前选中的单元格，设置为不同的颜色，或者
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			// mPaint.setColor(Color.rgb(255, 109, 0));
			mPaint.setColor(getSelectRectColor());
			// mRect.left=mRect.left+1;
			mRect.right = mRect.right - 1;
			// mRect.top=mRect.top+1;
			mRect.bottom = mRect.bottom - 1;
			canvas.drawRect(mRect, mPaint);
			
			mRect.left = mRect.left - 1;
			mRect.right = mRect.right + 1;
			mRect.top = mRect.top - 1;
			mRect.bottom = mRect.bottom + 1;
			mPaint.setStyle(Paint.Style.STROKE);
			// mPaint.setStrokeWidth(0);
			mPaint.setColor(R.color.background);
			canvas.drawRect(mRect, mPaint);
		}
		else
		{
			
			mRect.left = mRect.left - 1;
			// mRect.right=mRect.right+1;
			mRect.top = mRect.top - 1;
			// mRect.bottom=mRect.bottom+1;
			mPaint.setStyle(Paint.Style.STROKE);
			// mPaint.setStrokeWidth(1);
			mPaint.setColor(R.color.background);
			canvas.drawRect(mRect, mPaint);
			
		}
	}
	
	//
	private int getSelectRectColor()
	{
		return Color.rgb(255, 109, 0);
	}
	
	// mSelectedY * mCellHeight,(mSelectedY + 1) * mCellHeight
	private void drawSelectBackground(Canvas canvas)
	{
		if (mSelectedY < mTotalRows && mSelectedY > 0)
		{
			int top = mCellHeight * mSelectedY - mOffsetY;
			getSelectBackground().setBounds(0, top, getWidth(), top + mCellHeight);
			getSelectBackground().draw(canvas);
		}
	}
	
	private void drawBackground(Canvas canvas)
	{
		int i = mOffsetY / mCellHeight + 1;
		int j = mOffsetY % mCellHeight;
		int n = getHeight() / mCellHeight + 1;
		if (n > mTotalRows)
			n = mTotalRows;
		int top;
		for (int m = 1; m < n; m++)
		{
			top = mCellHeight * m - j;
			if (i % 2 == 1)
			{
				// int top = mCellHeight * m - j;
				// getContentBackground1().setBounds(0, top, getWidth(),
				// top + mCellHeight);
				// getContentBackground1().draw(canvas);
			}
			else
			{
				getContentBackground2().setBounds(0, top, getWidth(), top + mCellHeight);
				getContentBackground2().draw(canvas);
			}
			i++;
		}
		getTitleBackground().setBounds(0, 0, getWidth(), mCellHeight);
		getTitleBackground().draw(canvas);
	}
	
	public String getResult(int iRow, int iColumn)
	{
		if ((dataList == null || dataList.size() <= 0) && iRow > 0)
			return "";
		if (iRow == 0)
			return getCurrUIItem(iColumn).getCaption();
		else
		{
			if (iRow > dataList.size())
				return "";
			else
			{
				return (dataList.getFields(iRow - 1)).getStrValue(getCurrUIItem(iColumn).getDataKey());
			}
		}
	}

	/**
	 * 得到必备产品
	 * @param iRow
	 * @param iColumn
	 * @return
	 */
	public String getIsKeyStone(int iRow, int iColumn)
	{
		if ((dataList == null || dataList.size() <= 0) && iRow > 0)
			return "";
		if (iRow == 0)
			return getCurrUIItem(iColumn).getCaption();
		else
		{
			if (iRow > dataList.size())
				return "";
			else
			{
				return (dataList.getFields(iRow - 1)).getStrValue("iskeystone");
			}
		}
	}

	/**
	 * 得到主推产品
	 * @param iRow
	 * @param iColumn
	 * @return
	 */
	public String getIsStarProduct(int iRow, int iColumn)
	{
		if ((dataList == null || dataList.size() <= 0) && iRow > 0)
			return "";
		if (iRow == 0)
			return getCurrUIItem(iColumn).getCaption();
		else
		{
			if (iRow > dataList.size())
				return "";
			else
			{
				return (dataList.getFields(iRow - 1)).getStrValue("isstarproduct");
			}
		}
	}
	
	
	
	// private String getResultByKey(int iRow, String key) {
	// if ((mDataList == null || mDataList.size() <= 0) && iRow > 0)
	// return "";
	// if (iRow == 0)
	// return "";
	// else {
	// if (iRow > mDataList.size())
	// return "";
	// else {
	// return (mDataList.get(iRow - 1)).getStringData(key);
	// }
	// }
	// }
	
	private UIItem getCurrUIItem(int iColumn)
	{
		return mList.get(iColumn);
	}
	
	private void setData(int iRow, int iColumn, String strValue)
	{
		try
		{
			if (iRow > 0)
			{
				if (iRow <= dataList.size())
				{
					final Fields data = dataList.getFields(iRow - 1);
					data.put(getCurrUIItem(iColumn).getDataKey(), strValue);
					if (getCurrUIItem(iColumn).getValidate() == 1)
					{
						if (mHashtable.containsKey(data.getStrValue("ServerId")))
							mHashtable.remove(data.getStrValue("ServerId"));
						ColumnStyle style = mColumnStyles[iColumn];
						ControlType type = style.getControlType();
						if (type == ControlType.SELECTED)
						{
							if (strValue.equals("1"))
								mHashtable.put(data.getStrValue("ServerId"), data);
						}
						else
							mHashtable.put(data.getStrValue("ServerId"), data);
					}
				}
			}
		}
		catch (Exception ex)
		{
			
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (!mInitialized)
			return;
		
		if (mTotalWidth <= getWidth())
			mOffsetX = 0;
		else
		{
			int w = mTotalWidth - getWidth();
			if (mOffsetX > w)
				mOffsetX = w;
		}
		if (mTotalHeight <= getHeight())
			mOffsetY = 0;
		else
		{
			if (mOffsetY > mTotalHeight - getHeight())
				mOffsetY = mTotalHeight - getHeight();
		}
		
		drawBackground(canvas);
		
		drawSelectBackground(canvas);
		
		// 对于内容可以进行移动
		if (mLockedColumnsCount < mColumnWidth.length)
		{
			canvas.save();
			mRect.set(mCellsWidth[mLockedColumnsCount], mCellHeight, mTotalWidth, mTotalHeight);
			canvas.clipRect(mRect);
			
			canvas.translate(-mOffsetX, -mOffsetY);
			for (int i = 1; i < mTotalRows; i++)
			{
				
				// 表格看不见的地方，则不draw，提高性能
				if (Math.abs(mOffsetY) > mCellHeight * i)
					continue;
				
				for (int j = mLockedColumnsCount; j < mColumnWidth.length; j++)
				{
					int left = mCellsWidth[j];
					int top = i * mCellHeight;
					
					mRect.set(left, top, left + mColumnWidth[j], top + mCellHeight);
					canvas.save();
					canvas.clipRect(mRect);
					
					drawRect(canvas, mRect, j, i);
					
					// String str = getResult(i, j); // mDataSource[i][j];
					drawText(canvas, getResult(i, j), getIsKeyStone(i, j), getIsStarProduct(i, j), left, top, j, i);
					
					canvas.restore();
					
					if (mCellsWidth[j] + mColumnWidth[j] > getWidth() + mOffsetX)
						break;
				}
				
				// 表格看不见的地方，则不draw，提高性能
				if (mCellHeight * (i + 1) > getHeight() + mOffsetY)
					break;
			}
			canvas.restore();
		}
		
		for (int i = 0; i < mLockedColumnsCount; i++)
		{
			int left = mCellsWidth[i];
			mRect.set(left, 0, left + mColumnWidth[i], mCellHeight);
			canvas.save();
			canvas.clipRect(mRect);
			
			drawRect(canvas, mRect, i, 0);
			
			drawText(canvas, getResult(0, i), getIsKeyStone(0, i), getIsStarProduct(0, i), left, 0, i, 0);
			
			canvas.restore();
		}
		
		// 对于最上面的title只能左右移动
		canvas.save();
		mRect.set(mCellsWidth[mLockedColumnsCount], 0, mTotalWidth, mCellHeight);
		canvas.clipRect(mRect);
		canvas.translate(-mOffsetX, 0);
		
		for (int i = mLockedColumnsCount; i < mTotalColums; i++)
		{
			int left = mCellsWidth[i];
			int top = 0;
			mRect.set(left, top, left + mColumnWidth[i], top + mCellHeight);
			canvas.save();
			canvas.clipRect(mRect);
			
			drawRect(canvas, mRect, i, 0);
			
			// String str = getResult(0, i);// mDataSource[0][i];
			drawText(canvas, getResult(0, i), getIsKeyStone(0, i), getIsStarProduct(0, i), left, top, i, 0);
			
			canvas.restore();
			
			// 表格看不见的地方，则不draw，提高性能
			if (mCellsWidth[i] + mColumnWidth[i] > getWidth() + mOffsetX)
				break;
		}
		canvas.restore();
		
		// 对于左边锁定的列只能上下移动
		if (mLockedColumnsCount > 0)
		{
			canvas.save();
			mRect.set(0, mCellHeight, mCellsWidth[mLockedColumnsCount], mTotalHeight);
			canvas.clipRect(mRect);
			canvas.translate(0, -mOffsetY);
			for (int i = 1; i < mTotalRows; i++)
			{
				// 表格看不见的地方，则不draw，提高性能
				if (Math.abs(mOffsetY) > mCellHeight * i)
					continue;
				
				for (int j = 0; j < mLockedColumnsCount; j++)
				{
					int left = mCellsWidth[j];
					int top = i * mCellHeight;
					mRect.set(left, top, left + mColumnWidth[j], top + mCellHeight);
					canvas.save();
					canvas.clipRect(mRect);
					
					drawRect(canvas, mRect, j, i);
					
					// String str = getResult(i, j);// mDataSource[i][j];
					drawText(canvas, getResult(i, j), getIsKeyStone(i, j), getIsStarProduct(i, j), left, top, j, i);
					canvas.restore();
				}
				// 表格看不见的地方，则不draw，提高性能
				if (mCellHeight * (i + 1) > getHeight() + mOffsetY)
					break;
			}
			canvas.restore();
		}
		
		mPaint.setStrokeWidth(getWholeStrokeWidth());
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(R.color.background);
		// canvas.drawLine(0, 0, 0, getHeight(), mPaint);
		// canvas.drawLine(0, 0, getWidth(), 0, mPaint);
		// canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), mPaint);
		// canvas.drawLine(0, getHeight(), getWidth(), getHeight(), mPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (!mInitialized)
			return true;
		if (!mIsLongPressed && !mIsSingleTapUp)
		{
			mSelectedX = -1;
			mSelectedY = -1;
		}
		boolean consumed = mGestureDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP && !mIsLongPressed && !mIsSingleTapUp)
		{ // 进行行列位置的微调
			if (mTotalWidth > getWidth() && mTotalWidth - getWidth() == mOffsetX)
			{
				// 显示最右边一列，不进行x轴坐标纠正
			}
			else
			{
				// 校准X轴上的偏移
				int tmpW = 0;
				int tmpI = 0;
				for (int i = mLockedColumnsCount; i < mCellsWidth.length; i++)
				{
					if (mOffsetX > tmpW)
					{
						tmpW += mColumnWidth[i];
						tmpI = i;
						continue;
					}
					else
					{
						break;
					}
				}
				if (tmpW - mOffsetX > mColumnWidth[tmpI] / 2)
				{
					mOffsetX = tmpW - mColumnWidth[tmpI];
				}
				else
				{
					mOffsetX = tmpW;
				}
			}
			
			// 校准y轴上的偏移
			int y = mOffsetY % mCellHeight;
			if (y < mCellHeight / 2)
				mOffsetY -= y;
			else
			{
				mOffsetY += (mCellHeight - y);
			}
			
			invalidate();
		}
		return consumed;
	}
	
	public boolean onDown(MotionEvent event)
	{
		System.out.println("onDown()");
		
		mIsLongPressed = false;
		mIsSingleTapUp = false;
		mCurrX = (int) event.getX();
		mCurrY = (int) event.getY();
		return true;
	}
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		System.out.println("onFling()");
		return false;
	}
	
	public void onLongPress(MotionEvent e)
	{
		
		mCurrX = (int) e.getX();
		mCurrY = (int) e.getY();
		// 设置选中某一个单元格
		cacSelectedRowAndColumn(mCurrX, mCurrY);
		invalidate();
		mIsLongPressed = true;
		if (mSelectedY != -1 && mSelectedY < mTotalRows)
		{
			if (getCustomGridType() == Constants.CustomGridType.MAINMENU)
			{
				// cacSelectedRowAndColumn(mCurrX, mCurrY);
				getLinkHandler().sendMessage(getLinkHandler().obtainMessage(Constants.CustomGridType.MAINMENU, "数据同步失败，请重试！"));
			}
			else if (getCustomGridType() == Constants.CustomGridType.MESSAGE)
				getLinkHandler().sendMessage(getLinkHandler().obtainMessage(Constants.CustomGridType.MESSAGE, "数据同步失败，请重试！"));
			// showStartDialog(mSelectedX, mSelectedY);
			else
			{
				Tool.showToastMsg(getContext(), getResult(mSelectedY, mSelectedX), AlertType.DEFAULT);
				
			}
		}
	}
	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		System.out.println("onScroll()");
		if (!mIsLongPressed && !mIsSingleTapUp)
		{
			mSelectedX = -1;
			mSelectedY = -1;
		}
		float x = e2.getX();
		float y = e2.getY();
		int offsetX = (int) (mCurrX - x);
		int offsetY = (int) (mCurrY - y);
		int flag = -1;
		if (Math.abs(distanceX) < Math.abs(distanceY))
			flag = 0;
		else
			flag = 1;
		if (flag == 0)
		{// 如果滑动手势，上下的位移比左右的位移大，则只上下滑动
			mOffsetY += offsetY;
		}
		else if (flag == 1)
		{// 如果左右的位移比上下的位移大，则只左右滑动
			mOffsetX += offsetX;
		}
		else
		{
			mOffsetX += offsetX;
			mOffsetY += offsetY;
		}
		
		if (mOffsetX < 0)
			mOffsetX = 0;
		if (mOffsetY < 0)
			mOffsetY = 0;
		mCurrX = (int) x;
		mCurrY = (int) y;
		invalidate();
		return false;
	}
	
	// @Override
	public void onShowPress(MotionEvent e)
	{
		System.out.println("onShowPress()");
	}
	
	// @Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		System.out.println("onSingleTapUp()");
		
		mCurrX = (int) e.getX();
		mCurrY = (int) e.getY();
		// 设置选中某一个单元格
		cacSelectedRowAndColumn(mCurrX, mCurrY);
		invalidate();
		
		mIsSingleTapUp = true;
		
		if (getCustomGridType() == Constants.CustomGridType.ADDHOC)
			getLinkHandler().sendMessage(getLinkHandler().obtainMessage(Constants.CustomGridType.ADDHOC, "数据同步失败，请重试！"));
		else
		{
			ColumnStyle style = mColumnStyles[mSelectedX];
			ControlType type = style.getControlType();
			String str = getResult(mSelectedY, mSelectedX);// mDataSource[mSelectedY][mSelectedX];
			if (mNumericKeyboard != null)
				mNumericKeyboard.setVisibility(View.GONE, null);
			// 如果选中第一行title，则不响应
			if (mSelectedY == 0 || mSelectedY >= mTotalRows)
			{
				return true;
			}
			if (type == ControlType.NONE)
			{
				// if (mTemplateType == Constants.CustomGridType.ADDHOC)
				// mHandler.sendMessage(mHandler.obtainMessage(
				// Constants.CustomGridType.ADDHOC, "数据同步失败，请重试！"));
				// else if (mTemplateType == Constants.CustomGridType.MESSAGE)
				// mHandler.sendMessage(mHandler.obtainMessage(
				// Constants.CustomGridType.MESSAGE, "数据同步失败，请重试！"));
				// // showStartDialog(mSelectedX, mSelectedY);
				// else
				// if (!str.equals(""))
				// Toast.makeText(getContext(), str, Toast.LENGTH_SHORT)
				// .show();
			}
			else if (type == ControlType.TEXT)
			{ // 文本编辑
				buildEditDialog();
				mEditText.setText(str);
				mEditText.setSelection(0, null != str ? str.length() : 0);
				mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
				mEditText.setTag("0");
				mEditDialog.show();
			}
			else if (type == ControlType.MULTICHOICE)
			{ // 多选
				showMultiChoiceDialog(mSelectedX, mSelectedY);
			}
			else if (type == ControlType.SINGLECHOICE)
			{ // 单选
				showSingleChoiceDialog(mSelectedX, mSelectedY);
			}
			else if (type == ControlType.SELECTED)
			{ // 单选
				if (getResult(mSelectedY, mSelectedX).equals("0") || getResult(mSelectedY, mSelectedX).equals(""))
					setData(mSelectedY, mSelectedX, "1");
				else
					setData(mSelectedY, mSelectedX, "0");
			}
			
			else if (type == ControlType.PICKPHOTO)
			{ // 单选
				getLinkHandler().sendMessage(getLinkHandler().obtainMessage(Constants.CustomGridType.PICKPHOTO, getSelectData()));
			}
			else if (type == ControlType.PHONE_EDIT)
			{ // 电话
				buildEditDialog();
				mEditText.setText(str);
				mEditText.setSelection(0, null != str ? str.length() : 0);
				mEditText.setInputType(InputType.TYPE_CLASS_PHONE);
				mEditText.setTag("0");
				mEditDialog.show();
			}
			else if (type == ControlType.EMAIL_EDIT)
			{ // Email
				buildEditDialog();
				mEditText.setText(str);
				mEditText.setSelection(0, null != str ? str.length() : 0);
				mEditText.setTag("1");
				mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
				mEditDialog.show();
			}
			else if (type == ControlType.LINK)
			{ // 链接
				if (str.startsWith("http://") || str.startsWith("www."))
				{
					Uri uri = Uri.parse(str);
					Intent i = new Intent(Intent.ACTION_VIEW, uri);
					getContext().startActivity(i);
				}
			}
			else if (type == ControlType.INTEGER)
			{
				if (null != mNumericKeyboard)
				{
					mNumericKeyboard.setVisibility(View.VISIBLE, ControlType.INTEGER);
					
				}
			}
			else if (type == ControlType.DECIMAL)
			{
				if (null != mNumericKeyboard)
				{
					mNumericKeyboard.setVisibility(View.VISIBLE, ControlType.DECIMAL);
					
				}
			}
		}
		return true;
		
	}
	
	public Fields getSelectData()
	{
		if (mSelectedY > 0 && mSelectedY <= dataList.size())
		{
			mSelectedData = dataList.getFields(mSelectedY - 1);
		}
		return mSelectedData;
	}
	
	/**
	 * 计算选中的行与列
	 */
	private void cacSelectedRowAndColumn(int x, int y)
	{
		if (y < mCellHeight)
		{ // 点击锁定的行,第一行
			mSelectedY = 0;
			int tmpX = x;
			// 点击锁定的列
			if (x > mTotalLockedColumnWidth)
			{
				tmpX = x + mOffsetX;
			}
			int tmpSelectedColumn = 0;
			for (int i = 0; i < mCellsWidth.length; i++)
			{
				if (tmpX > mCellsWidth[i])
				{
					tmpSelectedColumn = i;
					continue;
				}
				else
				{
					break;
				}
			}
			mSelectedX = tmpSelectedColumn;
			return;
		}
		
		mSelectedY = (y + mOffsetY) / mCellHeight;
		
		int tmpX = x;
		// 点击锁定的列
		if (x > mTotalLockedColumnWidth)
		{
			tmpX = x + mOffsetX;
		}
		int tmpSelectedColumn = 0;
		for (int i = 0; i < mCellsWidth.length; i++)
		{
			if (tmpX > mCellsWidth[i])
			{
				tmpSelectedColumn = i;
				continue;
			}
			else
			{
				break;
			}
		}
		mSelectedX = tmpSelectedColumn;
	}
	
	/**
	 * 设置行的高度
	 * 
	 * @param height
	 */
	public void setCellHeight(int height)
	{
		this.mCellHeight = height;
	}
	
	/**
	 * 设置每一列的宽度
	 * 
	 * @param columnWidth
	 *            注意该数组长度必须与实际的列数一致
	 */
	public void setColumnWidth(int[] columnWidth)
	{
		this.mColumnWidth = columnWidth;
	}
	
	/**
	 * 设置锁定的列数，必须小于等于表格总的列数
	 * 
	 * @param count
	 */
	public void setLockedColumnsCount(int count)
	{
		mLockedColumnsCount = count;
	}
	
	/**
	 * 设置每一列的样式
	 * 
	 * @param styles
	 *            注意该数组长度必须与实际的列数一致
	 */
	public void setColumnStyles(ColumnStyle[] styles)
	{
		mColumnStyles = styles;
	}
	
	/**
	 * 设置数字键盘
	 * 
	 * @param numericKeyboard
	 */
	public void setNumericKeyboard(NumericKeyboard numericKeyboard)
	{
		mNumericKeyboard = numericKeyboard;
	}
	
	/**
	 * 设置数据源，二维数组
	 * 
	 * @param dataSrc
	 */
	// public void setDataSource(String[][] dataSrc) {
	// mDataSource = dataSrc;
	// }
	
	public void setDataInfo(List<UIItem> itemlist, FieldsList dataList)
	{
		mList = itemlist;
		mTotalColums = itemlist.size();
		this.dataList = dataList;
		if (dataList != null)
			mTotalRows = dataList.size() + 1;
		else
			mTotalRows = 1;
		init();
	}
	
	// private List<UIItem> getUIItemList() {
	// return mList;
	// }
	
	// 获取填写过的并且需要收集的Data
	public FieldsList getDataList()
	{
		FieldsList list = new FieldsList();
		Enumeration<Fields> e = getDataHashtable().elements();
		while (e.hasMoreElements())
			list.setFields(e.nextElement());
		return list;
	}
	
	// 当前grid加载到的Activity
	public void setLinkHandler(Handler handler)
	{
		mHandler = handler;
	}
	
	private Handler getLinkHandler()
	{
		return mHandler;
	}
	
	// 当前grid的使用类型
	public void setCustomGridType(int customGridType)
	{
		mCustomGridType = customGridType;
	}
	
	private int getCustomGridType()
	{
		return mCustomGridType;
	}
	
	// 当前保存数据的填写数据的Hashtable
	public void setDataHashtable(Hashtable<String, Fields> hashtable)
	{
		mHashtable = hashtable;
	}
	
	public Hashtable<String, Fields> getDataHashtable()
	{
		if (mHashtable == null)
			mHashtable = new Hashtable<String, Fields>();
		return mHashtable;
	}
	
	// 当前保存数据的填写数据的Hashtable
	public void setTemplateType(String templateType)
	{
		mTemplateType = templateType;
	}
	
	public String getTemplateType()
	{
		return mTemplateType;
	}
	
	private void init()
	{
		
		mPaint.setAntiAlias(true);
		
		int[] columnWidth = new int[mTotalColums];
		
		ColumnStyle[] styles = new ColumnStyle[mTotalColums];
		ColumnStyle style;
		
		UIItem ui;
		// int width = 0;
		// int restWidth;// 剩余宽度
		for (int i = 0; i < mTotalColums; i++)
		{
			
			ui = getCurrUIItem(i);
			// System.out.println("eeeee"+Tool.getScreenWidth());
			// System.out.println("当前"+width);
			// if (i == (mTotalColums - 1) && width < Tool.getScreenWidth()
			// && mCustomGridType == Constants.CustomGridType.DASHBOARD) {
			// restWidth = Tool.getScreenWidth() - width;
			// if (ui.getWidth() > 0) {
			// if (ui.getWidth() < restWidth)
			// columnWidth[i] = restWidth;
			// else
			// columnWidth[i] = ui.getWidth();
			// } else {
			// if (ui.getControlType() == ControlType.SELECTED) {
			// if (restWidth > 60)
			// columnWidth[i] = restWidth;
			// else
			// columnWidth[i] = 60;
			// } else {
			// if (restWidth > 120)
			// columnWidth[i] = restWidth;
			// else
			// columnWidth[i] = 120;
			// }
			// }
			//
			// } else {
			if (ui.getWidth() > 0)
				columnWidth[i] = ui.getWidth();
			else
			{
				if (ui.getControlType() == ControlType.SELECTED)
					columnWidth[i] = 60;
				else
					columnWidth[i] = 120;
			}
			// }
			// width += columnWidth[i];
			style = new ColumnStyle();
			
			style.setAlign(ui.getAlign());
			style.setTextSize(Tool.dip2px(getContext(), 13));
			if (ui.getControlType() == ControlType.SINGLECHOICE || ui.getControlType() == ControlType.MULTICHOICE)
			{
				style.setControlType(ui.getControlType(), Sqlite.getDictDataList(getContext(), ui.getDicId(), ""));
			}
			else
				style.setControlType(ui);
			styles[i] = style;
		}
		setColumnWidth(columnWidth);
		setColumnStyles(styles);
		setInitialized(true);
	}
	
	/**
	 * 设置每行的背景颜色
	 */
	// public void setRowColors(int[] colors) {
	// if (colors.length < mTotalRows)
	// throw new IllegalArgumentException("颜色数组的长度必须大于或等于数据源的长度");
	// mRowColors = colors;
	// }
	
	/**
	 * 设置是否初始化完毕
	 * 
	 * @param initialized
	 */
	public void setInitialized(boolean initialized)
	{
		this.mInitialized = initialized;
		initTableParams();
		invalidate();
	}
	
	/**
	 * 设置title的背景图片
	 * 
	 * @param resId
	 */
	public void setTitleBackground(int resId)
	{
		if (resId <= 0)
			return;
		mTitleBackground = getResources().getDrawable(resId);
	}
	
	private Drawable getTitleBackground()
	{
		if (mTitleBackground == null)
			mTitleBackground = getResources().getDrawable(R.drawable.mentou_bg);
		return mTitleBackground;
	}
	
	// public void setSelectBackground(int resId) {
	// if (resId <= 0)
	// return;
	// mSelectBackground = getResources().getDrawable(resId);
	// }
	
	private Drawable getSelectBackground()
	{
		if (mSelectBackground == null)
			mSelectBackground = getResources().getDrawable(R.drawable.table_click);
		return mSelectBackground;
	}
	
	// public void setContentBackground(int resId1, int resId2) {
	// if (resId1 <= 0 || resId2 <= 0)
	// return;
	// mContentBackground1 = getResources().getDrawable(resId1);
	// mContentBackground2 = getResources().getDrawable(resId2);
	// }
	
	// private Drawable getContentBackground1() {
	// if (mContentBackground1 == null)
	// mContentBackground1 = getResources().getDrawable(R.drawable.list1);
	// return mContentBackground1;
	// }
	
	private Drawable getContentBackground2()
	{
		if (mContentBackground2 == null)
			mContentBackground2 = getResources().getDrawable(R.drawable.table_td_click);
		return mContentBackground2;
	}
	
	private void initTableParams()
	{
		mPaint.setStyle(Paint.Style.STROKE);
		
		// 计算整个表格宽度
		for (int i = 0; i < mColumnWidth.length; i++)
		{
			mTotalWidth += mColumnWidth[i];
		}
		mCellsWidth = new int[mColumnWidth.length];
		for (int i = 0; i < mCellsWidth.length; i++)
		{
			mCellsWidth[i] = cacCellsWidth(mColumnWidth, i);
		}
		// 计算整个表格高度
		mTotalHeight = mTotalRows * mCellHeight;
		
		int totalLockedWidth = 0;
		for (int i = 0; i < mLockedColumnsCount; i++)
		{
			totalLockedWidth += mColumnWidth[i];
		}
		mTotalLockedColumnWidth = totalLockedWidth;
		
		mGestureDetector = new GestureDetector(this);
	}
	
	private int cacCellsWidth(int[] columnWidth, int index)
	{
		int total = 0;
		for (int i = 0; i < index; i++)
		{
			total += columnWidth[i];
		}
		return total;
	}
	
	/**
	 * 构造一个带有输入框的dialog
	 */
	private void buildEditDialog()
	{
		if (null != mEditDialog)
			return;
		mEditText = new EditText(getContext());
		mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setView(mEditText);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
				String str = mEditText.getText().toString();
				if ("1".equals(mEditText.getTag()))
				{
					if (!Tool.isEmail(str))
					{
						Tool.showToastMsg(getContext(), "请输入正确的Email地址", AlertType.ERR);
						return;
					}
				}
				
				setData(mSelectedY, mSelectedX, str);
				invalidate();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
				
			}
		});
		mEditDialog = builder.create();
	}
	
	/**
	 * 显示多选对话框
	 * 
	 * @param selectX
	 * @param selectY
	 */
	private void showMultiChoiceDialog(int selectX, int selectY)
	{
		if (null != mMultiChoiceDialog)
		{
			mMultiChoiceDialog.dismiss();
			mMultiChoiceDialog = null;
		}
		ColumnStyle style = mColumnStyles[selectX];
		final String[] choiceArr = style.getChoiceArray();
		final boolean[] checkedArr = new boolean[choiceArr.length];
		for (int i = 0; i < checkedArr.length; i++)
		{
			if (getResult(mSelectedY, mSelectedX).indexOf(choiceArr[i]) != -1)
				checkedArr[i] = true;
			else
				checkedArr[i] = false;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMultiChoiceItems(choiceArr, checkedArr, new OnMultiChoiceClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked)
			{
				checkedArr[which] = isChecked;
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < checkedArr.length; i++)
				{
					if (checkedArr[i])
					{
						sb.append(choiceArr[i]);
						sb.append(";");
					}
				}
				if (sb.length() > 0)
					sb.deleteCharAt(sb.length() - 1);
				setData(mSelectedY, mSelectedX, sb.toString());
				invalidate();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
				
			}
		});
		mMultiChoiceDialog = builder.create();
		mMultiChoiceDialog.show();
	}
	
	/**
	 * 显示单选对话框
	 * 
	 * @param selectX
	 * @param selectY
	 */
	private void showSingleChoiceDialog(int selectX, int selectY)
	{
		if (null != mSingleChoiceDialog)
		{
			mSingleChoiceDialog.dismiss();
			mSingleChoiceDialog = null;
		}
		final ChoiceGroupAdapter mChoiceGroupAdapter = new ChoiceGroupAdapter(getContext(), getCurrUIItem(mSelectedX));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setSingleChoiceItems(mChoiceGroupAdapter, mChoiceGroupAdapter.getPosition(getResult(mSelectedY, mSelectedX)), new DialogInterface.OnClickListener()
		{
			// @Override
			public void onClick(DialogInterface dialog, int which)
			{
				
				setData(mSelectedY, mSelectedX, mChoiceGroupAdapter.getSaveValue(which));
				
				invalidate();
				dialog.dismiss();
			}
		});
		
		mSingleChoiceDialog = builder.create();
		mSingleChoiceDialog.show();
	}
	
	// private UIItem getUIItem() {
	// return this.mList.get(mSelectedX);
	// }
	//
	// private int getIntValue(String result) {
	// if (result.equals(""))
	// return 0;
	// else
	// return Integer.valueOf(result);
	// }
	
	/**
	 * 响应数字键盘的输入
	 * 
	 * @param numeric
	 */
	public void setNumeric(String numeric)
	{
		
		try
		{
			if (mSelectedX != -1)
			{
				// if (getUIItem().getContrast().mKey.equals("")
				// && getUIItem().getControlTypeId() ==
				// Constants.ControlType.INTEGER) {
				// if (getUIItem().getContrast().mType == 1) {
				// if (getIntValue(numeric) <= getSelectData()
				// .getIntValue(getUIItem().getContrast().mKey))
				// setData(mSelectedY, mSelectedX, numeric);
				// else
				// getLinkHandler()
				// .sendMessage(
				// getLinkHandler()
				// .obtainMessage(
				// Constants.CustomGridType.STOCK,
				// getUIItem()
				// .getCaption()
				// + "不能大于"
				// + getUIItem()
				// .getContrast().mCaption));
				//
				// } else if (getUIItem().getContrast().mType == 2) {
				// if (getIntValue(numeric) >= getSelectData()
				// .getIntValue(getUIItem().getContrast().mKey))
				// setData(mSelectedY, mSelectedX, numeric);
				// else
				// getLinkHandler()
				// .sendMessage(
				// getLinkHandler()
				// .obtainMessage(
				// Constants.CustomGridType.STOCK,
				// getUIItem()
				// .getCaption()
				// + "不能小于"
				// + getUIItem()
				// .getContrast().mCaption));
				// }
				// } else
				setData(mSelectedY, mSelectedX, numeric);
			}
		}
		catch (Exception ex)
		{
			getLinkHandler().sendMessage(getLinkHandler().obtainMessage(Constants.CustomGridType.STOCK, "异常错误，请重新填写！"));
		}
		
		invalidate();
	}
}
