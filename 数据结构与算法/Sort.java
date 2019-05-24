import java.util.*;

class Sort {

    // o(n^2)
    private static int[] bubbleSort(int[] nums) {
        int tmp;
        for (int i = nums.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (nums[j] > nums[j + 1]) {
                    tmp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tmp;
                }
            }
        }

        return nums;
    }

    // o(n^2)
    private static int[] selectionSort(int[] nums) {
        int tmp;
        for (int i = nums.length; i > 1; i--) {
            int maxIndex = 0;
            int max = nums[0];
            for (int j = 1; j < i; j++) {
                if (nums[j] > max) {
                    maxIndex = j;
                    max = nums[maxIndex];
                }
            }
            if (maxIndex != i - 1) {
                tmp = nums[maxIndex];
                nums[maxIndex] = nums[i - 1];
                nums[i - 1] = tmp;
            }
        }

        return nums;
    }

    // o(n^2) 链表稍快一点
    private static int[] insertionSort(int[] nums, int start, int length) {
        int tmp;
        for (int i = start + 1; i < start + length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] < nums[j]) {
                    tmp = nums[i];
                    for (int k = i; k > j; k--) {
                        nums[k] = nums[k - 1];
                    }
                    nums[j] = tmp;
                }
            }
        }

        return nums;
    }

    // o(n * logn)
    private static void mergeSort(int[] nums, int start, int length) {
        if (length == 1) {
            return;
        }
        if (length == 2) {
            if (nums[start] > nums[start + 1]) {
                int tmp = nums[start];
                nums[start] = nums[start + 1];
                nums[start + 1] = tmp;
            }
            return;
        }
        int halfLength = length / 2;
        mergeSort(nums, start, halfLength);
        mergeSort(nums, start + halfLength, length - halfLength);
        int bucket1Top = start;
        int bucket2Top = start + halfLength;
        int[] tmp = new int[length];
        for (int i = 0; i < length; i++) {
            if (nums[bucket1Top] > nums[bucket2Top]) {
                tmp[i] = nums[bucket2Top];
                if (bucket2Top < start + length - 1) {
                    bucket2Top++;
                }
            } else {
                tmp[i] = nums[bucket1Top];
                if (bucket1Top < start + halfLength - 1) {
                    bucket1Top++;
                }
            }
        }
        for (int i = 0; i < length; i++) {
            nums[start + i] = tmp[i];
        }
    }

    // o(n * logn)
    private static void quickSort(int[] nums, int start, int length) {
        if (length < 2) {
            return;
        }
        if (length == 2) {
            if (nums[start] > nums[start + 1]) {
                int tmp = nums[start];
                nums[start] = nums[start + 1];
                nums[start + 1] = tmp;
            }
            return;
        }

        int i = start;
        int j = start + length - 1;
        int baseNum = nums[start];
        int tmp;
        while (true) {
            while (i != j && nums[j] >= baseNum) {
                j--;
            }
            if (i == j) {
                break;
            }
            while (i != j && nums[i] <= baseNum) {
                i++;
            }
            if (i == j) {
                break;
            } else {
                tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
            }
        }
        if (i != start) {
            tmp = nums[start];
            nums[start] = nums[i];
            nums[i] = tmp;
        }

        quickSort(nums, start, i - start);
        quickSort(nums, i + 1, length - 1 - i + start);
    }

    // o(n * logn)
    private static int[] shellSort(int[] nums, int step) {
        if (step < 1) {
            throw new IllegalArgumentException("step 最小为 1");
        }
        if (step == 1) {
            return insertionSort(nums, 0, nums.length);
        }
        int[] subSeq = new int[nums.length - nums.length / 2];
        int subSeqSize = 0;
        while (step > 1) {
            for (int start = 0; start < step; start++) {
                subSeqSize = 0;
                for (int i = start; i < nums.length; i += step) {
                    subSeq[subSeqSize++] = nums[i];
                }
                insertionSort(subSeq, 0, subSeqSize);
                subSeqSize = 0;
                for (int i = start; i < nums.length; i += step) {
                    nums[i] = subSeq[subSeqSize++];
                }
            }
            step = (step + 1) / 2 - 1;
        }
        return insertionSort(nums, 0, nums.length);
    }

    private static int[] prepareNums() {
        return new Random().ints(100001).toArray();
    }

    private static boolean isSorted(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] > nums[i + 1]) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        long start, end;
        
        start = System.currentTimeMillis();
        int[] selectionSorted = selectionSort(prepareNums());
        end = System.currentTimeMillis();
        System.out.println("sorted: " + isSorted(selectionSorted));
        System.out.println("selection sort time: " + (end - start));
        System.out.println();
        
        start = System.currentTimeMillis();
        int[] nums = prepareNums();
        int[] insertionSorted = insertionSort(nums, 0, nums.length);
        end = System.currentTimeMillis();
        System.out.println("sorted: " + isSorted(insertionSorted));
        System.out.println("insertion sort time: " + (end - start));
        System.out.println();

        start = System.currentTimeMillis();
        int[] bubbleSorted = bubbleSort(prepareNums());
        end = System.currentTimeMillis();
        System.out.println("sorted: " + isSorted(bubbleSorted));
        System.out.println("bubble sort time: " + (end - start));
        System.out.println();
        
        start = System.currentTimeMillis();
        nums = prepareNums();
        mergeSort(nums, 0, nums.length);
        end = System.currentTimeMillis();
        System.out.println("sorted: " + isSorted(nums));
        System.out.println("merge sort time: " + (end - start));
        System.out.println();

        start = System.currentTimeMillis();
        nums = prepareNums();
        quickSort(nums, 0, nums.length);
        end = System.currentTimeMillis();
        System.out.println("sorted: " + isSorted(nums));
        System.out.println("quick sort time: " + (end - start));
        System.out.println();

        int step = 32767;
        start = System.currentTimeMillis();
        nums = prepareNums();
        int[] shellSorted = shellSort(nums, step);
        end = System.currentTimeMillis();
        System.out.println("sorted: " + isSorted(shellSorted));
        System.out.println("shell sort time: " + (end - start) + " step: " + step);
        System.out.println();
    }
}