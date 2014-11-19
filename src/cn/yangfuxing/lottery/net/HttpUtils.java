package cn.yangfuxing.lottery.net;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.yangfuxing.lottery.ConstantValue;
import cn.yangfuxing.lottery.GlobalParams;

/**
 *  通信工具
 * @author flystar
 */
public class HttpUtils 
{
	//如果代理信息非空，就设置代理
	private HttpClient client;
	private HttpPost post;
	private HttpGet get;
	

	public HttpUtils() 
	{
		super();
		client = new DefaultHttpClient();
		
		if(StringUtils.isNotBlank(GlobalParams.PROXY_IP))
		{
			//设置代理
			HttpHost host  = new HttpHost(GlobalParams.PROXY_IP, GlobalParams.PROXY_PORT);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
		}
	}
	
	/**
	 *   向服务端请求数据
	 * @param uri
	 * @param xml
	 * @return
	 */
	public InputStream sendXML(String uri,String xml)
	{
		post = new HttpPost(uri);
		try {
			StringEntity entity = new StringEntity(xml,ConstantValue.ENCODING);
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				return response.getEntity().getContent();
			}
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}
