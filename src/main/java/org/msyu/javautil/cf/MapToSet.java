package org.msyu.javautil.cf;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @since 0.1.1
 */
public class MapToSet<K, V> extends MapToCollection<K, V, Set<V>> {
	public MapToSet(Map<K, Set<V>> backingMap, Function<? super K, ? extends Set<V>> collectionCtor) {
		super(backingMap, collectionCtor);
	}
}
