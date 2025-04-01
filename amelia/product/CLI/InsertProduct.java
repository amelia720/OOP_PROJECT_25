package amelia.product.CLI;

import java.sql.Connection;
import java.util.Scanner;

import amelia.MyConnection;
import amelia.product.Product;
import amelia.product.ProductDAO;

public class InsertProduct {

    public static void main(String[] args) {
        insertProd(); // CLI entry point
    }

    public static void insertProd() {
        Scanner input = new Scanner(System.in);
        Connection conn = null;

        try {
            Product product = new Product();

            System.out.print("Enter product name: ");
            product.setName(input.nextLine());

            System.out.print("Enter category: ");
            product.setCategory(input.nextLine());

            // Price validation loop
            while (true) {
                try {
                    System.out.print("Enter price: ");
                    double price = Double.parseDouble(input.nextLine());
                    product.setPrice(price);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter a valid price.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            // Stock validation loop
            while (true) {
                try {
                    System.out.print("Enter stock quantity: ");
                    int stock = Integer.parseInt(input.nextLine());
                    product.setStock(stock);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter a valid stock quantity.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            // Insert into DB
            conn = MyConnection.getConnection();
            ProductDAO.insertProduct(conn, product);

            System.out.println("Product added successfully.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.out.println("⚠️ Failed to close connection: " + e.getMessage());
            }
            input.close();
        }
    }
}
