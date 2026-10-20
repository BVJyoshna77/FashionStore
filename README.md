# FashionStore 

**FashionStore** is a Java-based e-commerce web application designed to make online fashion shopping convenient. Users can browse clothing collections, explore products, manage their shopping cart, and place orders through a web interface.

## Features

- **Home Page:** Browse featured products and fashion collections.
- **Product Catalog:** Explore products across categories such as Men, Women, and Kids.
- **Product Details:** View product information and pricing.
- **User Authentication:** Register and log in to an account.
- **Email OTP Verification:** Verify email addresses during registration, if configured.
- **Shopping Cart:** Add and manage products before checkout.
- **Checkout:** Place orders using the available payment options.
- **Order Management:** Save and view order information.
- **Admin Features:** Manage store information through authorized admin functionality.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Backend programming |
| Jakarta Servlets | Request handling |
| JSP | Dynamic web pages |
| Apache Tomcat 10.1 | Web application server |
| Maven | Dependency management and build |
| MySQL | Database |
| JDBC | Database connectivity |
| HTML, CSS, JavaScript | Frontend |
| Bootstrap 5 | Responsive UI |
| Jakarta Mail | Email OTP functionality |

## Project Structure

```text
FashionStore/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/fashionstore/
│       │       ├── dao/
│       │       ├── model/
│       │       ├── servlet/
│       │       ├── service/
│       │       └── util/
│       └── webapp/
│           ├── WEB-INF/
│           │   └── views/
│           ├── css/
│           ├── images/
│           └── index.jsp
├── schema_update.sql
├── pom.xml
├── .gitignore
└── README.md
```

*The structure above is illustrative; the exact packages and files may vary in the project.*

## Prerequisites

Install the following software:

- JDK 21
- Apache Maven
- Apache Tomcat 10.1
- MySQL Server
- Git (optional, for cloning the repository)

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/BVJyoshna77/FashionStore.git
cd FashionStore
```

### 2. Configure the Database

1. Start MySQL Server.
2. Create the `fashion_store` database if it does not already exist.
3. Review `schema_update.sql` and execute the required SQL statements.
4. Configure the database connection using your local database credentials.

**Security:** Never commit real database passwords or other secrets to GitHub.

### 3. Configure Environment Variables

Set the variables required by your application, if used:

```text
FASHIONSTORE_GMAIL
FASHIONSTORE_GMAIL_APP_PASSWORD
FASHIONSTORE_OTP_PEPPER
```

Configure your database connection variables according to the implementation in your project. Use secure values and keep them outside source control.

### 4. Build the Project

From the directory containing `pom.xml`, run:

```bash
mvn clean package
```

The generated WAR file should be available in the `target/` directory if the build succeeds.

### 5. Run the Application

**Using Eclipse:**

1. Import the project as an existing Maven project.
2. Configure Apache Tomcat 10.1.
3. Add FashionStore to the Tomcat server.
4. Start the server.
5. Open the application URL shown by your local Tomcat configuration.

**Using a WAR file:**

Deploy the generated WAR file to a compatible Tomcat 10.1 server.

##  Security Considerations

- Store passwords using a secure password-hashing method.
- Use prepared statements for database queries.
- Keep database credentials and Gmail App Passwords out of source code.
- Store secrets in environment variables or a secure secrets manager.
- Protect administrative routes with role-based authorization.
- Validate registration input and enforce OTP expiry and attempt limits when OTP verification is enabled.

##  Author

**Jyoshna**

GitHub: [BVJyoshna77](https://github.com/BVJyoshna77)

## 📄 License

No license has been specified yet. Add a license file if you intend to define how others may use, modify, and distribute this project.
