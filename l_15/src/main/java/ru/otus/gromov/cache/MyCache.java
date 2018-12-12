package ru.otus.gromov.cache;
/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

public interface MyCache<K, V> {

	void put(K key, V value);

	V get(K key);

	int getHitCount();

	int getMissCount();

	void dispose();

}
