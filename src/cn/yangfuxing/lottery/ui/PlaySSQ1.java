package cn.yangfuxing.lottery.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.R;
import cn.yangfuxing.lottery.ui.adapter.PoolAdapter;
import cn.yangfuxing.lottery.ui.manager.BaseUI;
import cn.yangfuxing.lottery.ui.manager.TitleManager;

/**
 * 双色球的选号界面　
 * @author flystar
 *
 */

public class PlaySSQ1 extends BaseUI 
{
	//1,标题
	//２，填充选号容器
	//3,选号：单击＋机选
	//4,手机摇一摇
	//5,提示信息＋清空＋选好了
	
	//机选按钮
	private Button randomRed;
	private Button randomBlue;
	//选号容器
	private GridView redContainer;
	private GridView blueContainer;
	private PoolAdapter redAdapter;
	private PoolAdapter blueAdapter;
	
	private List<Integer> redNums;     //  restore the selected red ball's num
	private List<Integer> blueNums;  
	
	public PlaySSQ1(Context context) {
	        super(context);
        }

	@Override
	public int getId() {
		return ConstantValue.VIEW_PLAYSSQ;
	}

	@Override
	protected void init() {
		showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_playssq1, null);
		
		randomRed = (Button) findViewById(R.id.ii_ssq_random_red);
		randomBlue = (Button) findViewById(R.id.ii_ssq_random_blue);
		redContainer = (GridView) findViewById(R.id.ii_ssq_red_number_container);
		blueContainer = (GridView) findViewById(R.id.ii_ssq_blue_number_container);
		
		redNums = new  ArrayList<Integer>();
		blueNums = new ArrayList<Integer>();
		
		redAdapter = new PoolAdapter(context,33,redNums,R.drawable.id_redball);
		redContainer.setAdapter(redAdapter);
		
		blueAdapter = new PoolAdapter(context, 16,blueNums,R.drawable.id_blueball);
		blueContainer.setAdapter(blueAdapter);
	}

	@Override
	protected void setListener() {
		randomRed.setOnClickListener(this);
		randomBlue.setOnClickListener(this);
		
		redContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(!redNums.contains(position+1))
				{
					view.setBackgroundResource(R.drawable.id_redball);
					view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_change));
					redNums.add(position+1);
				}
				else
				{
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					redNums.remove((Object)(position+1));
				}
                        }
			
		});
		
		blueContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(!blueNums.contains(position+1))
				{
					view.setBackgroundResource(R.drawable.id_blueball);
					view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_change));
					blueNums.add(position+1);
				}
				else
				{
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					blueNums.remove((Object)(position+1));
				}
                        }
			
		});
		
		
	}

	@Override
        public void onClick(View v) {
		 switch (v.getId()) {
		case R.id.ii_ssq_random_red:
			
			break;
		case R.id.ii_ssq_random_blue:
			break;
		default:
			break;
		}
	        super.onClick(v);
        }
	
	/**
	 * 　修改界面的标题
	 */
	private void changeTitle()
	{
		String titleInfo="";
		//1,标题  －－－－　界面之间的数据传递
		//判断购彩大厅是否获取到期次信息
		if(bundle != null)
		{
			titleInfo = "双色球" + bundle.getString("issue") + "期";
		}
		else
		{
			titleInfo = "双色球选号";
		}
		//如果获取到：拼装标题
		//否则默认的标题展示
		 
		TitleManager.getInstance().changeTitle(titleInfo);
	}
	
	@Override
	public void onResume() {
		changeTitle();
	        super.onResume();
	}
	

}
