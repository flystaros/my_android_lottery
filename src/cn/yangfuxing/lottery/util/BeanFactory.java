package cn.yangfuxing.lottery.util;

import java.io.IOException;
import java.util.Properties;

public class BeanFactory 
{
	private static Properties properties;
	static 
	{
		properties = new Properties();
		try {
	                properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties"));
                } catch (IOException e) {
	                e.printStackTrace();
                }
	}
	
	public static<T>  T getImpl(Class<T> clazz)     
	{
		String key  =  clazz.getSimpleName();
		String className = properties.getProperty(key);
		
		try {
	                return (T) Class.forName(className).newInstance();
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
	                e.printStackTrace();
                }
		return null;
	}
}
  