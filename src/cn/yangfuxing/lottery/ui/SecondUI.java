package cn.yangfuxing.lottery.ui;

import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.ui.manager.BaseUI;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class SecondUI  extends BaseUI{

	private TextView textView ;
	public SecondUI(Context context) {
	        super(context);
        }

	protected void init() {
		textView = new TextView(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		textView.setLayoutParams(layoutParams);
		textView.setBackgroundColor(Color.BLUE);
		textView.setText("这是二个简单的页面");	        
        }

	public View getChild()
	{
		return textView;
	}

	@Override
        public int getId() {
	        return ConstantValue.VIEW_SECOND;
        }

	@Override
        protected void setListener() {
	        
        }
}
