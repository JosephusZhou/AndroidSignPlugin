# AndroidSignPlugin

AndroidSignPlugin is a Gradle plugin that centralizes Android application signing configuration.

[中文文档](README.zh-CN.md)

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.JosephusZhou.androidsigning?label=Gradle%20Plugin%20Portal&logo=gradle)](https://plugins.gradle.org/plugin/io.github.JosephusZhou.androidsigning)

## Features

- Reads signing credentials from the root `local.properties` first.
- Falls back to the root `key.properties`.
- Creates or reuses the Android `release` signing config.
- Can reuse release signing for `debug` builds.
- Can fall back to debug signing when release credentials are absent.

## Quick Start

Add the plugin to your Android project's `build.gradle.kts`:

```kotlin
plugins {
    id("io.github.JosephusZhou.androidsigning") version "1.0.0"
}
```

> See the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/io.github.JosephusZhou.androidsigning) for available versions.

## Plugin Coordinates

- `plugin id`: `io.github.JosephusZhou.androidsigning`
- `artifact`: `AndroidSignPlugin`
- `version`: `1.0.0`
- `group`: `io.github.JosephusZhou`
- `package`: `io.github.JosephusZhou`
- Portal: https://plugins.gradle.org/plugin/io.github.JosephusZhou.androidsigning

## Signing Sources

`local.properties`:

```properties
storeFile=/path/to/keystore.jks
storePassword=***
keyAlias=***
keyPassword=***
```

`key.properties`:

```properties
storeFile=keystore/release.jks
storePassword=***
keyAlias=***
keyPassword=***
```

## Apply The Plugin

```kotlin
plugins {
    id("io.github.JosephusZhou.androidsigning") version "1.0.0"
}
```

## Optional Configuration

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

## Extension Properties

| Property | Default | Description |
| --- | --- | --- |
| `localPropertiesFileName` | `local.properties` | Root local properties file read first. |
| `propertiesFileName` | `key.properties` | Fallback properties file used when local credentials are absent or incomplete. |
| `localPropertyPrefix` | `""` | Prefix for keys in `local.properties`; empty means keys such as `storeFile` are read directly. |
| `releaseConfigName` | `release` | Android signing config name created or reused by the plugin. |
| `signDebugWithRelease` | `true` | Whether `debug` builds should reuse release signing when credentials are available. |
| `fallbackToDebugSigning` | `true` | Whether `release` builds should fall back to debug signing when credentials are absent. |

## Default Behavior

1. Read `storeFile`, `storePassword`, `keyAlias`, and `keyPassword` from the root `local.properties`.
2. If `local.properties` does not provide complete credentials, read `storeFile`, `storePassword`, `keyAlias`, and `keyPassword` from the root `key.properties`.
3. If complete credentials are found, use them for `release`; `debug` also reuses them by default.
4. If no complete credentials are found, `release` falls back to debug signing by default.

## Publish To Maven Local

```bash
./gradlew publishToMavenLocal
```

## Publish To Gradle Plugin Portal

Create a Gradle Plugin Portal account, generate an API key, and add the credentials to your user-level Gradle properties file:

```properties
gradle.publish.key=YOUR_KEY
gradle.publish.secret=YOUR_SECRET
```

The default path is `~/.gradle/gradle.properties`.

For CI, the credentials can also be provided as environment variables:

```bash
GRADLE_PUBLISH_KEY=YOUR_KEY
GRADLE_PUBLISH_SECRET=YOUR_SECRET
```

Publish with:

```bash
./gradlew publishPlugins
```

The first publication enters the Gradle Plugin Portal manual review process.

Current portal metadata:

- `website`: `https://github.com/josephuszhou/AndroidSignPlugin`
- `vcsUrl`: `https://github.com/josephuszhou/AndroidSignPlugin.git`
- `tags`: `android`, `signing`, `keystore`, `local-properties`
- `configurationCache`: `false`

The published namespace follows the GitHub-owned `io.github.JosephusZhou` pattern required for plugin portal review.

## License

AndroidSignPlugin is released under the MIT License.
