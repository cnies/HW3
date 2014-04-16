/**
 * NAME: Christopher Nies
 * ID: A11393577
 * LOGIN: cs12sfl
 */
import java.util.*;

public class MyLinkedList<E> extends AbstractList<E> {

  private int nelems;
  protected Node head;
  protected Node tail;

  protected class Node {
    E data;
    Node next;
    Node prev;

    /** Constructor to create singleton Node */
    public Node(E element)
    {
      this.data = element;
      this.next = null;
      this.prev = null;
    }
    /** Constructor to create singleton link it between previous and next 
     *   @param element Element to add, can be null
     *   @param prevNode predecessor Node, can be null
     *   @param nextNode successor Node, can be null 
     */
    public Node(E element, Node prevNode, Node nextNode)
    {
      this.data = element;
      this.next = nextNode;
      this.prev = prevNode;
    }
    /** Remove this node from the link. Update previous and next nodes */
    public void remove()
    {
      this.next.setPrev(this.prev);
      this.prev.setNext(this.next);
      this.next = null;
      this.prev = null;  
    }
    /** Set the previous node in the list
     *  @param p new previous node
     */
    public void setPrev(Node p)
    {
      this.prev = p;
    }
    /** Set the next node in the list
     *  @param n new next node
     */
    public void setNext(Node n)
    {
      this.next = n;
    }

    /** Set the element 
     *  @param e new element 
     */
    public void setElement(E e)
    {
      this.data = e;
    }
    /** Accessor to get the next Node in the list */
    public Node getNext()
    {
      return this.next;
    }
    /** Accessor to get the prev Node in the list */
    public Node getPrev()
    {
      return this.prev;
    } 
    /** Accessor to get the Nodes Element */
    public E getElement()
    {
      return this.data;
    } 
  }

  /** ListIterator implementation */ 
  protected class MyListIterator implements ListIterator<E> {

    private boolean forward;
    private boolean canRemove;
    private Node left,right; // Cursor sits between these two nodes
    private int idx;        // Tracks current position. what next() would
    // return 
    public MyListIterator()
    {
      this.left = head;
      this.right = head.getNext();
      this.idx = 0;
      this.canRemove = false;
    }
    //@Override
      public void add(E e) throws  NullPointerException
      {
        if (e == null)
          throw new NullPointerException();
        Node newNode = new Node(e, left, right);
        left.setNext(newNode);
        right.setPrev(newNode);
        this.left = newNode;
        nelems++;
        canRemove = false;
      }
    //@Override
      public boolean hasNext()
      {
        if (this.right != tail)
          return true;
        else
          return false;
      }

    //@Override
      public boolean hasPrevious()
      {
        if (this.left != head)
          return true;
        else
          return false;
      }
    //@Override
      public E next() throws NoSuchElementException
      {
        if (!hasNext())
          throw new NoSuchElementException();
        this.left = right;
        this.right = right.getNext();
        this.idx++;
        this.forward = true;
        this.canRemove = true;
        return left.getElement();
      }
    //@Override
      public int nextIndex()
      {
        return idx;
      }
    //@Override
      public E previous() throws NoSuchElementException
      {
        if (!hasPrevious())
          throw new NoSuchElementException();
        this.right = left;
        this.left = left.getPrev();
        this.idx--;
        this.forward = false;
        this.canRemove = true;
        return right.getElement(); 
      }

    //@Override
      public int previousIndex()
      {
        return idx-1;
      }
    //@Override
      public void remove() throws IllegalStateException
      {
        if (!canRemove)
          throw new IllegalStateException();
        if (forward){
          canRemove = false;
          //right.setPrev(left.getPrev());
          //left.getPrev().setNext(right);
          left = left.getPrev();
          left.getNext().remove();
          idx--;
          nelems--;
        }
        else {
          canRemove = false;
          //left.setNext(right.getNext());
          //right.getNext().setPrev(left);
          right = right.getNext();
          right.getPrev().remove();
          nelems--;
        }
      }
      
    //@Override
      public void set(E e) throws NullPointerException, IllegalStateException
      {
        if(e == null)
          throw new NullPointerException();
        if (!canRemove)
          throw new IllegalStateException();
        if (forward){
          left.setElement(e);
        }
        else{
          right.setElement(e);
        }
      }

  }


  //  Implementation of the MyLinkedList Class


  /** Only 0-argument constructor is define */
  public MyLinkedList()
  {
    this.head= new Node(null, null, this.tail); 
    this.tail= new Node(null, this.head, null);
    this.head.setNext(this.tail);
    this.nelems = 0;
  }
  @Override
    public int size()
    {
      return nelems;  
    }

