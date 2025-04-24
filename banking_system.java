package Lets_Ceate_File;

import javax.swing.*;
import java.sql.*;
import java.util.Scanner;

public class banking_system {
    // PostgreSQL database connection details
    public static final String url = "jdbc:postgresql://localhost:5432/bankingsystem";
    public static final String user = "postgres"; // Default PostgreSQL user
    public static final String password = "haris";

    public static void main(String[] args) {
        banking_system bs = new banking_system();
        try {
            Class.forName("org.postgresql.Driver"); // Load PostgreSQL JDBC Driver
        } catch (Exception e) {
            System.out.println(e);
        }

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            System.out.println("Welcome to the Bank");
            while (true) {
                Scanner sc = new Scanner(System.in);
                System.out.println(" 1 : Register \n 2 : Login \n 3 : Exit \n Enter the command line no: ");
                int choose = sc.nextInt();
                switch (choose) {
                    case 1:
                        bs.createUser(con, sc); // Register new user
                        break;
                    case 2:
                        String LoginEmail = bs.login(con, sc); // Login user
                        if (LoginEmail != null) {
                            System.out.println("Login successful for: " + LoginEmail);
                            while (true) {
                                System.out.println("Welcome to bank \n 1 : Create account \n 2 : Login \n 3 : Delete account \n 4 : Logout");
                                int innerChoose = sc.nextInt();
                                switch (innerChoose) {
                                    case 1:
                                        bs.createAccount(con, sc, LoginEmail); // Create account
                                        break;
                                    case 2:
                                        String accountEmail = bs.loginAcc(con, sc); // Login account
                                        if (accountEmail != null && accountEmail.equals(LoginEmail)) {
                                            System.out.println("Account Login successful for: " + accountEmail);
                                            while (true) {
                                                System.out.println("Banking Menu:\n 1 : Check Balance\n 2 : Deposit\n 3 : Withdraw\n 4 : Send Money \n 5 : Exit Account");
                                                int bankChoice = sc.nextInt();
                                                switch (bankChoice) {
                                                    case 1:
                                                        bs.checkBalance(con, accountEmail); // Check balance
                                                        break;
                                                    case 2:
                                                        bs.depositBalance(con, accountEmail, sc); // Deposit money
                                                        break;
                                                    case 3:
                                                        bs.withdrawBalance(con, accountEmail, sc); // Withdraw money
                                                        break;
                                                    case 4:
                                                        bs.transferMoney(con, accountEmail, sc);
                                                        break;
                                                    case 5:
                                                        System.out.println("Exiting Account Menu..."); // Exit to main account menu
                                                        break;
                                                    default:
                                                        System.out.println("Invalid option.");
                                                }
                                                if (bankChoice == 5) break; // Exit from account
                                            }
                                        } else {
                                            System.out.println("Invalid account login or not your account.");
                                        }
                                        break;
                                    case 3:
                                        bs.deleteAcc(con, sc, LoginEmail); // Delete account
                                        break;
                                    case 4:
                                        System.out.println("Logging out from account..."); // Logout to login/register menu
                                        break;
                                    default:
                                        System.out.println("Invalid command");
                                }
                                if (innerChoose == 4) break; // Exit to main menu
                            }
                        } else {
                            System.out.println("Invalid login");
                        }
                        break;
                    case 3:
                        System.out.println("Exiting... Thank you for using the app!"); // Exit application
                        return;
                    default:
                        System.out.println("Invalid option, please try again");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Create new user
    void createUser(Connection con, Scanner sc) {
        try {
            PreparedStatement ps1 = con.prepareStatement("insert into user1 (full_name, email, password) values(?,?,?)");
            System.out.println("Enter fullname");
            String fname = sc.next();
            System.out.println("Enter email");
            String email = sc.next();
            System.out.println("Enter password");
            String password = sc.next();
            ps1.setString(1, fname);
            ps1.setString(2, email);
            ps1.setString(3, password);
            int rowsEffect = ps1.executeUpdate();
            if (rowsEffect > 0) {
                System.out.println("User Registered Successfully");
            } else {
                System.out.println("Error in registration");
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Email already used. Please try with another email.");
            } else {
                System.out.println("Something went wrong while creating user.");
            }
        }
    }

    // Login user
    String login(Connection con, Scanner sc) {
        try {
            System.out.println("Enter email and password to login");
            String email = sc.next();
            String password = sc.next();
            PreparedStatement ps1 = con.prepareStatement("select * from user1 where email = ? and password = ?");
            ps1.setString(1, email);
            ps1.setString(2, password);
            ResultSet rowsEffect = ps1.executeQuery();
            if (rowsEffect.next()) {
                return email; // Return email if login successful
            } else {
                return null; // Return null if login failed
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // Create bank account
    void createAccount(Connection con, Scanner sc, String loggedInEmail) {
        try {
            System.out.println("Creating account for: " + loggedInEmail);
            PreparedStatement ps1 = con.prepareStatement("Insert into account (full_name, email, security_pin, account_number) values(?,?,?,?);");
            System.out.println("Enter full name");
            String fname = sc.next();
            System.out.println("Enter security_pin");
            int security_pin = sc.nextInt();
            System.out.println("Enter account number");
            int account_number = sc.nextInt();
            ps1.setString(1, fname);
            ps1.setString(2, loggedInEmail);
            ps1.setInt(3, security_pin);
            ps1.setInt(4, account_number);
            int rowsEffect = ps1.executeUpdate();
            if (rowsEffect > 0) {
                System.out.println("Account created successfully");
            } else {
                System.out.println("Account not created");
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("This email is already registered with another account.");
            } else {
                System.out.println("Something went wrong while creating account.");
            }
        }
    }

    // Login to bank account
    String loginAcc(Connection con, Scanner sc) {
        try {
            System.out.println("Enter email and security_pin to login account");
            String email = sc.next();
            String security_pin = sc.next();
            PreparedStatement ps1 = con.prepareStatement("select * from account where email = ? and security_pin = ?");
            ps1.setString(1, email);
            ps1.setString(2, security_pin);
            ResultSet rowsEffect = ps1.executeQuery();
            if (rowsEffect.next()) {
                return email;
            } else {
                System.out.println("Account not found for this user.");
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // Delete account
    void deleteAcc(Connection con, Scanner sc, String loggedInEmail) {
        try {
            System.out.println("Enter your security_pin to delete your account");
            String security_pin = sc.next();
            PreparedStatement ps1 = con.prepareStatement("delete from account where email = ? and security_pin =?");
            ps1.setString(1, loggedInEmail);
            ps1.setString(2, security_pin);
            int rowsEffect = ps1.executeUpdate();
            if (rowsEffect > 0) {
                System.out.println("Account deleted successfully");
            } else {
                System.out.println("Account not deleted");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Check account balance
    void checkBalance(Connection con, String email) {
        try {
            PreparedStatement ps1 = con.prepareStatement("select balance from account where email = ?");
            ps1.setString(1, email);
            ResultSet rowsEffect = ps1.executeQuery();
            if (rowsEffect.next()) {
                int balance = rowsEffect.getInt("balance");
                System.out.println("Your current balance is: " + balance);
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Deposit into account
    void depositBalance(Connection con, String email, Scanner sc) {
        try {
            PreparedStatement ps1 = con.prepareStatement("update account set balance = balance + ? where email = ?");
            System.out.println("Enter amount to deposit");
            int amount = sc.nextInt();
            ps1.setInt(1, amount);
            ps1.setString(2, email);
            int rowsEffect = ps1.executeUpdate();
            if (rowsEffect > 0) {
                System.out.println("Amount deposited successfully");
            } else {
                System.out.println("Facing error in depositing");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Withdraw from account
    void withdrawBalance(Connection con, String email, Scanner sc) {
        try {
            PreparedStatement ps1 = con.prepareStatement("update account set balance = balance - ? where email = ?");
            System.out.println("Enter amount to withdraw");
            int amount = sc.nextInt();
            ps1.setInt(1, amount);
            ps1.setString(2, email);
            int rowsEffect = ps1.executeUpdate();
            if (rowsEffect > 0) {
                System.out.println("Amount withdraw done");
            } else {
                System.out.println("Facing error in withdraw");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Transfer money with security pin verification
    void transferMoney(Connection con, String senderEmail, Scanner sc) {
        try {
            con.setAutoCommit(false); // Start transaction

            // 1. Get receiver email
            System.out.print("Enter receiver's email: ");
            String receiverEmail = sc.next();

            // 2. Check if receiver exists
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM account WHERE email = ?");
            ps.setString(1, receiverEmail);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Receiver account not found.");
                return;
            } else {
                System.out.println("Account found.");
            }

            // 3. Get transfer amount
            System.out.print("Enter amount to transfer: ");
            int amount = sc.nextInt();

            // 4. Ask for sender's security pin
            System.out.print("Enter your security pin: ");
            String securityPin = sc.next();

            // 5. Verify sender and pin, and get balance
            ps = con.prepareStatement("SELECT balance FROM account WHERE email = ? AND security_pin = ?");
            ps.setString(1, senderEmail);
            ps.setString(2, securityPin);
            rs = ps.executeQuery();

            if (rs.next()) {
                int senderBalance = rs.getInt("balance");

                if (amount > senderBalance) {
                    System.out.println("Insufficient balance.");
                    return;
                }

                // 6. Perform transfer using transaction

                // Deduct from sender
                PreparedStatement deduct = con.prepareStatement("UPDATE account SET balance = balance - ? WHERE email = ?");
                deduct.setInt(1, amount);
                deduct.setString(2, senderEmail);
                int send = deduct.executeUpdate();

                // Add to receiver
                PreparedStatement add = con.prepareStatement("UPDATE account SET balance = balance + ? WHERE email = ?");
                add.setInt(1, amount);
                add.setString(2, receiverEmail);
                int receive = add.executeUpdate();

                // Commit the transaction
                con.commit(); // Commit transaction

                if (send > 0 && receive > 0) {
                    System.out.println("Transfer successful.");
                } else {
                    System.out.println("Error: Could not transfer to receiver.");
                    con.rollback();
                }

            } else {
                System.out.println("Invalid security pin or sender account not found.");
            }

        } catch (Exception e) {
            try {
                con.rollback();
                System.out.println("Transaction rolled back.");
            } catch (SQLException ex) {
                System.out.println("Rollback failed: " + ex);
            }
            System.out.println("Error during transfer: " + e);
        }
    }


}