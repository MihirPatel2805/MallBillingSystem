/**
 * Author: Foram
 * Date: 2023-10-02
 * Description: Admin class for the Mall Billing System.
 * Version: 1.0
 */

package Mallbillingsystem3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin extends Product {
    Connection con;
    String aname; 
    String email_phone;
    String password = "";

    // Constructor
    Admin() {
        super(0, null, null, 0);
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mallbilling", "root", "");
        } catch (Exception e) {
            // Handle connection exception
            System.out.println("Connection not established.");
        }
    }

    Scanner sc = new Scanner(System.in);

    // Admin login method
    boolean adminLogin() throws Exception {
        System.out.print("Enter Email or Phone number: ");
        email_phone = sc.nextLine();
        
        if (!isAdminLoginValid()) {
            System.out.println("Invalid Email or Phone Number");
            return false;
        }

        for (int i = 0; i < 3; i++) {
            System.out.print("Enter the Password: ");
            password = sc.nextLine();

            if (isAdminLoginValid()) {
                return true;
            } else {
                System.out.println("Incorrect Password");
            }
            
            if (i != 2) {
                System.out.print("Try again... ");
            }
        }

        return false;
    }

    void display() throws SQLException{
        PreparedStatement ps=con.prepareStatement("select * from products");
        ResultSet rs=ps.executeQuery();
        System.out.println("Barcode               Name               Price                Quatity");
        while (rs.next()) {
            
            System.out.print(""+rs.getLong(1));
            System.out.print("           "+rs.getString(2));
            System.out.print("           "+rs.getFloat(3));
            System.out.println("                "+rs.getInt(4));
        }
    }

    // Check if the admin login is valid
    boolean isAdminLoginValid() throws Exception {
        PreparedStatement ps = con.prepareStatement("select aemail, aphone, apass from admin where aemail=? or aphone=?");
        ps.setString(1, email_phone);
        ps.setString(2, email_phone);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            if(password=="")
                return true;
            else if (password.equals(rs.getString(3))) {
                return true;
            }
        }
        
        return false;
    }

    // Add item to the product table
    void addItem() throws SQLException {
        try {
            System.out.print("Enter Product Name: ");
            String name = sc.next().toLowerCase();
            System.out.print("Enter Price: ");
            double price = sc.nextDouble();

            if (price <= 0) {
                System.out.println("Price can't be negative.");
                return;
            }

            System.out.print("Enter Quantity: ");
            int quantity = sc.nextInt();

            if (quantity <= 0) {
                System.out.println("Quantity can't be negative.");
                return;
            }

            sc.nextLine(); // Consume the newline character

            addItemInTable(con, name, price, quantity); // Product class
        } catch (Exception e) {
            // Handle input mismatch
            System.out.println("Input Mismatch.");
            sc.nextLine(); // Consume the newline character
        }
    }

    // Update item price
    void updatePrice() throws SQLException {
        try {
            System.out.print("Enter Product Name: ");
            String name = sc.next().toLowerCase();
            System.out.print("Enter Updated Price: ");
            double updatedPrice = sc.nextDouble();

            if (updatedPrice <= 0) {
                System.out.println("Price can't be negative.");
                return;
            }

            sc.nextLine(); // Consume the newline character

            updateItemPrice(con, name, updatedPrice);
        } catch (Exception e) {
            // Handle input mismatch
            System.out.println("Input Mismatch.");
            sc.nextLine(); // Consume the newline character
        }
    }

    // Refill stock
    void refill() throws Exception {
        try {
            Thread.currentThread().setName("refill");
            System.out.print("Enter Product Name: ");
            String name = sc.next();
            System.out.print("Enter quantity you want to add: ");
            int quantity = sc.nextInt();

            if (quantity <= 0) {
                System.out.println("Quantity can't be negative.");
                return;
            }

            sc.nextLine(); // Consume the newline character

            updateStock(con, name, quantity);
        } catch (Exception e) {
            // Handle input mismatch
            System.out.println("Input Mismatch.");
            sc.nextLine(); // Consume the newline character
        }
    }

    // Delete a product
    void delete() throws SQLException {
        try {
            System.out.print("Enter Product Name: ");
            String name = sc.next();
            removeProduct(con, name);
        } catch (Exception e) {
            // Handle input mismatch
            System.out.println("Input Mismatch.");
        }
    }
}
