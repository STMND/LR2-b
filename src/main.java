//Вариант 3

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

class ParallelSearchTask implements Callable<Integer> {
    private final int[] array;
    private final int target;
    private final int start;
    private final int end;

    public ParallelSearchTask(int[] array, int target, int start, int end) {
        this.array = array;
        this.target = target;
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer call() throws Exception {
        for (int i = start; i < end; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
}

public class main {
    public static void main(String[] args) {
        int[] array;
        array = new int[100];
        for (int i=0;i<100;i++) {
            array[i]=i*2;
        }

        int target = 82;
        int numThreads = Runtime.getRuntime().availableProcessors(); // Почему бы не ограничить ограничить
                                                                    // количество потоков количеством потоков ядер процессора
        
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(numThreads, 4)); // Минимум 4 потока

        int chunkSize = array.length / numThreads;
        int result = -1;

        try {
            for (int i = 0; i < numThreads; i++) {
                int start = i * chunkSize; // Начало отрезка для пула - номер пула умножить на длину отрезка
                int end = (i == numThreads - 1) ? array.length : (i + 1) * chunkSize; // Поиск конца отрезка

                Callable<Integer> task = new ParallelSearchTask(array, target, start, end);
                Future<Integer> future = executor.submit(task);

                int partialResult = future.get();
                if (partialResult != -1) {
                    result = partialResult;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        if (result != -1) {
            System.out.println("Element found: " + result);
        } else {
            System.out.println("Element not found:");
        }
    }
}
