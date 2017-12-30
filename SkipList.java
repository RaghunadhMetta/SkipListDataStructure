

import java.util.Iterator;
import java.util.Random;

//Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
	private int maxLevel;
	private int size;
	SkipListEntry<T> head, tail;
 // Constructor
 public SkipList() {
	 maxLevel = 0;
	 size = 0;
	 head = new SkipListEntry(null, maxLevel);
	 tail = head; 
 }

 // Add x to list. If x already exists, replace it. Returns true if new node is added to list
 public boolean add(T x) {
	 SkipListEntry<T>[] prev = find(x);
	 //if the element is the first
	 if(prev[0]==head && prev[0].next[0]==null) {
		 head.next[0] = new SkipListEntry<T>(x, 0);
		 tail = head.next[0];
		 head.span[0] = 1;
		 size++;
		 return true;
	 }
	 //if the prev of finding an element is not null
	 if(prev[0].next[0]!=null) {
		//if the element already exists, replace the element and return it
		 if(prev[0].next[0].element.compareTo(x)==0) {
			 prev[0].next[0].element = x;
		     return false; 
		     }
	 }
	     //adding element to the list
		 int lev = chooseLevel(maxLevel);
		  SkipListEntry<T> newEntry = new SkipListEntry<T>(x, lev);
		  for(int i=0; i<=maxLevel; i++) {
			  if(i<=lev) {
				  newEntry.next[i] = prev[i].next[i];
				  prev[i].next[i] = newEntry;
				  newEntry.span[i] = prev[i].span[i];
				  prev[i].span[i] = 1;
			  }
			  else {
				  prev[i].span[i] +=1;
			  }	    
		  }
		  size++;
		  if(prev[0].equals(tail))
			  tail=prev[0].next[0];
		  rebuild();
		  return true;
	 } 
 
 //Helper function to locate the element which returns the array of nodes when we go one level down
 public SkipListEntry<T>[] find(T element) {
	 SkipListEntry<T> p = null;
	 p = head;
	 SkipListEntry<T>[] prev = new SkipListEntry[maxLevel+1];
	 for(int i=maxLevel; i>=0; i--) {
		 while(p.next[i]!=null && p.next[i].element.compareTo(element)<0) {
			 p = p.next[i];
		 }
		 prev[i] = p;
	 }
	return prev; 
 }
 
 //Choose number of levels for a new node randomly
 public int chooseLevel(int lev) {
	 int i = 0;
	 boolean b;
	 Random random = new Random();
	 while(i<lev) {
		 b = random.nextBoolean();
	 if(b)
		 i++;
	 else
		 break;
	 }
	return i; 
 }

 // Find smallest element that is greater or equal to x
 public T ceiling(T x) {
	 SkipListEntry<T>[] prev = find(x);
	 if(prev[0].next[0]==null)
		 return null;
	 else
		 return prev[0].next[0].element;
 }

 // Does list contain x?
 public boolean contains(T x) {
	 SkipListEntry<T>[] prev = find(x);
	 if(prev[0].next[0]==null)
		 return false;
	 else
		 return prev[0].next[0].element.equals(x)?true:false;
 }

 // Return first element of list
 public T first() {
	return head.next[0]==null?null:head.next[0].element;
 }

 // Find largest element that is less than or equal to x
 public T floor(T x) {
	 SkipListEntry<T>[] prev = find(x);
	 if(prev[0].next[0]==null)
		 return prev[0].element;
	 else
		 return prev[0].next[0].element.equals(x)?x:prev[0].element;
 }

 // Return element at index n of list.  First element is at index 0.
 public T get(int n){
	 int index = 0;
	 SkipListEntry<T> entry = head;
	 if(size == 0 || n >= size){
		 return null;
	 }
	 else{
		 while(index <= n){
			 entry = entry.next[0];
			 index++;
		 }
		 return entry.element;
	 }
 }

 // Is the list empty?
 public boolean isEmpty() {
	return size==0;
 }

 // Iterate through the elements of list in sorted order
 public Iterator<T> iterator() {
	return new SkipListIterator(head);
 }
 
 // Return last element of list
 public T last() {
	return tail.element;
 }

 // Reorganize the elements of the list into a perfect skip list
 /*
  * Rebuilds into perfect skip list of size = 2^ new level -1.
  * It is called periodically when size = 2^(maxLevel+1)-1 or size = 2^(maxLevel-1)-1
  */
 public void rebuild() {
	 SkipListEntry<T>[] newSkipList = new SkipListEntry[size];
	 if(size==Math.pow(2, maxLevel+2)-1) {
		 rebuild(newSkipList, maxLevel+1, 0, size-1);
		 head = update(newSkipList, maxLevel+1);
		 maxLevel++;
	 }
	 else if(size==Math.pow(2, maxLevel-1)-1){
		 rebuild(newSkipList, maxLevel-1, 0, size-1);
		 head = update(newSkipList, maxLevel-1);
		 maxLevel--;
	 }
 }
 private void rebuild(SkipListEntry<T>[] newSkipList, int level, int start, int end) {
	 if(end>=start) {
		 if(level==0) {
			 for(int i=start; i<=end; i++) {
				 newSkipList[i] = new SkipListEntry<T> (null,0);
			 }
		 }
		 else {
			 int mid = (start+end)/2;
			 newSkipList[mid] = new SkipListEntry<T> (null, level);
			 rebuild(newSkipList, level-1, start, mid-1);
			 rebuild(newSkipList, level-1, mid+1, end);		 
		 }
	 }
 }
 
 //updates the values, next array and span of rebuilt skip list
 private SkipListEntry<T> update (SkipListEntry<T>[] skipList, int level) {
	 SkipListEntry<T> updHead = new SkipListEntry<T> (null, level);
	 SkipListEntry<T>[] updNext = new SkipListEntry[level+1];
	 SkipListEntry<T> a =head.next[0];
	 int c=0, l=0;
	 int s = skipList[c].next.length;
	 while(a!=null) {
		 //forming the links
		 for(int i=0; i<s; i++) {
			 //forming head links
			 if(l<=level && l==i) {
				 updHead.next[l] = skipList[c];
			 }
			 if(updNext[i]!=null) {
				 updNext[i].next[i] = skipList[c];
			 }
			 updNext[i] = skipList[c];
			 skipList[c].span[i] = i+1;
		 }
		 skipList[c++].element = a.element;
		 a = a.next[0];
	 }
	 tail = skipList[c-1];
	 
	 //rebuilding the first node from header
	 SkipListEntry<T>[] firstEntry = new SkipListEntry[level+1];
	 int[] firstSpan = new int[level+1];
	 firstEntry[0] = skipList[0].next[0];
	 firstSpan[0] = 1;
	 updHead.span[0] = 1;
	 updHead.next[0] = skipList[0];
	 for(int i=1; i<=level; i++) {
		 firstEntry[i] = updHead.next[i];
		 firstSpan[i] = i+1;
		 updHead.span[i] = 1;
		 updHead.next[i] = skipList[0]; 
	 }
	 skipList[0].next = firstEntry;
	 skipList[0].span = firstSpan;
	 return updHead;
 }

 // Remove x from list.  Removed element is returned. Return null if x not in list
 public T remove(T x) {
	 SkipListEntry<T>[] prev = find(x);
	 //case for the end of list
	 if(prev[0].next[0]==null) 
		 return null;
	 if(prev[0].next[0].element.compareTo(x)==0) {
		 SkipListEntry<T> entry = prev[0].next[0];
		 for(int i=0; i<=maxLevel; i++) {
			 if(prev[i].next[i]!=null && prev[i].next[i].equals(entry)) {
				 prev[i].next[i] = entry.next[i];
				 prev[i].span[i] = entry.span[i];
			 }
			 else {
				 prev[i].span[i] -=1;
			 }
		 }
		 size--;
		 if(tail.equals(entry)) {
			 if(prev[0].next[0]!= null) {
				 tail = prev[0].next[0];
			 }
			 else {
				 tail = prev[0];
			 }
		 }
		 rebuild();
		 return entry.element;
	 }
	 return null;
 }

 // Return the number of elements in the list
 public int size() {
	return size;
 }
}