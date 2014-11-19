package cn.yangfuxing.lottery.ui.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.yangfuxing.lottery.R;

/**
 * 选号用的适配器　
 * @author flystar
 *
 */
public class PoolAdapter extends BaseAdapter {

	private Context context;
	private int ballNum;
	private List<Integer> slectedNums;
	private int slectedBgResId;

	public PoolAdapter(Context context, int ballNum, List<Integer> slectedNums, int slectedBgResId) {
	        super();
	        this.context = context;
	        this.ballNum = ballNum;
	        this.slectedNums = slectedNums;
	        this.slectedBgResId = slectedBgResId;
        }

	@Override
	public int getCount() {
		return ballNum;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView ball = new TextView(context);
		DecimalFormat decimalFormat = new DecimalFormat("00");
		ball.setText(decimalFormat.format(position+1));
		ball.setTextSize(16);
		ball.setGravity(Gravity.CENTER);
		if(slectedNums.contains(position+1))
		{
			ball.setBackgroundResource(slectedBgResId);
		}else
		{
			ball.setBackgroundResource(R.drawable.id_defalut_ball);
		}
		return ball;
	}

}
