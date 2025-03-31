package amelia.customer.CLI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import amelia.customer.Customer;
import amelia.customer.CustomerDAO;
import amelia.MyConnection;

public class EditCustomer {
    public static void editCust() {
        Connection connection = null;
        Scanner input = new Scanner(System.in);

        try {
            connection = MyConnection.getConnection();
            ViewCustomer.viewAllCustomers();

            System.out.print("\nEnter the ID of the customer to edit: ");
            int customerId = Integer.parseInt(input.nextLine());

            // Check if customer exists
            PreparedStatement checkStmt = connection.prepareStatement(
                "SELECT * FROM Customer WHERE customerId = ?"
            );
            checkStmt.setInt(1, customerId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Customer not found.");
                return;
            }

            Customer customer = new Customer();
            customer.setCustomerId(customerId);

            System.out.print("Enter new first name: ");
            customer.setFname(input.nextLine());

            System.out.print("Enter new surname: ");
            customer.setSname(input.nextLine());

            System.out.print("Enter new address: ");
            customer.setAddress(input.nextLine());

            while (true) {
                try {
                    System.out.print("Enter new email: ");
                    customer.setEmail(input.nextLine());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            while (true) {
                try {
                    System.out.print("Enter new phone (10 digits): ");
                    customer.setPhone(input.nextLine());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            int rows = CustomerDAO.updateCustomer(connection, customer);
            if (rows > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("Update failed.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numeric customer ID.");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
            input.close();
        }
    }
}