  @Override
    public E get(int index) throws IndexOutOfBoundsException
    {
      Node toCheck = this.getNth(index);
      return toCheck.getElement(); 
    }

  @Override
    /** Add an element to the list 
     * @param index  where in the list to add
     * @param data data to add
     * @throws IndexOutOfBoundsException
     * @throws NullPointerException
     */ 
    public void add(int index, E data) 
    throws IndexOutOfBoundsException,NullPointerException
    {
      if (index<0 || index>this.nelems)
        throw new IndexOutOfBoundsException();
      if (data == null)
        throw new NullPointerException();
      if (index == 0){
        this.addAtHead(data);
        return;
      }
      if (index == this.nelems){
        this.add(data);
        return;
      }
      Node toShift = this.getNth(index);
      Node newNode = new Node(data, toShift.getPrev(), toShift);
      toShift.getPrev().setNext(newNode);
      toShift.setPrev(newNode);
      this.nelems++;
    }

    private void addAtHead(E data){
      Node toAdd = new Node(data, this.head, this.head.getNext());
      this.head.setNext(toAdd);
      toAdd.getNext().setPrev(toAdd);
    }

  /** Add an element to the end of the list 
   * @param data data to add
   * @throws NullPointerException
   */ 
  public boolean add(E data) throws NullPointerException
  {
    if (data == null)
      throw new NullPointerException();
    Node newNode = new Node(data,this.tail.getPrev(), this.tail);
    this.tail.getPrev().setNext(newNode);
    this.tail.setPrev(newNode);
    this.nelems++;
    return true; 
  }

  /** Set the element at an index in the list 
   * @param index  where in the list to add
   * @param data data to add
   * @return element data added
   * @throws IndexOutOfBoundsException
   * @throws NullPointerException
   */ 
  public E set(int index, E data) 
    throws IndexOutOfBoundsException,NullPointerException
    {
      if (index<0 || index>=this.nelems)
        throw new IndexOutOfBoundsException();
      if (data == null)
        throw new NullPointerException();
      Node toChange = this.getNth(index);
      E element = toChange.getElement();
      toChange.setElement(data);
      return element;
    }

  /** Remove the element at an index in the list 
   * @param index  where in the list to add
   * @return element the data found
   * @throws IndexOutOfBoundsException
   */ 
  public E remove(int index) throws IndexOutOfBoundsException
  {
    if (index<0 || index>=this.nelems)
      throw new IndexOutOfBoundsException();
    Node toRemove = this.getNth(index);
    E element = toRemove.getElement();
    toRemove.remove();
    //Node prev = toRemove.getPrev();
    //Node next = toRemove.getNext();
    //prev.setNext(next);
    //next.setPrev(prev);
    //toRemove = null;
    this.nelems--;
    return element;
  }

  /** Clear the linked list */
  public void clear()
  {
	  this.head.setNext(tail);
	  this.tail.setPrev(head);
    this.nelems = 0;
    //while(!this.isEmpty())
      //this.remove(0);
  }

  /** Determine if the list empty 
   *  @return true if empty, false otherwise */
  public boolean isEmpty()
  {
    if (this.head.getNext()==this.tail && this.tail.getPrev()==this.head)
      return true;
    else
      return false; // XXX-CHANGE-XXX
  }

  public Iterator<E> QQQiterator()
  {
    return new MyListIterator();
  }
  public ListIterator<E> QQQlistIterator()
  {
    return new MyListIterator();
  }

  // Helper method to get the Node at the Nth index
  private Node getNth(int index) 
  {
	if(index<0 || index>=this.nelems)
		throw new IndexOutOfBoundsException();
    Node toCheck;
    if (index>= nelems/2){
      int count = this.nelems-1;
      toCheck = this.tail.getPrev();
      while(count!=index){
        toCheck = toCheck.getPrev();
        count--;
      }
    }
    else{
      int count = 0;
      toCheck = this.head.getNext();
      while(count!=index){
        toCheck =toCheck.getNext();
        count++;
      }
    }
    return toCheck;
  }

  
  public static void main(String[] args){
    MyLinkedList<Integer> list = new MyLinkedList<Integer>();
    list.add(new Integer(10));
    list.add(new Integer(20));
    list.add(new Integer(30));
    list.add(new Integer(40));
    list.add(new Integer(50));
    list.add(2, new Integer(15));
    ListIterator<Integer> iter  = list.listIterator();
    iter.next();
    for (Integer i: list)
      System.out.println(i);

  }


      public Iterator<E> iterator()
      {
      return new MyListIterator();
      }
      public ListIterator<E> listIterator()
      {
      return new MyListIterator();
      }
}

// VIM: set the tabstop and shiftwidth to 4 
// vim:tw=78:ts=4:et:sw=4

