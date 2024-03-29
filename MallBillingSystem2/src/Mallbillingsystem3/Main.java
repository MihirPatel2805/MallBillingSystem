/**
 * Author: Kalp
 * Date: 2023-09-20
 * Description: Main class for the Mall Billing System.
 * Version: 1.0
 */

package Mallbillingsystem3;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    
    static Scanner sc = new Scanner(System.in);
    public static int billno = 1;
    
    public static void main(String[] args) throws Exception {
        // Load the JDBC driver for MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        Admin a = new Admin();
        System.out.println("=========== Welcome To Mall Billing System ===========");
        
        boolean adminLoggedIn;
        do {
            adminLoggedIn = a.adminLogin();
        } while (!adminLoggedIn);
        
        if (adminLoggedIn) {
            int choice = 0;
            do {
                mainMenu();
                System.out.print("Enter choice: ");
                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    System.out.println("Invalid input.");
                    continue;
                }
                    
                switch (choice) {
                    case 1:
                        adminFunction();
                        break;
                    case 2:
                        UserFunction();
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Enter a valid choice.");
                        break;
                }
            } while (choice != 3);
        }
    }

    // UserMenu method to show user menu
    static void userMenu() {
        System.out.println("====================");
        System.out.println("1) Scan item");
        System.out.println("2) View item");
        System.out.println("3) Show bill");
        System.out.println("4) Generate bill");
        System.out.println("5) Exit");
        System.out.println("====================");
    }

    // MainMenu method to show main menu
    static void mainMenu() {
        System.out.println("====================");
        System.out.println("1) Admin Function");
        System.out.println("2) User Function");
        System.out.println("3) EXIT");
        System.out.println("====================");
    }

    // UserFunction method
    static void UserFunction() throws Exception {
        int choice = 0;
        while (true) {
            User u1 = new User();
            do { 
                userMenu();
                System.out.print("Enter choice: ");
                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    System.out.println("Enter a valid choice.");
                    continue;
                }
                switch (choice) {
                    case 1:
                        Thread.currentThread().setName("scan");
                        u1.scanItem();
                        break;
                    case 2:
                        Thread.currentThread().setName("view");
                        u1.viewItem();
                        break;
                    case 3:
                        u1.showBill();
                        break;
                    case 4:
                        if (u1.generateBill())
                            return;
                        else
                            continue;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            } while (choice != 5);
        }
    }

    // AdminFunction method
    static void adminFunction() throws Exception {
        int choice = 0;
        Admin admin = new Admin();
        do {
            adminMenu();
            System.out.print("Enter Choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                // Handle invalid input
                System.out.println("Invalid input.");
                continue;
            }   
            switch (choice) {
                case 1:
                    admin.addItem();
                    break;
                case 2:
                    admin.updatePrice();
                    break;
                case 3:
                    admin.refill();
                    break;
                case 4:
                    admin.delete();
                    break;
                case 5:admin.display();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } while (choice != 6);
    }

    // AdminMenu method to show admin menu
    static void adminMenu() {
        System.out.println("====================");
        System.out.println("1) Add items");
        System.out.println("2) Update Price");
        System.out.println("3) Refill Stocks");
        System.out.println("4) Delete items");
        System.out.println("5) Display");
        System.out.println("6) EXIT");
        System.out.println("====================");
    }
}
