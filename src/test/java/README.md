# 运行测试案例

本文件提供了如何运行测试案例的说明。

## 前提条件
- 确保您的计算机上已安装 Java 和 Maven。
- 导航到包含 `pom.xml` 文件的项目根目录。

## 运行测试
要运行所有测试案例，请使用以下命令：

```bash
mvn test
```

此命令将编译测试类并执行 `src/test/java` 目录中定义的所有测试案例。

## 预期结果
- 在成功执行测试后，您应该看到输出指示所有测试已通过。请查找类似的消息：
  - `Tests run: X, Failures: 0, Errors: 0, Skipped: 0`
- 如果有任何测试失败，输出将提供有关哪些测试失败及失败原因的详细信息，从而便于调试。

## 其他信息
- 您可以通过指定类名来运行特定的测试类：

```bash
mvn -Dtest=UserServiceTest test
```

- 要获得更详细的输出，您可以使用 `-Dmaven.test.failure.ignore=false` 选项，以确保 Maven 在第一次失败时停止。
