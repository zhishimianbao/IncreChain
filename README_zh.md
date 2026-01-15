# IncreChain

<p align="center">中文 | <a href="README_en.md">English</a></p>

一种基于ABE-KDF与区块链的安全轻量增量式数据共享框架

## 🛠️ 技术栈

- **Java 8+**
- **Maven** - 项目构建和依赖管理
- **JPBC (Java Pairing-Based Cryptography)** - 双线性配对密码学库
- **BouncyCastle** - 密码学提供者
- **Apache POI** - Excel文件操作
- **Lombok** - 简化Java代码
- **Gson/JSON** - JSON数据处理

## 📁 项目结构

```
.
├── src/main/java/com/hjx/cp/abe/
│   ├── attribute/          # 属性相关类
│   │   └── Attribute.java
│   ├── engine/             # CP-ABE加密引擎
│   │   └── CpAneEngine.java
│   ├── MyPlan/             # 测试和实验代码
│   │   └── Mytest.java
│   ├── parameter/          # 密钥参数类
│   │   ├── SystemKey.java
│   │   ├── PublicKey.java
│   │   ├── MasterPrivateKey.java
│   │   └── UserPrivateKey.java
│   ├── structure/          # 访问树结构
│   │   ├── AccessTree.java
│   │   ├── AccessTreeNode.java
│   │   └── ...
│   ├── text/               # 明文和密文类
│   │   ├── PlainText.java
│   │   └── CipherText.java
│   └── util/               # 工具类
│       └── ConvertUtils.java
├── lib/                    # 第三方JAR库
├── params/                 # 密码学参数配置
│   ├── curves/            # 椭圆曲线参数
│   └── mm/                # 配对参数
├── input/                  # 输入数据文件（JSON格式）
├── pom.xml                # Maven配置文件
└── LICENSE                # Apache License 2.0

```

## 🚀 快速开始

### 环境要求

- JDK 8 或更高版本
- Maven 3.6+

### 安装步骤

1. **克隆项目**

   ```bash
   git clone <repository-url>
   cd A-CP-ABE-Github
   ```
2. **编译项目**

   ```bash
   mvn clean compile
   ```
3. **运行测试**

   ```bash
   mvn exec:java -Dexec.mainClass="com.hjx.cp.abe.MyPlan.Mytest"
   ```

### 实验测试

项目包含完整的性能测试模块（`Mytest.java`），可以测试：

- ABE加密/解密耗时
- 文件批量加密/解密耗时
- 不同部门数量下的性能表现

运行测试后，结果会自动保存到 `Mytest.xlsx` 文件中。

## 📦 依赖说明

项目依赖的主要库位于 `lib/` 目录：

- `jpbc-*.jar` - JPBC密码学库
- `bcprov-jdk16-1.46.jar` - BouncyCastle提供者

Maven依赖包括：

- Lombok
- Apache POI
- Gson
- Log4j
- JWT等

## 📝 配置说明

密码学参数配置文件位于 `params/` 目录：

- `curves/` - 椭圆曲线参数（a.properties, d159.properties等）
- `mm/ctl13/` - 配对参数（toy, small, medium, large等）

可根据安全需求选择合适的参数配置。

## 📄 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证。

## 📧 联系方式

如有问题或建议，欢迎提交 Issue 或 Pull Request。
