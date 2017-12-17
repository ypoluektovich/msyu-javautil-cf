package org.msyu.javautil.cf;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import static java.util.Arrays.asList;

public class Iterators {

	public static <E> Iterator<E> singletonIterator(E element) {
		return new Iterator<E>() {
			private boolean hasNext = true;

			@Override
			public final boolean hasNext() {
				return hasNext;
			}

			@Override
			public final E next() {
				if (!hasNext) {
					throw new NoSuchElementException();
				}
				hasNext = false;
				return element;
			}
		};
	}

	public static <E> Iterator<E> concat(List<Iterator<E>> iterators) {
		return new Iterator<E>() {
			private final Queue<Iterator<E>> queue = new ArrayDeque<>(iterators);
			private Iterator<E> current = queue.poll();

			@Override
			public final boolean hasNext() {
				if (current == null) {
					return false;
				}
				while (!current.hasNext()) {
					current = queue.poll();
					if (current == null) {
						return false;
					}
				}
				return true;
			}

			@Override
			public final E next() {
				if (hasNext()) {
					return current.next();
				} else {
					throw new NoSuchElementException();
				}
			}
		};
	}

	@SafeVarargs
	public static <E> Iterator<E> concat(Iterator<E>... iterators) {
		return concat(asList(iterators));
	}

	public static <E> Iterator<E> cutoff(Iterator<E> back, int count) {
		return new CutoffIterator<>(back, count);
	}

	private static final class CutoffIterator<E> implements Iterator<E> {

		private final Iterator<E> back;
		private int remainingCount;

		private E next;
		private boolean hasNext;

		CutoffIterator(Iterator<E> back, int count) {
			this.back = back;
			this.remainingCount = count;
			updateNext();
		}

		private void updateNext() {
			if (remainingCount == 0) {
				hasNext = false;
				next = null;
				return;
			}
			if (back.hasNext()) {
				hasNext = true;
				next = back.next();
				--remainingCount;
			} else {
				hasNext = false;
				next = null;
			}
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public E next() {
			if (!hasNext) {
				throw new NoSuchElementException();
			}
			E elementToReturn = next;
			updateNext();
			return elementToReturn;
		}

	}

	public static <E> ListIterator<E> reverseListIterator(List<E> list) {
		return reverseListIterator(list.listIterator(list.size()), list.size());
	}

	public static <E> ListIterator<E> reverseListIterator(ListIterator<E> back, int size) {
		return new ReverseListIterator<>(back, size);
	}

	private static final class ReverseListIterator<E> implements ListIterator<E> {

		private final ListIterator<E> back;
		private int size;

		ReverseListIterator(ListIterator<E> back, int size) {
			this.back = back;
			this.size = size;
		}

		@Override
		public boolean hasNext() {
			return back.hasPrevious();
		}

		@Override
		public E next() {
			return back.previous();
		}

		@Override
		public boolean hasPrevious() {
			return back.hasNext();
		}

		@Override
		public E previous() {
			return back.next();
		}

		@Override
		public int nextIndex() {
			return size - back.nextIndex();
		}

		@Override
		public int previousIndex() {
			return nextIndex() - 1;
		}

		@Override
		public void remove() {
			back.remove();
			--size;
		}

		@Override
		public void set(E e) {
			back.set(e);
		}

		@Override
		public void add(E e) {
			back.add(e);
			back.previous();
			++size;
		}

	}

}
