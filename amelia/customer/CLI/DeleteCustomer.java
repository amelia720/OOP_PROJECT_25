package amelia.customer.CLI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import amelia.customer.CustomerDAO;
import amelia.MyConnection;

public class DeleteCustomer {
    public static void deleteCust() {
        Scanner input = new Scanner(System.in);
        Connection conn = null;

        try {
            conn = MyConnection.getConnection();
            ViewCustomer.viewAllCustomers();

            System.out.print("\nEnter the ID of the customer to delete: ");
            int customerId = Integer.parseInt(input.nextLine());

            // Confirm deletion
            System.out.print("Are you sure you want to delete this customer? (yes/no): ");
            String confirm = input.nextLine().trim().toLowerCase();
            if (!confirm.equals("yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            int deleted = CustomerDAO.deleteCustomer(conn, customerId);
            if (deleted > 0) {
                System.out.println("✅ Customer deleted successfully.");
            } else {
                System.out.println("❌ Customer not found or already deleted.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            input.close();
        }
    }
}
