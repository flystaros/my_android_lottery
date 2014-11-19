package cn.yangfuxing.lottery.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 
 * @author flystar
 *
 * @param <K>
 * @param <V>
 */
public class SorftMap<K,V> extends HashMap<K, V> 
{
	private HashMap<K, SoftValue<K,V>> temp;  //存放袋子的集合　	
	private ReferenceQueue<? super V> queue;
	
	public  SorftMap() {
		temp = new HashMap<K, SoftValue<K,V>>();
		queue = new ReferenceQueue<V>();
        }
	
	@Override
        public V put(K key, V value) {
//		SoftReference<V> sr = new SoftReference<V>(value);
		
		SoftValue<K,V> sr = new SoftValue<K,V>(key,value, queue);
		temp.put(key, sr);
	        return null;
        }
	
	
	@Override
	public V get(Object key) {
		clearsr();
		SoftValue<K,V> sr = temp.get(key);
		if(sr != null)
		{
			return sr.get();
		}
	        return null;
	}
	
	@Override
	public boolean containsKey(Object key) {
		
		return get(key) != null;
	}
	
	private void clearsr()
	{
		SoftValue<K,V> poll = (SoftValue<K,V>) queue.poll();
		while(poll != null)
		{
			temp.remove(poll.key);
			poll = (SoftValue<K,V>) queue.poll();
		}
	}
	
	/**
	 * 增强版的袋子, 增加了一个key
	 * @author flystar
	 *
	 * @param <K>
	 * @param <V>
	 */
	private class SoftValue<K,V> extends SoftReference<V>
	{
		private Object key;
		public SoftValue(K key,V r, ReferenceQueue<? super V> q) 
		{
	                super(r, q);
	                this.key = key;
                }

		
	}
	
}

