import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class Problem_1 {

    public interface BestFit {
        String fitPlusMinus(String digits, long expectedResult);

        String fit(String digits, long expectedResult);

        String fitBraces(String digits, long expectedResult);
    }

    public static class BestFitImpl implements BestFit {

        @Override
        public String fitPlusMinus(String digits, long expectedResult) {
            if (Double.valueOf(digits) < 10) {
                return "Sry, I won`t do that";
            }
            String[] arrayOfDigits = digits.split("");
            return bruteForce(expectedResult, 1, arrayOfDigits, false, "", "+", "-");
        }

        @Override
        public String fit(String digits, long expectedResult) {
            if (Double.valueOf(digits) < 10) {
                return "Sry, I won`t do that";
            }
            String[] arrayOfDigits = digits.split("");
            return bruteForce(expectedResult, 1, arrayOfDigits, false,"", "+", "-", "*", "/");
        }

        @Override
        public String fitBraces(String digits, long expectedResult) {
            if (Double.valueOf(digits) < 10) {
                return "Sry, I won`t do that";
            }
            String[] arrayOfDigits = digits.split("");
            return bruteForce(expectedResult, 1, arrayOfDigits, true, "", "+", "-", "*", "/");
        }

        private String bruteForce(long expectedResult, int positionOfDigit, String[] arrayOfDigits, boolean isBracesEnabled, String... operations) {
            for (String operation : operations) {
                arrayOfDigits[positionOfDigit] = operation + (arrayOfDigits[positionOfDigit].length() > 1 ? arrayOfDigits[positionOfDigit].substring(1) : arrayOfDigits[positionOfDigit]);
                if (positionOfDigit < arrayOfDigits.length - 1) {
                    String returnResult = bruteForce(expectedResult, positionOfDigit + 1, arrayOfDigits, isBracesEnabled, operations);
                    if (!returnResult.equals("")) {
                        return returnResult;
                    }
                } else {
                    String bakedExpression = String.join("", arrayOfDigits);
                    Object result = evaluateExpressionString(bakedExpression, isBracesEnabled);
                    if (isFitResult(result, expectedResult)) {
                        return bakedExpression;
                    }
                }
            }
            return "";
        }

        private Object evaluateExpressionString(String expression, boolean isBracesEnabled) {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            if (isBracesEnabled) {
                Bracer bracer = new Bracer();
                return bracer.bracify(expression);
            } else {
                Object result = null;
                try {
                    result = engine.eval(expression);
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }

        private boolean isFitResult(Object computedResult, long expectedResult) {
            Double doubleExpectedResult = Double.valueOf(String.valueOf(expectedResult));
            if (computedResult instanceof List) {
                List<Double> castComputedResult = (List<Double>) computedResult;
                for (Double result : castComputedResult) {
                    if (result.equals(doubleExpectedResult)) {
                        return true;
                    }
                }
                return false;
            }
            return Double.valueOf(computedResult.toString()).equals(doubleExpectedResult);
        }
    }

    public static class Bracer {
        public List<Double> bracify(String input) {
            List<Double> result = new ArrayList<>();
            if (input == null || input.length() == 0) {
                return result;
            }

            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);

                if (!isOperator(c)) {
                    continue;
                }

                List<Double> left = bracify(input.substring(0, i));
                List<Double> right = bracify(input.substring(i + 1));

                for (double num1 : left) {
                    for (double num2 : right) {
                        double val = calculate(num1, num2, c);
                        result.add(val);
                    }
                }
            }

            if (result.isEmpty()) {
                result.add(Double.parseDouble(input));
            }

            return result;
        }

        private double calculate(double num1, double num2, char operator) {
            double result = 0;

            switch(operator) {
                case '+' : result = num1 + num2;
                    break;

                case '-' : result = num1 - num2;
                    break;

                case '*' : result = num1 * num2;
                    break;

                case '/' : result = num1 / num2;
                    break;
            }

            return result;
        }

        private boolean isOperator(char operator) {
            return (operator == '+') || (operator == '-') || (operator == '*') || (operator == '/');
        }
    }

    public static void main(String[] args) {

        BestFit bestFit = new BestFitImpl();

        System.out.println(bestFit.fitPlusMinus("123", 9));
        System.out.println(bestFit.fitPlusMinus("222", 11));

        System.out.println(bestFit.fit("222", 11));
        System.out.println(bestFit.fit("321", 8));

        System.out.println(bestFit.fitBraces("321", 9));
    }
}
