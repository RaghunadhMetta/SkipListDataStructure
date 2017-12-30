
public class SkipListEntry<T> {
	T element;
	SkipListEntry<T>[] next;
	int[] span;
	//Constructor
	public SkipListEntry(T element, int level) {
		this.element = element;
		next = new SkipListEntry[level+1];
		span = new int[level+1];
		for(int i=0; i<=level;i++) {
			next[i] = null;
			span[i] = 0;
		}
	}
}
