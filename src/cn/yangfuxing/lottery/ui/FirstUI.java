package cn.yangfuxing.lottery.ui;

import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.ui.manager.BaseUI;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class FirstUI  extends BaseUI{

	public FirstUI(Context context) 
	{
	        super(context);
        }

	public View getChild()
	{
		TextView textView = new TextView(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		textView.setLayoutParams(layoutParams);
		textView.setBackgroundColor(Color.GREEN);
		textView.setText("这是一个简单的页面");
		return textView;
	}

	@Override
        public int getId() {
	        return ConstantValue.VIEW_FRIST;
        }

	@Override
        protected void init() {
	        
        }

	@Override
        protected void setListener() {
        }

}
