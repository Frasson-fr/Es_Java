import java.util.Objects;

public class TestAutoboxing {

    public static boolean confrontaBatteriaSbagliato(Integer b1, Integer b2) {
        return b1 == b2; // Errato per oggetti wrapper
    }

    public static boolean confrontaBatteriaCorretto(Integer b1, Integer b2) {
        return Objects.equals(b1, b2); // Corretto
    }

    public static void eseguiDimostrazione() {
        System.out.println("--- 1. Valori nel range cache (-128..127): 50 e 50 ---");
        Integer bat1 = 50;
        Integer bat2 = 50;
        System.out.println("  Confronto con '==':             " + confrontaBatteriaSbagliato(bat1, bat2));
        System.out.println("  Confronto con 'Objects.equals': " + confrontaBatteriaCorretto(bat1, bat2));

        System.out.println("\n--- 2. Valori fuori dal range cache: 200 e 200 ---");
        Integer bat3 = 200;
        Integer bat4 = 200;
        System.out.println("  Confronto con '==':             " + confrontaBatteriaSbagliato(bat3, bat4));
        System.out.println("  Confronto con 'Objects.equals': " + confrontaBatteriaCorretto(bat3, bat4));
    }
}