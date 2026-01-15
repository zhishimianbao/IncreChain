# IncreChain

<p align="center"><a href="README_zh.md">中文</a> | English</p>

A secure lightweight incremental data sharing framework based on ABE-KDF and blockchain

## 🛠️ Technology Stack

- **Java 8+**
- **Maven** - Project build and dependency management
- **JPBC (Java Pairing-Based Cryptography)** - Bilinear pairing cryptography library
- **BouncyCastle** - Cryptography provider
- **Apache POI** - Excel file operations
- **Lombok** - Simplify Java code
- **Gson/JSON** - JSON data processing

## 📁 Project Structure

```
.
├── src/main/java/com/hjx/cp/abe/
│   ├── attribute/          # Attribute-related classes
│   │   └── Attribute.java
│   ├── engine/             # CP-ABE encryption engine
│   │   └── CpAneEngine.java
│   ├── MyPlan/             # Test and experimental code
│   │   └── Mytest.java
│   ├── parameter/          # Key parameter classes
│   │   ├── SystemKey.java
│   │   ├── PublicKey.java
│   │   ├── MasterPrivateKey.java
│   │   └── UserPrivateKey.java
│   ├── structure/          # Access tree structure
│   │   ├── AccessTree.java
│   │   ├── AccessTreeNode.java
│   │   └── ...
│   ├── text/               # Plaintext and ciphertext classes
│   │   ├── PlainText.java
│   │   └── CipherText.java
│   └── util/               # Utility classes
│       └── ConvertUtils.java
├── lib/                    # Third-party JAR libraries
├── params/                 # Cryptographic parameter configurations
│   ├── curves/            # Elliptic curve parameters
│   └── mm/                # Pairing parameters
├── input/                  # Input data files (JSON format)
├── pom.xml                # Maven configuration file
└── LICENSE                # Apache License 2.0

```

## 🚀 Quick Start

### Environment Requirements

- JDK 8 or higher
- Maven 3.6+

### Installation Steps

1. **Clone the project**

   ```bash
   git clone <repository-url>
   cd A-CP-ABE-Github
   ```
2. **Compile the project**

   ```bash
   mvn clean compile
   ```
3. **Run tests**

   ```bash
   mvn exec:java -Dexec.mainClass="com.hjx.cp.abe.MyPlan.Mytest"
   ```

### Experimental Testing

The project includes a complete performance testing module (`Mytest.java`) that can test:

- ABE encryption/decryption time
- Batch file encryption/decryption time
- Performance under different numbers of departments

After running the tests, results are automatically saved to the `Mytest.xlsx` file.

## 📦 Dependency Description

Main libraries dependencies are located in the `lib/` directory:

- `jpbc-*.jar` - JPBC cryptography library
- `bcprov-jdk16-1.46.jar` - BouncyCastle provider

Maven dependencies include:

- Lombok
- Apache POI
- Gson
- Log4j
- JWT, etc.

## 📝 Configuration Description

Cryptographic parameter configuration files are located in the `params/` directory:

- `curves/` - Elliptic curve parameters (a.properties, d159.properties, etc.)
- `mm/ctl13/` - Pairing parameters (toy, small, medium, large, etc.)

You can choose appropriate parameter configurations based on security requirements.

## 📄 License

This project is licensed under the [Apache License 2.0](LICENSE).

## 📧 Contact

For questions or suggestions, please submit an Issue or Pull Request.