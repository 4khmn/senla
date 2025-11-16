public class Task4 {

    public static void main(String[] args) {
        int randomNumber = 100 + (new java.util.Random().nextInt(900));

        System.out.println("Случайное число: " + randomNumber);

        int hundreds = randomNumber/100;
        int tens = (randomNumber/10)%10;
        int units = randomNumber%10;

        int sum = hundreds+tens+units;
        System.out.println("Сумма цифр: " + sum);
    }
}
