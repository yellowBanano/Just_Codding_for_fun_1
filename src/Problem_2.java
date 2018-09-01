import java.util.Arrays;

public class Problem_2 {

    public interface LuckyTicket {
        boolean isLucky(String number);
        long countLucky(long min, long max);
        long countLucky(String min, String max);
    }

    public static class LuckyTicketImpl implements LuckyTicket {
        @Override
        public boolean isLucky(String number) {
            if (!isTicketOk(number)) {
                return false;
            }
            String firstDigits = number.substring(0, number.length() / 2);
            String lastDigits = number.substring(number.length() / 2);
            int sumFirstDigits = sumOfDigitsInString(firstDigits);
            int sumLastDigits = sumOfDigitsInString(lastDigits);
            return sumFirstDigits == sumLastDigits;
        }

        public boolean isLucky(Long number) {
            return this.isLucky(number.toString());
        }

        @Override
        public long countLucky(long min, long max) {
            final int MAX_TICKET_LENGTH = 12;
            long count = 0;
            for (long current = min; current <= max; current++) {
                if (Long.toString(current).length() == MAX_TICKET_LENGTH && this.isLucky(current)) {
                    count++;
                }
            }
            return count;
        }

        @Override
        public long countLucky(String min, String max) {
            long minLong = Long.valueOf(min);
            long maxLong = Long.valueOf(max);
            final int MIN_TICKET_LENGTH = 2;
            final int MAX_TICKET_LENGTH = 30;
            long count = 0;
            for (long current = minLong; current <= maxLong; current++) {
                if (count >= Long.MAX_VALUE) {
                    return count;
                }
                int ticketNumberLength = Long.toString(current).length();
                if (
                        MIN_TICKET_LENGTH <= ticketNumberLength
                        && ticketNumberLength <= MAX_TICKET_LENGTH
                        && this.isLucky(current)
                ) {
                    count++;
                }
            }
            return count;
        }

        private int sumOfDigitsInString(String number) {
            return Arrays.stream(number.split(""))
                    .mapToInt(Integer::valueOf)
                    .sum();
        }

        private boolean isTicketOk(String number) {
            return number.length() % 2 == 0;
        }
    }

    public static void main(String[] args) {
        LuckyTicket luckyTicket = new LuckyTicketImpl();

        System.out.println(luckyTicket.isLucky("123600"));
        System.out.println(luckyTicket.isLucky("123456"));

        System.out.println(luckyTicket.countLucky(123456123456L, 123456123465L));

        System.out.println(luckyTicket.countLucky("1234567", "12378532"));
    }
}
