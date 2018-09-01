import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem_3 {

    public interface Summands {
        long[] maxProduct(long n);

        long[][] allMaxProduct(long n);

        long[] maxPairProduct(long n);
    }

    public static class SummandsImpl implements Summands {

        @Override
        public long[] maxProduct(long n) {
            List<long[]> allCombinations = splitter((int) n);
            List<Integer> indexMaxCombination = findIndexMaxCombination(allCombinations);
            return reduceNullInArray(allCombinations.get(indexMaxCombination.get(0)));
        }

        @Override
        public long[][] allMaxProduct(long n) {
            List<long[]> allCombinations = splitter((int) n);
            List<Integer> indexMaxCombination = findIndexMaxCombination(allCombinations);
            long[][] allMaxProduct = new long[indexMaxCombination.size()][];
            for (int i = 0; i < indexMaxCombination.size(); i++) {
                allMaxProduct[i] = reduceNullInArray(allCombinations.get(indexMaxCombination.get(i)).clone());
            }
            return allMaxProduct;
        }

        @Override
        public long[] maxPairProduct(long n) {
            List<long[]> allCombinations = splitter((int) n);
            int indexMaxPairedCombination = findIndexMaxPairedCombination(allCombinations);
            return reduceNullInArray(allCombinations.get(indexMaxPairedCombination));
        }
    }

    private static List<long[]> splitter(int n) {
        List<long[]> allCombinations = new ArrayList<>();
        long[] arrayOfCombination = new long[n];

        Arrays.fill(arrayOfCombination, 1);

        boolean firstIteration = true;
        int lastNotNullValue = arrayOfCombination.length - 1;
        while (arrayOfCombination[0] <= n) {
            allCombinations.add(Arrays.copyOf(arrayOfCombination, n));
            for (int pos = 0; pos < lastNotNullValue; pos++) {
                if(!firstIteration && pos == 0) {
                    continue;
                }
                arrayOfCombination[pos]++;
                arrayOfCombination[lastNotNullValue]--;
                lastNotNullValue--;
                allCombinations.add(Arrays.copyOf(arrayOfCombination, n));
                if(pos >= lastNotNullValue - 1) {
                    for (int i = 0; i < lastNotNullValue; i++) {
                        if (arrayOfCombination[i] < arrayOfCombination[0]) {
                            pos = 0;
                        }
                    }
                }
            }
            firstIteration = false;
            arrayOfCombination[0]++;

            Arrays.fill(arrayOfCombination, 1, arrayOfCombination.length, 0);

            long rest = n - arrayOfCombination[0];
            for (int pos = 1; rest > 0; pos++, rest--) {
                arrayOfCombination[pos] = 1;
                lastNotNullValue = pos;
            }
        }
        return allCombinations;
    }

    private static List<Integer> findIndexMaxCombination(List<long[]> allCombination) {
        List<Integer> indexOfMaxCombination = new ArrayList<>();
        long[] multiplyCombinations = allCombination.stream().mapToLong(combination -> Arrays.stream(combination).reduce(1, (a, b) -> b != 0 ? a * b : a + b)).toArray();
        long maxCombination = Arrays.stream(multiplyCombinations).max().getAsLong();
        for (int i = 0; i < multiplyCombinations.length; i++) {
            if (multiplyCombinations[i] == maxCombination) {
                indexOfMaxCombination.add(i);
            }
        }
        return indexOfMaxCombination;
    }

    private static int findIndexMaxPairedCombination(List<long[]> allCombination) {

        /*

        [1, 2, 3, 4, ... k]

        1* (2+3+4+...+k)+
        + 2* (3+4+...+k)+
        + 3*(4+...+k)+
        ....
        + (k-2) *(k-1+k)+
        + *(k-1)*k

         */

        long max = 0;
        int maxIndex = 0;
        for (int combinationIndex = 0; combinationIndex < allCombination.size(); combinationIndex++) {
            long[] combination = allCombination.get(combinationIndex);
            long sumPair = 0;
            for (int i = 0; i < combination.length - 1; i++) {
                long sum = 0;
                for (int j = i + 1; j < combination.length; j++) {
                    sum += combination[j];
                }
                sumPair += combination[i] * sum;
            }
            if (sumPair > max) {
                max = sumPair;
                maxIndex = combinationIndex;
            }
        }
        return maxIndex;
    }

    private static long[] reduceNullInArray(long[] maxCombination) {
        for (int i = 0; i < maxCombination.length; i++) {
            if (maxCombination[i] == 0) {
                return Arrays.copyOfRange(maxCombination, 0, i);
            }
        }
        return maxCombination;
    }

    public static void main(String[] args) {
        Summands summands = new SummandsImpl();

        System.out.println(Arrays.toString(summands.maxProduct(6)));
        System.out.println(Arrays.toString(summands.maxProduct(7)));
        System.out.println(Arrays.toString(summands.maxProduct(9)));

        System.out.println(Arrays.deepToString(summands.allMaxProduct(6)));
        System.out.println(Arrays.deepToString(summands.allMaxProduct(7)));
        System.out.println(Arrays.deepToString(summands.allMaxProduct(9)));

        System.out.println(Arrays.toString(summands.maxPairProduct(6)));
    }
}
