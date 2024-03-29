/**
 * Author: Stavan
 * Date: 2023-09-27
 * Description: Product class for the Mall Billing System.
 * Version: 1.0
 */
package Mallbillingsystem3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.CallableStatement;
import java.util.Scanner;

/**
 * Represents a product in the store.
 */
public class Product {
    long barcodeNo;
    String pName;
    Double price;
    int quantity;

    Scanner sc = new Scanner(System.in);

    // Constructor of a product
    public Product(long barcodeNo, String pName, Double price, int quantity) {
        this.barcodeNo = barcodeNo;
        this.pName = pName;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String pName) {
        this.pName = pName;
    }

    public long getBarcodeNo() {
        return barcodeNo;
    }

    public String getpName() {
        return pName;
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets a product by name from the database.
     * 
     *  con   The database connection.
     *  pName The name of the product to retrieve.
     *  A Product object if found, null otherwise.
     */
    Product getProduct(Connection con, String pName) {
        try {
            String query = "select * from products where pname=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, pName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(rs.getLong(1), rs.getString(2), rs.getDouble(3), rs.getInt(4));
            }
        } catch (Exception e) {
            // Handle the exception
        }

        return null;
    }

    /**
     * Checks if a product is in stock.
     * 
     *  con      The database connection.
     *  name     The name of the product to check.
     *  quantity The quantity to check.
     *  True if the product is in stock, false otherwise.
     */
    boolean isItemInStock(Connection con, String name, int quantity) {
        try {
            String query = "select quantity from products where pname=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (quantity > rs.getInt(1))
                    return false;
            }
        } catch (Exception e) {
            // Handle the exception
        }
        return true;
    }

    /**
     * Updates the stock of a product.
     * 
     *  con       The database connection.
     *  name      The name of the product.
     *  quantity1 The quantity to add or subtract.
     *  Exception If an error occurs during the update.
     */
    void updateStock(Connection con, String name, int quantity1) throws Exception {
        con.setAutoCommit(false);
        try {
            PreparedStatement ps = con.prepareStatement("select quantity from products where pname=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                ;
            int dataQuantity = rs.getInt(1);
            CallableStatement cs = con.prepareCall("call stockUpdate(?,?)");
            cs.setString(1, name);
            if (Thread.currentThread().getName().equals("scan")) {
                cs.setInt(2, dataQuantity - quantity1);
            } else if (Thread.currentThread().getName().equals("view") || Thread.currentThread().getName().equals("refill")) {
                cs.setInt(2, dataQuantity + quantity1);
            }
            cs.execute();
            con.commit();
            if (Thread.currentThread().getName().equals("refill"))
                System.out.println("Refilling Done..");
        } catch (SQLException e) {
            // Handle the exception
            System.out.println("No Product exist..");
            System.out.println("Rolling back..");
            con.rollback();
        } catch (Exception e) {
            // Handle the exception
            System.out.println(e);
            System.out.println("Rolling back..");
            con.rollback();
        }
    }

    /**
     * Adds a new product to the database.
     * 
     *  con      The database connection.
     *  name     The name of the product.
     *  price    The price of the product.
     *  quantity The initial quantity of the product.
     *  SQLException If an error occurs during the insertion.
     */
    void addItemInTable(Connection con, String name, Double price, int quantity) throws SQLException {
        con.setAutoCommit(false);
        try {
            PreparedStatement ps = con.prepareStatement("insert into products (pname,price,quantity) values (?,?,?)");

            ps.setString(1, name);

            ps.setDouble(2, price);

            ps.setInt(3, quantity);
            int r = ps.executeUpdate();
            con.commit();
            if (r > 0) {
                System.out.println("Product Added..");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // Handle the exception
            System.out.println("Product Already Exists..");
            System.out.println("Rolling back...");
            con.rollback();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Rolling back...");
            con.rollback();
        }
    }

    /**
     * Updates the price of a product.
     * 
     *  con         The database connection.
     *  name        The name of the product.
     *  updatedPrice The new price of the product.
     *  SQLException If an error occurs during the update.
     */
    void updateItemPrice(Connection con, String name, Double updatedPrice) throws SQLException {
        con.setAutoCommit(false);
        try {
            PreparedStatement ps = con.prepareStatement("update products set price= ? where pname=?");

            ps.setDouble(1, updatedPrice);

            ps.setString(2, name);

            int r = ps.executeUpdate();
            con.commit();
            if (r > 0) {
                System.out.println("Product Updated..");
            } else {
                System.out.println("Item Not Present");
            }
        } catch (Exception e) {
            // Handle the exception
            System.out.println(e);
            System.out.println("Rolling back...");
            con.rollback();
        }
    }

    /**
     * Removes a product from the database.
     * 
     *  con  The database connection.
     *  name The name of the product to remove.
     *  SQLException If an error occurs during the removal.
     */
    void removeProduct(Connection con, String name) throws SQLException {
        con.setAutoCommit(false);
        try {
            PreparedStatement ps = con.prepareStatement("delete from products where pname=?");
            ps.setString(1, name);
            int r = ps.executeUpdate();
            con.commit();
            if (r > 0)
                System.out.println("Deleted..");
            else
                System.out.println("No Product Exists..");
        } catch (Exception e) {
            // Handle the exception
            System.out.println(e);
            System.out.println("Rolling back...");
            con.rollback();
        }
    }
}
