
import java.util.Iterator;

public class SkipListIterator<T extends Comparable<? super T>> implements Iterator<T> {
	//constructor
	 SkipListEntry<T> curr, prev;
	 public SkipListIterator (SkipListEntry<T> head) {
		 curr = head;
		 prev = null;
	 }
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return curr.next[0]!=null;
	}

	@Override
	public T next() {
		// TODO Auto-generated method stub
		prev = curr;
		curr = curr.next[0];
		return curr.element;
	}

}
