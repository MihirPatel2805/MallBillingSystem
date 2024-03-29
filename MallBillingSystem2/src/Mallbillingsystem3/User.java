/**
 * Author: Mihir And Pratham
 * Date: 2023-09-15
 * Description: Main class for the Mall Billing System.
 * Version: 1.0
 */

package Mallbillingsystem3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class User extends Product {
    // Constructors
    User() {
        super(0, null, null, 0);
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mallbilling", "root", "");
        } catch (Exception e) {
            // Handle connection exception
            System.out.println("Connection not established.");
        }
    }

    // Objects
    Connection con;
    boolean flag = false;
    Scanner sc = new Scanner(System.in);
    Map<String, Product> cartInfo = new Hashtable();
    Map<String, Integer> cart = new Hashtable();

    // Methods
    // 1) scanItem
    // 2) viewItem
    // 3) viewItemMenu
    // 4) removeItemFromCart
    // 5) addItemFromCart

    // ScanItems method
    void scanItem() throws Exception {
        System.out.print("Enter item name for Scan: ");

        if (flag) {
            sc.nextLine();
        }
        flag = true;

        try {
            String scanItemName = sc.nextLine().toLowerCase();;
            System.out.print("Enter Quantity: ");
            int quantity = sc.nextInt();

            if (quantity <= 0) {
                throw new NegativeInput("Quantity Can't Be Negative");
            }

            if (isItemInStock(con, scanItemName, quantity)) {
                Product product = getProduct(con, scanItemName);

                if (product != null) {

                    if (cart.containsKey(scanItemName)) {
                        cart.put(scanItemName, cart.get(scanItemName) + quantity);
                    } else {
                        cart.put(scanItemName, quantity);
                        cartInfo.put(scanItemName, product);
                    }

                    System.out.println("Added Item..");
                    updateStock(con, scanItemName, quantity);
                } else {
                    System.out.println("Invalid Item");
                }
            } else {
                System.out.println("Item out of stock");
            }
        } catch (Exception e) {
            // Handle exceptions
            System.out.println(e);
        }
    }

    // viewItem method
    void viewItem() throws Exception {
        int choice=0;
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty");
            return;
        }

        do {
            System.out.println("Items            Quantity");
            for (String itemName : cart.keySet()) {
                System.out.print(itemName);
                for (int i = 1; i <= (18 - itemName.length()); i++) {
                    System.out.print(" ");
                }
                System.out.println(cart.get(itemName));
            }
            try {
                viewItemMenu();
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                // TODO: handle exception
                System.out.println("Invalid Input");
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            
            switch (choice) {
                case 1:
                    removeItemFromCart(con);
                    break;
                case 2:
                    addItemFromCart(con);
                default:
                    break;
            }
        } while (choice != 3);
    }

    // viewItemMenu;
    void viewItemMenu() {
        System.out.println("1) Remove item:");
        System.out.println("2) Add item:");
        System.out.println("3) EXIT");
    }

    // removeItemFromCart method
    void removeItemFromCart(Connection con) throws Exception {
        try {
            Thread.currentThread().setName("view");
            System.out.println("Enter Item Name: ");
            sc.nextLine();
            String item_name = sc.nextLine().toLowerCase();
            if (cart.containsKey(item_name)) {

                System.out.println("Enter Quantity: ");
                int quantity = sc.nextInt();
                if (quantity <= 0) {
                    throw new NegativeInput("Quantity Can't Be Negative");
                }
                if (cart.get(item_name) >= quantity) {
                    cart.put(item_name, cart.get(item_name) - quantity);
                    if (cart.get(item_name) == 0) {
                        cart.remove(item_name);
                    }
                    updateStock(con, item_name, quantity);
                    System.out.println("Removed..");
                } else {
                    System.out.println("That Much Quantity Is Not Present In Cart");
                }
            } else {
                System.out.println("No Such Item Present In Cart");
            }
        } catch (Exception e) {
            // Handle exceptions
            System.out.println(e.getMessage());
        }
    }

    // addItemFromCart method
    void addItemFromCart(Connection con) throws Exception {
        try {
            Thread.currentThread().setName("scan");
            System.out.println("Enter Item Name: ");
            sc.nextLine();
            String item_name = sc.nextLine().toLowerCase();
            if (cart.containsKey(item_name)) {
                System.out.println("Enter Quantity: ");
                int quantity = sc.nextInt();
                if (quantity <= 0) {
                    throw new NegativeInput("Quantity Can't Be Negative");
                }
                if (isItemInStock(con, item_name, quantity)) {
                    cart.put(item_name, cart.get(item_name) + quantity);
                    updateStock(con, item_name, quantity);
                    System.out.println("Added..");
                } else {
                    System.out.println("Out of stock");
                }
            } else {
                System.out.println("No Such Item Present In Cart");
            }
        } catch (Exception e) {
            // Handle exceptions
            System.out.println(e.getMessage());
        }
    }

    // showBill() method
    void showBill() {
        try {
            if (cart.isEmpty()) {
                System.out.println("Your Cart Is Empty");
                return;
            }
        } catch (Exception e) {
            // Handle exceptions
            return;
        }

        Double totalBillPrice = 0d;
        int totalQuantity = 0;
        int srNo = 0;
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("|                                  TOTAL BILL                                     |");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("|     Bill no.:" + Main.billno + "                                              Date:07/10/2023     |");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("| Sr.no |          Items          |   Quantity   |     MRP     |    TotalPrice    |");
        for (String itemName : cart.keySet()) {
            // for sr.no
            System.out.print("|   " + (++srNo) + "   ");
            // for itemname display
            System.out.print("| " + itemName);
            for (int i = 1; i <= (24 - itemName.length()); i++) {
                System.out.print(" ");
            }

            // for quantity display
            int quantity = cart.get(itemName);
            System.out.print("| " + quantity);
            String quantityLength = "";
            quantityLength = quantityLength.valueOf(quantity);
            for (int i = 1; i <= (13 - quantityLength.length()); i++) {
                System.out.print(" ");
            }
            totalQuantity += quantity;
            // for MRP display
            Product p = cartInfo.get(itemName);
            System.out.print("| " + p.price);
            String priceLength = "";
            priceLength = priceLength.valueOf(p.price);
            for (int i = 1; i <= (12 - priceLength.length()); i++) {
                System.out.print(" ");
            }

            // for TotalPrice diaplay
            Double totalItemPrice = quantity * p.price;
            System.out.print("| " + totalItemPrice);
            String totalPriceLength = "";
            totalPriceLength = totalPriceLength.valueOf(totalItemPrice);
            for (int i = 1; i <= (17 - totalPriceLength.length()); i++) {
                System.out.print(" ");
            }
            totalBillPrice += totalItemPrice;
            System.out.println("|");
        }

        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println("|        Total Amount=             " + totalQuantity + "                            " + totalBillPrice
                + "             |");
        System.out.println("+---------------------------------------------------------------------------------+");
        System.out.println();
        System.out.println("                              THANK YOU FOR SHOPPING                               ");
    }

    boolean generateBill() throws IOException {
        if (cart.isEmpty()) {
            System.out.println("NO item in cart");
            return false;
        }
        Double totalBillPrice = 0d;
        int totalQuantity = 0;
        int srNo = 0;
        BufferedWriter bw = new BufferedWriter(new FileWriter("D://Group_project_java//Bills//Billno."
                + Main.billno + ".txt"));
        bw.write("+---------------------------------------------------------------------------------+");
        bw.newLine();
        bw.write("|                                  TOTAL BILL                                     |");
        bw.newLine();
        bw.write("+---------------------------------------------------------------------------------+");
        bw.newLine();
        bw.write("|     Bill no.:" + Main.billno + "                                              Date:12/05/2023     |");
        bw.newLine();
        bw.write("+---------------------------------------------------------------------------------+");
        bw.newLine();
        bw.write("| Sr.no |          Items          |   Quantity   |     MRP     |    TotalPrice    |");
        bw.newLine();
        for (String itemName : cart.keySet()) {
            // for sr.no and itemname display
            bw.write("|   " + (++srNo) + "   " + "| " + itemName);

            for (int i = 1; i <= (24 - itemName.length()); i++) {
                bw.write(" ");
            }

            // for quantity display
            int quantity = cart.get(itemName);
            bw.write("| " + quantity);
            String quantityLength = "";
            quantityLength = quantityLength.valueOf(quantity);
            for (int i = 1; i <= (13 - quantityLength.length()); i++) {
                bw.write(" ");
            }
            totalQuantity += quantity;
            // for MRP display
            Product p = cartInfo.get(itemName);
            bw.write("| " + p.price);
            String priceLength = "";
            priceLength = priceLength.valueOf(p.price);
            for (int i = 1; i <= (12 - priceLength.length()); i++) {
                bw.write(" ");
            }

            // for TotalPrice diaplay
            Double totalItemPrice = quantity * p.price;
            bw.write("| " + totalItemPrice);
            String totalPriceLength = "";
            totalPriceLength = totalPriceLength.valueOf(totalItemPrice);
            for (int i = 1; i <= (17 - totalPriceLength.length()); i++) {
                bw.write(" ");
            }
            totalBillPrice += totalItemPrice;
            bw.write("|");
            bw.newLine();
        }

        bw.write("+---------------------------------------------------------------------------------+");
        bw.newLine();
        bw.write("|        Total Amount=             " + totalQuantity + "                            " + totalBillPrice
                + "             |");
        bw.newLine();
        bw.write("+---------------------------------------------------------------------------------+");
        bw.newLine();
        bw.newLine();
        bw.write("                              THANK YOU FOR SHOPPING                               ");
        bw.newLine();
        bw.close();
        System.out.println("Bill generated...");
        Main.billno++;
        return true;
    }
}
