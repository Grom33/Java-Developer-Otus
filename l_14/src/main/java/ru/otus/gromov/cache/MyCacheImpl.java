package ru.otus.gromov.cache;

import lombok.extern.slf4j.Slf4j;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

@Slf4j
public class MyCacheImpl<K, V> implements MyCache<K, V> {

	private static final int TIME_THRESHOLD_MS = 5;

	private final int maxElements;
	private final long lifeTimeMs;
	private final long idleTimeMs;
	private final boolean isEternal;

	private final Map<K, SoftReference<CacheElement>> elements = new LinkedHashMap<>();
	private final Timer timer = new Timer();

	private int hit = 0;
	private int miss = 0;

	MyCacheImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
		this.maxElements = maxElements;
		this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
		this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
		this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
	}

	@Override
	public void put(K key, V value) {
		log.info("Put value {} with key {} to cash ", value, key);
		removeElementIfCashFullFilled();
		elements.put(key, new SoftReference<>(new CacheElement<>(key, value)));
		if (!isEternal) scheduleTask(key);
	}

	@Override
	public V get(K key) {
		SoftReference<CacheElement> element = elements.get(key);
		if (element != null) {
			CacheElement<K, V> elementValue = element.get();
			if (elementValue != null) {
				this.hit++;
				log.info("Get value with key {} from cash ", key);
				return elementValue.getValue();
			} else {
				this.miss++;
				log.info("Element with key {} destroyed by GC", key);
				return null;
			}
		} else {
			this.miss++;
			log.info("Element with key {} removed by task", key);
			return null;
		}
	}

	public int getHitCount() {
		return hit;
	}

	public int getMissCount() {
		return miss;
	}

	public void dispose() {
		timer.cancel();
		log.info("Scheduler canceled");
	}

	private void removeElementIfCashFullFilled() {
		if (elements.size() == maxElements) {
			K firstKey = elements.keySet().iterator().next();
			elements.remove(firstKey);
			log.info("Cash was full, element with key {} removed", firstKey);
		}
	}

	private void scheduleTask(K key) {
		if (lifeTimeMs != 0) {
			TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
			timer.schedule(lifeTimerTask, lifeTimeMs);
			log.info("Task remove by time life scheduled for element with key {}", key);
		}
		if (idleTimeMs != 0) {
			TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
			timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
			log.info("Task remove by idle time scheduled for element with key {}", key);
		}
	}

	private TimerTask getTimerTask(final K key, Function<CacheElement<K, V>, Long> timeFunction) {
		return new TimerTask() {
			@Override
			public void run() {
				SoftReference<CacheElement> element = elements.get(key);
				if (element == null || element.get() == null || isT1BeforeT2(timeFunction.apply(element.get()), System.currentTimeMillis())) {
					log.info("Element with key {} is removing by task", key);
					elements.remove(key);
					this.cancel();

				}
			}
		};
	}

	private boolean isT1BeforeT2(long t1, long t2) {
		return t1 < t2 + TIME_THRESHOLD_MS;
	}

	public class CacheElement<A, B> {
		private final A key;
		private final B value;
		private final long creationTime;
		private long lastAccessTime;

		CacheElement(A key, B value) {
			this.key = key;
			this.value = value;
			this.creationTime = getCurrentTime();
			this.lastAccessTime = getCurrentTime();
		}

		long getCurrentTime() {
			return System.currentTimeMillis();
		}

		public A getKey() {
			return key;
		}

		B getValue() {
			setAccessed();
			return value;
		}

		long getCreationTime() {
			return creationTime;
		}

		long getLastAccessTime() {
			return lastAccessTime;
		}

		void setAccessed() {
			lastAccessTime = getCurrentTime();
		}
	}

}
