# AndroidSignPlugin

AndroidSignPlugin 是一个 Gradle 插件，用于统一管理 Android 应用签名配置。

[English README](README.md)

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.JosephusZhou.androidsigning?label=Gradle%20Plugin%20Portal&logo=gradle)](https://plugins.gradle.org/plugin/io.github.JosephusZhou.androidsigning)

## 功能

- 优先从根目录 `local.properties` 读取签名信息。
- 回退读取根目录 `key.properties`。
- 创建或复用 Android `release` 签名配置。
- 可让 `debug` 构建复用 release 签名。
- 当 release 签名信息不存在时，可回退到 debug 签名。

## 快速开始

在 Android 项目的 `build.gradle.kts` 中添加插件：

```kotlin
plugins {
    id("io.github.JosephusZhou.androidsigning") version "1.0.0"
}
```

> 可在 [Gradle Plugin Portal](https://plugins.gradle.org/plugin/io.github.JosephusZhou.androidsigning) 查看所有可用版本。

## 插件坐标

- `plugin id`: `io.github.JosephusZhou.androidsigning`
- `artifact`: `AndroidSignPlugin`
- `version`: `1.0.0`
- `group`: `io.github.JosephusZhou`
- `package`: `io.github.JosephusZhou`
- Portal: https://plugins.gradle.org/plugin/io.github.JosephusZhou.androidsigning

## 签名来源

`local.properties`：

```properties
storeFile=/path/to/keystore.jks
storePassword=***
keyAlias=***
keyPassword=***
```

`key.properties`：

```properties
storeFile=keystore/release.jks
storePassword=***
keyAlias=***
keyPassword=***
```

## 应用插件

```kotlin
plugins {
    id("io.github.JosephusZhou.androidsigning") version "1.0.0"
}
```

## 可选配置

```kotlin
androidSigning {
    localPropertiesFileName.set("local.properties")
    propertiesFileName.set("key.properties")
    localPropertyPrefix.set("")
    releaseConfigName.set("release")
    signDebugWithRelease.set(true)
    fallbackToDebugSigning.set(true)
}
```

## 扩展参数

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `localPropertiesFileName` | `local.properties` | 优先读取的根目录本地属性文件。 |
| `propertiesFileName` | `key.properties` | 当本地签名信息不存在或不完整时使用的回退属性文件。 |
| `localPropertyPrefix` | `""` | `local.properties` 中签名键的前缀；为空时直接读取 `storeFile` 这类键。 |
| `releaseConfigName` | `release` | 插件创建或复用的 Android 签名配置名称。 |
| `signDebugWithRelease` | `true` | 当签名信息可用时，`debug` 构建是否复用 release 签名。 |
| `fallbackToDebugSigning` | `true` | 当签名信息不存在时，`release` 构建是否回退到 debug 签名。 |

## 默认行为

1. 从根目录 `local.properties` 读取 `storeFile`、`storePassword`、`keyAlias`、`keyPassword`。
2. 如果 `local.properties` 没有提供完整签名信息，再从根目录 `key.properties` 读取同名字段。
3. 如果读取到完整签名信息，则 `release` 使用这套签名；默认情况下 `debug` 也会复用这套签名。
4. 如果没有读取到完整签名信息，则默认让 `release` 回退到 debug 签名。

## 发布到 Maven Local

```bash
./gradlew publishToMavenLocal
```

## 发布到 Gradle Plugin Portal

先创建 Gradle Plugin Portal 账号并生成 API Key，然后将凭据添加到用户级 Gradle 配置文件：

```properties
gradle.publish.key=YOUR_KEY
gradle.publish.secret=YOUR_SECRET
```

默认路径是 `~/.gradle/gradle.properties`。

CI 中也可以使用环境变量：

```bash
GRADLE_PUBLISH_KEY=YOUR_KEY
GRADLE_PUBLISH_SECRET=YOUR_SECRET
```

发布命令：

```bash
./gradlew publishPlugins
```

首次发布会进入 Gradle Plugin Portal 人工审核流程。

当前 Portal 元数据：

- `website`: `https://github.com/josephuszhou/AndroidSignPlugin`
- `vcsUrl`: `https://github.com/josephuszhou/AndroidSignPlugin.git`
- `tags`: `android`, `signing`, `keystore`, `local-properties`
- `configurationCache`: `false`

发布命名空间使用 Gradle Plugin Portal 审核认可的 GitHub 用户归属形式：`io.github.JosephusZhou`。

## License

AndroidSignPlugin 基于 MIT License 发布。
