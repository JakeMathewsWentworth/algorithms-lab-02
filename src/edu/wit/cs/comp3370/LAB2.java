package edu.wit.cs.comp3370;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/* Adds floating point numbers with varying precision 
 * 
 * Wentworth Institute of Technology
 * COMP 3370
 * Lab Assignment 2
 * 
 */

class Heap {
	public Float value;
	public Heap leftChild;
	public Heap rightChild;
	
	public int getSize() {
		int size = 1;
		if(leftChild != null) {
			size += leftChild.getSize();
		}
		if(rightChild != null) {
			size += rightChild.getSize();
		}
		return size;
	}
}

public class LAB2 {
	
	//TODO: document this method
	public static float heapAdd(float[] values) {
		Arrays.sort(values);
		List<Float> list = new ArrayList<>();
		for(float value : values) {
			list.add(0, value);
		}
		Heap heap = buildHeap(list);
		while(heap.getSize() > 1) {
			Float sum = 0f;
			Heap min1 = extractMinFromHeap(heap);
			if(min1.value != null) {
				sum += min1.value;
			}
			min1.value = null;
			Heap min2 = extractMinFromHeap(heap);
			if(min2.value != null) {
			  sum += min2.value;
			}
			min2.value = null;
			heap = addToHeap(heap, sum);
		}
		
		return heap.value;
	}
	
	private static Heap buildHeap(List<Float> list) {
		if(list.isEmpty()) {
			return null;
		}
		Heap heap = new Heap();
		heap.value = list.remove(0);
		heap.leftChild = buildHeap(list.subList(0, list.size()/2));
		heap.rightChild = buildHeap(list.subList(list.size()/2, list.size()));
		return heap;
	}
	
	private static Heap extractMinFromHeap(Heap heap) {
		if(heap == null) {
			return null;
		}
		Heap rightMin = extractMinFromHeap(heap.rightChild);
		if(rightMin != null) {
			if(rightMin.value != null) {
				return rightMin;
			} else {
				heap.rightChild = null;
			}
		}
		Heap leftMin = extractMinFromHeap(heap.leftChild);
		if(leftMin != null) {
			if(leftMin.value != null) {
				return leftMin;
			} else {
				heap.leftChild = null;
			}
		}
		return heap;
	}

	private static Heap addToHeap(Heap heap, Float value) {
		if(heap.value != null && heap.value < value) {
			Heap newHeap = new Heap();
			newHeap.rightChild = heap;
			return newHeap;
		}
		if(heap.rightChild != null && heap.rightChild.value != null && heap.rightChild.value > value) {
			addToHeap(heap.rightChild, value);
		} else if(heap.leftChild != null && heap.leftChild.value != null && heap.leftChild.value > value) {
			addToHeap(heap.leftChild, value);
		} else {
			if(heap.rightChild == null) {
				heap.rightChild = new Heap();
				heap.rightChild.value = value;
			} else if (heap.leftChild == null) {
				heap.leftChild = new Heap();
				heap.leftChild.value = value;
			}
		}
		return heap;
	}
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	// sum an array of floats sequentially - high rounding error
	public static float seqAdd(float[] a) {
		float ret = 0;
		
		for (int i = 0; i < a.length; i++)
			ret += a[i];
		
		return ret;
	}

	// sort an array of floats and then sum sequentially - medium rounding error
	public static float sortAdd(float[] a) {
		Arrays.sort(a);
		return seqAdd(a);
	}

	// scan linearly through an array for two minimum values,
	// remove them, and put their sum back in the array. repeat.
	// minimized rounding error
	public static float min2ScanAdd(float[] a) {
		int min1, min2;
		float tmp;
		
		if (a.length == 0) return 0;
		
		for (int i = 0, end = a.length; i < a.length - 1; i++, end--) {
			
			if (a[0] < a[1]) { min1 = 0; min2 = 1; }	// initialize
			else { min1 = 1; min2 = 0; }
			
			for (int j = 2; j < end; j++) {		// find two min indices
				if (a[min1] > a[j]) { min2 = min1; min1 = j; }
				else if (a[min2] > a[j]) { min2 = j; }
			}
			
			tmp = a[min1] + a[min2];	// add together
			if (min1<min2) {			// put into first slot of array
				a[min1] = tmp;			// fill second slot from end of array
				a[min2] = a[end-1];
			}
			else {
				a[min2] = tmp;
				a[min1] = a[end-1];
			}
		}
		
		return a[0];
	}

	// read floats from a Scanner
	// returns an array of the floats read
	private static float[] getFloats(Scanner s) {
		ArrayList<Float> a = new ArrayList<Float>();

		while (s.hasNextFloat()) {
			float f = s.nextFloat();
			if (f >= 0)
				a.add(f);
		}
		return toFloatArray(a);
	}

	// copies an ArrayList to an array
	private static float[] toFloatArray(ArrayList<Float> a) {
		float[] ret = new float[a.size()];
		for(int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the adding algorithm to use ([h]eap, [m]in2scan, se[q], [s]ort): ");
		char algo = s.next().charAt(0);

		System.out.printf("Enter the non-negative floats that you would like summed, followed by a non-numeric input: ");
		float[] values = getFloats(s);
		float sum = 0;

		s.close();

		if (values.length == 0) {
			System.out.println("You must enter at least one value");
			System.exit(0);
		}
		else if (values.length == 1) {
			System.out.println("Sum is " + values[0]);
			System.exit(0);
			
		}
		
		switch (algo) {
		case 'h':
			sum = heapAdd(values);
			break;
		case 'm':
			sum = min2ScanAdd(values);
			break;
		case 'q':
			sum = seqAdd(values);
			break;
		case 's':
			sum = sortAdd(values);
			break;
		default:
			System.out.println("Invalid adding algorithm");
			System.exit(0);
			break;
		}

		System.out.printf("Sum is %f\n", sum);		

	}

}
