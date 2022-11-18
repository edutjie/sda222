import java.util.Arrays;
import java.util.NoSuchElementException;

public class MaxHeap {
  private int capacity;
  private int size = 0;
  private int[] heap;

  public MaxHeap(int capacity) {
    this.capacity = capacity;
    this.heap = new int[capacity];
  }


  private int getLeftChildIndex(int parentIndex) {
    return 2 * parentIndex + 1;
  }

  private int getRightChildIndex(int parentIndex) {
    return 2 * parentIndex + 2;
  }

  private int getParentIndex(int childIndex) {
    return (childIndex - 1) / 2;
  }

  private boolean hasLeftChild(int index) {
    return getLeftChildIndex(index) < size;
  }

  private boolean hasRightChild(int index) {
    return getRightChildIndex(index) < size;
  }

  private boolean hasParent(int index) {
    return getParentIndex(index) >= 0;
  }

  private int leftChild(int parentIndex) {
    return heap[getLeftChildIndex(parentIndex)];
  }

  private int rightChild(int parentIndex) {
    return heap[getRightChildIndex(parentIndex)];
  }

  private int parent(int childIndex) {
    return heap[getParentIndex(childIndex)];
  }

  private void swap(int index1, int index2) {
    int element = heap[index1];
    heap[index1] = heap[index2];
    heap[index2] = element;
  }

  // Time Complexity : O( Log n)
  public void add(int item) {
    ensureCapacity();
    heap[size] = item;
    size++;
    heapifyUp();
  }

  private void ensureCapacity() {
    if (size == capacity) {
      heap = Arrays.copyOf(heap, capacity * 2);
      capacity = capacity * 2;
    }
  }

  // Time Complexity : O(1)
  public int peek() {
    if (size == 0) {
      throw new NoSuchElementException();
    }
    return heap[0];
  }

  // Time Complexity : O( Log n)
  public void poll() {
    if (size == 0) {
      throw new NoSuchElementException();
    }

    int element = heap[0];

    heap[0] = heap[size - 1];
    size--;
    heapifyDown();
  }

  private void heapifyDown() {
    int index = 0;

    while (hasLeftChild(index)) {
      int smallestChildIndex = getLeftChildIndex(index);

      if (hasRightChild(index) && rightChild(index) < leftChild(index)) {
        smallestChildIndex = getRightChildIndex(index);
      }

      if (heap[index] < heap[smallestChildIndex]) {
        swap(index, smallestChildIndex);
      } else {
        break;
      }
      index = smallestChildIndex;
    }
  }

  private void heapifyUp() {
    int index = size - 1;

    while (hasParent(index) && parent(index) < heap[index]) {
      swap(getParentIndex(index), index);
      index = getParentIndex(index);
    }
  }

  private void printHeap() {
    for (int i = 0; i < size; i++) {
      System.out.print(heap[i] + " ");
    }
  }

  public static void main(String[] args) {
    MaxHeap maxHeap = new MaxHeap(10);
//    maxHeap.add(12);
//    maxHeap.add(23);
//    maxHeap.add(2);
//    maxHeap.add(112);
//    maxHeap.add(112);
//    maxHeap.add(2);
//    maxHeap.add(53);
//    maxHeap.add(1);
//    maxHeap.add(9);
//    maxHeap.add(5);
//    maxHeap.add(8);
//    maxHeap.add(3);
//    maxHeap.add(6);

    maxHeap.add(10);
    maxHeap.add(2);
    maxHeap.add(7);
    maxHeap.add(1);
    maxHeap.add(2);
    maxHeap.add(2);
    maxHeap.add(6);
    maxHeap.poll(); 
    maxHeap.printHeap();
  }
}