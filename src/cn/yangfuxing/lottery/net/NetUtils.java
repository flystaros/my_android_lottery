package cn.yangfuxing.lottery.net;

import cn.yangfuxing.lottery.GlobalParams;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class NetUtils 
{
	//andorid 4.0以上　已屏蔽掉该权限　
	private static Uri PREFERRED_APN_URI = Uri.parse("conent://telephony/carriers/preferapn");
	
	public static boolean checkNet(Context context)
	{
		// 1,判断WLAN(wi-fi)联网
		//２，手机APN接入点
		//如果没有联网提示用户
		//如果是APN接入点(基站)
		//判断WAP还是NET
		
			boolean isWIFI = isWIFIConnectivity(context);
			boolean isMOBILE = isMOBILEConnectivity(context);
			
			if(!isWIFI&&!isMOBILE)
			{
				return false;
			}
			
			if(isMOBILE)
			{
				// 判断wap net 
				//通过读取APN的配置信息
				readAPN(context);
			}
		 
		return true;
	}

	private static void readAPN(Context context) 
	{
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(PREFERRED_APN_URI, null, null, null, null);
		if(cursor != null)
		{
			if(cursor.moveToFirst())
			{
				GlobalParams.PROXY_IP = cursor.getString(cursor.getColumnIndex("proxy"));
				GlobalParams.PROXY_PORT = cursor.getInt(cursor.getColumnIndex("port"));
			}
		}
		
		
	}

	private static boolean isMOBILEConnectivity(Context context) 
	{
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(networkInfo != null)
		{
			return networkInfo.isConnected();
		}
		
		return false;
	}

	private static boolean isWIFIConnectivity(Context context) 
	{
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);   //得到WIFI的信息
		if(networkInfo != null)
		{
			return networkInfo.isConnected();
		}
		return false;
	}
}
