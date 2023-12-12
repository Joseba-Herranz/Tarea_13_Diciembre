package proyecto_Hibernate;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MenuHandler menuHandler = new MenuHandler();

        while (true) {
            menuHandler.showMenu();
            int opcion = sc.nextInt();
            menuHandler.handleOption(opcion);
        }
        
    }
}