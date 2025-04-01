package amelia.product.CLI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import amelia.product.Product;
import amelia.product.ProductDAO;
import amelia.MyConnection;

public class EditProduct 
{
    public static void editProd() 
    {
        Connection connection = null;
        Scanner input = new Scanner(System.in);

        try 
        {
            connection = MyConnection.getConnection();
            ViewProduct.viewAllProducts();

            System.out.print("\nEnter the ID of the product to edit: ");
            int productId = Integer.parseInt(input.nextLine());

            // Check if product exists
            PreparedStatement checkStmt = connection.prepareStatement(
                "SELECT * FROM Product WHERE productId = ?"
            );
            checkStmt.setInt(1, productId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) 
            {
                System.out.println("Product not found.");
                return;
            }

            Product product = new Product();
            product.setProductId(productId);

            System.out.print("Enter new product name: ");
            product.setName(input.nextLine());

            System.out.print("Enter new category: ");
            product.setCategory(input.nextLine());

            // Price input with validation
            while (true) 
            {
                try 
                {
                    System.out.print("Enter new price: ");
                    product.setPrice(Double.parseDouble(input.nextLine()));
                    break;
                } 
                catch (NumberFormatException e) 
                {
                    System.out.println("Invalid number. Please enter a valid price.");
                } 
                catch (IllegalArgumentException e) 
                {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            // Stock input with validation
            while (true) 
            {
                try 
                {
                    System.out.print("Enter new stock quantity: ");
                    product.setStock(Integer.parseInt(input.nextLine()));
                    break;
                } 
                catch (NumberFormatException e) 
                {
                    System.out.println("Invalid number. Please enter a valid stock quantity.");
                } 
                catch (IllegalArgumentException e) 
                {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            int rows = ProductDAO.updateProduct(connection, product);
            if (rows > 0) 
            {
                System.out.println("Product updated successfully.");
            } 
            else 
            {
                System.out.println("Update failed.");
            }

        } 
        catch (SQLException e) 
        {
            System.out.println("SQL Error: " + e.getMessage());
        } 
        catch (NumberFormatException e) 
        {
            System.out.println("Invalid input. Please enter a numeric product ID.");
        } 
        finally 
        {
            try 
            {
                if (connection != null) connection.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Error closing connection: " + e.getMessage());
            }
            input.close();
        }
    }
}
