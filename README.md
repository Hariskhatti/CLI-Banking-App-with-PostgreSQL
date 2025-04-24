# 💳 CLI Banking App with PostgreSQL


Ek simple sa lekin powerful **banking system** banaya hai Java mein — jisme user register, login, account create, paisa jama/nikal aur transfer sab kuch ho sakta hai. Yeh poora console se chal raha hai aur backend mein PostgreSQL use ho raha hai. Pure JDBC ke sath likha gaya hai, bina kisi external library ke.

---

## 🔧 Tech Used

- **Java (JDK 8+)** – Core logic & UI (CLI based)
- **PostgreSQL** – Data storage (Users & Accounts)
- **JDBC (Java Database Connectivity)** – SQL connection & queries

---

## 💡 Key Features

- 🔐 **User Authentication** (email + password)
- 🧾 **Account Management**
  - Create account (with security pin & account number)
  - Delete account
- 💰 **Banking Operations**
  - Check balance
  - Deposit / Withdraw funds
  - Send money to other users
- 🔁 **Nested Menus** for smooth navigation
- 🧠 **Transaction Handling** with rollback on failure (money transfer)
- 🚫 Input validation & error messages

---

## 🗂 Database Tables

### 🧍‍♂️ `user1`

| Column     | Type | Description             |
|------------|------|-------------------------|
| full_name  | text | User ka full name       |
| email      | text | **Primary key**         |
| password   | text | Login password          |

---

### 🏦 `account`

| Column         | Type   | Description                                 |
|----------------|--------|---------------------------------------------|
| full_name      | text   | Account holder ka naam                      |
| email          | text   | **Foreign key** → `user1(email)`           |
| balance        | int    | User ka available balance                   |
| security_pin   | text   | 4-digit PIN (as text)                       |
| account_number | serial | **Primary key**, auto-generated ID          |

📌 `account.email` foreign key hai jo `user1.email` se linked hai — har account ka ek registered user hona zaruri hai.

---

## 🧾 SQL Queries (Run these to create tables)

```sql
create table user1(
  full_name text,
  email text primary key,
  password text
);

create table account(
  full_name text,
  email text references user1(email),
  balance int,
  security_pin text,
  account_number serial primary key
);
```

---

## ▶️ How to Run

1. PostgreSQL install karo aur ek DB banao naam: `bankingsystem`
2. Upar wale 2 tables create karo using SQL
3. Apne DB credentials code mein daalo:
   ```java
   public static final String url = "jdbc:postgresql://localhost:5432/bankingsystem";
   public static final String user = "postgres"; // ya tumhara username
   public static final String password = "your_password";
   ```

4. Compile & Run karo:

   ```bash
   javac banking_system.java
   java banking_system
   ```

---

## 🧪 App Flow

```
Welcome to the Bank
 1 : Register 
 2 : Login 
 3 : Exit 
```

**Login ke baad:**

- Account create karo
- Account login karo (email + pin se)
- Fir milta hai:
  - Check Balance
  - Deposit
  - Withdraw
  - Send Money

---

## 🔄 Transfer Logic (How it works)

- Receiver ki email check hoti hai DB mein
- Sender apna security pin dalta hai
- Agar sab sahi hai aur balance bhi hai to:
  - Sender se paisa minus
  - Receiver mein paisa plus
- Ye sab ek hi **transaction block** mein hota hai
- Agar koi error aaya, toh **rollback** ho jata hai

---

## ✅ TODOs / Improvements (Agar aage badhaye to...)

- GUI (JavaFX / Swing based)
- Password hashing (abhi plain-text)
- Admin panel / multi-role support
- Transaction history table
- Input validation aur robust error handling

---

## 🧑‍💻 Created By

Made by **Haris Khatti** 
Mann se likha, test karke banaya, aur thoda dil se design bhi kiya 💻❤️  
Agar pasand aaye toh ⭐ zarur dena bhai!

---

## 📜 License

**MIT License** — khula source hai, use karo, seekho, aage badhao.
