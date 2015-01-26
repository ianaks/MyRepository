import java.util.ArrayList;
import java.util.List;




public class Test {

    private Integer a[] = {10,9,8,7,6,5,4,3,2,1};
    
    private void sortBubble() {
        System.out.println("N: " + a.length);
        int o = 0;
        for (int i = 0; i < a.length; ++i) {
            boolean sorted = true;
            for (int j = 1; j < a.length - i; ++j, ++o) {
                if (a[j - 1] > a[j]) {
                    int t = a[j - 1];
                    a[j - 1] = a[j];
                    a[j] = t;
                    sorted = false;
                }
            }
            if (sorted) {
                break;
            }
        }
        
        System.out.println("O(n^2): " + o);
    }
    
    private void sortSelection() {        
        System.out.println("N: " + a.length);
        int o = 0;
        int min = 0;
        for (int i = 0; i < a.length; ++i) {
            min = i;
            for (int j = i; j < a.length; ++j, ++o) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            if (min != i) {
                int t = a[i];
                a[i] = a[min];
                a[min] = t;
            }
        }
        
        System.out.println("O(n^2): " + o);
    }
    
    private void sortInsertion() {        
        System.out.println("N: " + a.length);
        int o = 0;
        for (int i = 1; i < a.length; ++i) {
            int j = i;
            while (j > 0 && a[j - 1] > a[j]) {
                int t = a[j - 1];
                a[j - 1] = a[j];
                a[j] = t;            
                --j;
                ++o;
            }
        }
        
        System.out.println("O(n^2): " + o);
    }
    
    private void sortQuick() {
        //shuffle();
        System.out.println("N: " + a.length);
        quick(a, 0, a.length - 1);        
    }
    
    private void quick(Integer[] a, int i, int j) {
        if (j <= i)
            return;
        
        int p = partition(a, i, j);
        
        quick(a, i, p - 1);
        quick(a, p + 1, j);
    }

    private int partition(Integer[] a, int i, int j) {
        int l = i + 1;
        int g = j;
        while (true) {
            
            while (a[l] < a[i] && l < j)
                ++l;
            
            while (a[g] > a[i] && g > i)
                --g;
            
            if (g <= l) {
                break;
            }
            
            swap(l, g);
        }
        
        swap(i, g);
        return g;
    }

    private void swap(int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private void shuffle() {
        Integer [] a2 = {1,2,8,4,7,5,10,3,9,6};
        a = a2;
        System.out.println("Shuffle:");
        print();
    }
    
    private void sortMerge() {
        //shuffle();
        System.out.println("N: " + a.length);
        Integer[] buffer = new Integer[a.length];
        mergeSort(a, buffer, 0, a.length);
    }

    private void mergeSort(Integer[] a, Integer[] buffer, int i, int j) {
        if (j - i > 1) {
            int middle = i + (j - i) / 2;
            mergeSort(a, buffer, i, middle);
            mergeSort(a, buffer, middle, j);
            
            merge(a, buffer, i, middle, j);
        }
    }

    private void merge(Integer[] a, Integer[] buffer, int start, int middle, int end) {
        for (int k = start; k < end; k++) {
            buffer[k] = a[k];
        }
        
        int start1 = start;
        int start2 = middle;
        for (int i = start; i < end; ++i) {
            if (start1 >= middle) {
                a[i] = buffer[start2++];
            }
            else if (start2 >= end) {
                a[i] = buffer[start1++];
            }
            else if (buffer[start1] < buffer[start2]) {
                a[i] = buffer[start1++];
            } else {
                a[i] = buffer[start2++];
            }
        }
    }
    
    private void sortShell() {
        System.out.println("N: " + a.length);
        int h = 1;
        while (h < a.length / 3)
            h = h *3 + 1;
        
        while (h >= 1) {
            for (int i = h; i < a.length; ++i) {
                for (int j = i; j >= h && a[j] < a[j - h]; j-= h) {
                    int t = a[j];
                    a[j] = a[j - h];
                    a[j - h] = t;
                }
            }
            h /= 3;
        }
    }
    
    private void sortHeap() {
        System.out.println("N: " + a.length);
        buildHeap();
        heapSort();
    }

    private void buildHeap() {
        for (int i = a.length / 2; i >= 1; --i) {
            int t = a[0];
            a[0] = a[i];
            a[i] = t;
            sink(0, a.length);
        }
    }
    
    private void heapSort() {
        int N = a.length;
        while (N >= 1) {
            int t = a[0];
            a[0] = a[N - 1];
            a[N - 1] = t;
            sink(0, --N);
        }
    }
    
    private void sink(int k, int N) {
        if (k < N) {
            int j = k * 2;
            if (j < N - 1 && a[j + 1] > a[j])
                ++j;
            if (j < N && a[k] < a[j]) {
                int t = a[j];
                a[j] = a[k];
                a[k] = t;
                sink(j, N);
            }
        }
    }
    
    private void sortBucket() {
        System.out.println("N: " + a.length);
        buildHeap();
        heapSort();
    }

    private void print() {
        System.out.println();
        for (int i = 0; i < a.length; ++i) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }

    public static void main(String [] args) {
        // bubble
        System.out.println("===================================");
        System.out.println("Bubble sort:");
        System.out.println("===================================");
        Test test = new Test();
        test.print();
        test.sortBubble();
        test.print();
        System.out.println("===================================");
        
        // selection
        System.out.println();
        System.out.println("===================================");
        System.out.println("Selection sort:");
        System.out.println("===================================");
        Test test2 = new Test();
        test2.print();
        test2.sortSelection();
        test2.print();
        System.out.println("===================================");
        
        // insertion
        System.out.println();
        System.out.println("===================================");
        System.out.println("Insertion sort:");
        System.out.println("===================================");
        Test test3 = new Test();
        test3.print();
        test3.sortInsertion();
        test3.print();
        System.out.println("===================================");
        
        // quick
        System.out.println();
        System.out.println("===================================");
        System.out.println("Quick sort:");
        System.out.println("===================================");
        Test test4 = new Test();
        test4.print();
        test4.sortQuick();
        test4.print();
        System.out.println("===================================");
        
        // Shell
        System.out.println();
        System.out.println("===================================");
        System.out.println("Shell sort:");
        System.out.println("===================================");
        Test test6 = new Test();
        test6.print();
        test6.sortShell();
        test6.print();
        System.out.println("===================================");
        
        // Heap
        System.out.println();
        System.out.println("===================================");
        System.out.println("Heap sort:");
        System.out.println("===================================");
        Test test7 = new Test();
        test7.print();
        test7.sortHeap();
        test7.print();
        System.out.println("===================================");
        
        // Bucket
        System.out.println();
        System.out.println("===================================");
        System.out.println("Bucket sort:");
        System.out.println("===================================");
        Test test8 = new Test();
        test8.print();
        test8.sortBucket();
        test8.print();
        System.out.println("===================================");
    }

}
