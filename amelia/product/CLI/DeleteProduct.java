package amelia.product.CLI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import amelia.product.ProductDAO;
import amelia.MyConnection;

public class DeleteProduct {
    public static void deleteProd() {
        Scanner input = new Scanner(System.in);
        Connection conn = null;

        try {
            conn = MyConnection.getConnection();
            ViewProduct.viewAllProducts(); // This method should display all products before deletion

            System.out.print("\nEnter the ID of the product to delete: ");
            int productId = Integer.parseInt(input.nextLine());

            // Confirm deletion
            System.out.print("Are you sure you want to delete this product? (yes/no): ");
            String confirm = input.nextLine().trim().toLowerCase();
            if (!confirm.equals("yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            int deleted = ProductDAO.deleteProduct(conn, productId);
            if (deleted > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product not found or already deleted.");
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
