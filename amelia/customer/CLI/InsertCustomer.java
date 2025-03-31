package amelia.customer.CLI;

import java.sql.Connection;
import java.util.Scanner;
import amelia.MyConnection;

import amelia.customer.Customer;
import amelia.customer.CustomerDAO;

public class InsertCustomer {

    public static void main(String[] args) {
        insertCust(); // CLI entry point
    }

    public static void insertCust() {
        Scanner input = new Scanner(System.in);
        Connection conn = null;

        try {
            Customer customer = new Customer();

            System.out.print("Enter first name: ");
            customer.setFname(input.nextLine());

            System.out.print("Enter surname: ");
            customer.setSname(input.nextLine());

            System.out.print("Enter address: ");
            customer.setAddress(input.nextLine());

            // Email validation loop
            while (true) {
                try {
                    System.out.print("Enter email: ");
                    customer.setEmail(input.nextLine());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            // Phone validation loop
            while (true) {
                try {
                    System.out.print("Enter phone (10 digits): ");
                    customer.setPhone(input.nextLine());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            // Get DB connection and insert
            conn = MyConnection.getConnection();
            CustomerDAO.insertCustomer(conn, customer);

            System.out.println("Customer added successfully.");

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
