import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Problem_1 {

    public interface BestFit {
        String fitPlusMinus(String digits, long expectedResult);

        String fit(String digits, long expectedResult);

        String fitBraces(String digits, long expectedResult);
    }

    public static class BestFitImpl implements BestFit {

        @Override
        public String fitPlusMinus(String digits, long expectedResult) {
            String[] arrayOfDigits = digits.split("");
            return bruteForce(expectedResult, 1, arrayOfDigits, "", "+", "-");
        }

        @Override
        public String fit(String digits, long expectedResult) {
            String[] arrayOfDigits = digits.split("");
            return bruteForce(expectedResult, 1, arrayOfDigits, "", "+", "-", "*", "/");
        }

        @Override
        public String fitBraces(String digits, long expectedResult) {
            return null;
        }

        private String bruteForce(long expectedResult, int positionOfDigit, String[] arrayOfDigits, String... operations) {
            for (String operation : operations) {
                arrayOfDigits[positionOfDigit] = operation + (arrayOfDigits[positionOfDigit].length() > 1 ? arrayOfDigits[positionOfDigit].substring(1) : arrayOfDigits[positionOfDigit]);
                if (positionOfDigit < arrayOfDigits.length - 1) {
                    String returnResult = bruteForce(expectedResult, positionOfDigit + 1, arrayOfDigits, operations);
                    if (!returnResult.equals("")) {
                        return returnResult;
                    }
                } else {
                    String bakedExpression = String.join("", arrayOfDigits);
                    Object result = evaluateExpressionString(bakedExpression);
                    if (isFitResult(result, expectedResult)) {
                        return bakedExpression;
                    }
                }
            }
            return "";
        }

        private Object evaluateExpressionString(String expression) {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            Object result = null;
            try {
                result = engine.eval(expression);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
            return result;
        }

        private boolean isFitResult(Object computedResult, long expectedResult) {
            return Long.valueOf(computedResult.toString()).equals(expectedResult);
        }
    }

    public static void main(String[] args) {

        BestFit bestFit = new BestFitImpl();

        System.out.println(bestFit.fitPlusMinus("123", 9));
        System.out.println(bestFit.fitPlusMinus("222", 11));

        System.out.println(bestFit.fit("222", 11));
    }
}
