package cn.yangfuxing.lottery.ui.custom;

import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.util.DensityUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyGridView extends GridView {

	private PopupWindow pop;
	private TextView ball;
	private OnActionUpListener onActionUpListener;
	
	
	
	public void setOnActionUpListener(OnActionUpListener onActionUpListener) {
		this.onActionUpListener = onActionUpListener;
	}

	public MyGridView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        
	        View view = View.inflate(context, R.layout.il_gridview_item_pop, null);
	        ball = (TextView) view.findViewById(R.id.ii_pretextView);
	        pop = new  PopupWindow(context);
	        pop.setContentView(view);
	        
	        //set width and height
	        pop.setWidth(DensityUtil.dip2px(context, 55));
	        pop.setHeight(DensityUtil.dip2px(context, 53));
	        
        }
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		int x = (int) ev.getX();
		int y = (int) ev.getY();
		
		int position = pointToPosition(x, y);
		
		if(position == INVALID_POSITION)
		{
			return false;
		} 
		
		TextView child = (TextView) this.getChildAt(position);
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
			showPop(child);
			break;
		case MotionEvent.ACTION_MOVE:
			updatePop(child);
			break;
		case MotionEvent.ACTION_UP:
			this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
			hiddenPop();
			if(onActionUpListener != null)
			{
				onActionUpListener.onActionUp(child,position);
			}
			
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	
	private void showPop(TextView child)
	{
		int yOffset = -(pop.getHeight()+child.getHeight());
		int xOffset = -(pop.getWidth()-child.getWidth())/2;
		
		ball.setText(child.getText());
//		pop.showAsDropDown(child);
		
		pop.showAsDropDown(child, xOffset, yOffset);
		
	}
	
	private void updatePop(TextView child)
	{
		int yOffset = -(pop.getHeight()+child.getHeight());
		int xOffset = -(pop.getWidth()-child.getWidth())/2;
		ball.setText(child.getText());
		pop.update(child, xOffset, yOffset, -1, -1);
	}
	
	private void hiddenPop()
	{
		pop.dismiss(); 
	}
	
	public interface OnActionUpListener{
		void onActionUp(TextView child, int position);
	}
	
	
}
