# AndroidSignPlugin Agent Guide

This file defines collaboration rules for AI agents working in the `AndroidSignPlugin` project.

## Project Scope

- Project type: standalone Gradle plugin.
- Language: Kotlin.
- Primary plugin id: `io.github.JosephusZhou.androidsigning`.
- Primary package: `io.github.JosephusZhou`.
- Goal: provide reusable Android application signing configuration from `local.properties` or `key.properties`.

## Important Files

- Plugin implementation: `src/main/kotlin/io/github/JosephusZhou/AndroidSigningPlugin.kt`
- Build and publishing configuration: `build.gradle.kts`
- Gradle settings: `settings.gradle.kts`
- Public documentation: `README.md`
- Release notes: `CHANGELOG.md`
- License: `LICENSE`

## Development Rules

- Keep the plugin independent from the parent `DogInventory` project.
- Do not reference app-specific code, packages, resources, or build logic from `DogInventory`.
- Preserve the published coordinates unless the user explicitly requests a change:
  - `group`: `io.github.JosephusZhou`
  - `plugin id`: `io.github.JosephusZhou.androidsigning`
  - `artifact`: `AndroidSignPlugin`
- Do not commit local signing files, credentials, keystores, or environment files.
- Do not add real signing secrets to `local.properties`, `key.properties`, README examples, tests, or build scripts.
- Prefer small, backwards-compatible changes for public extension properties.
- If changing plugin behavior, update `README.md` and `CHANGELOG.md` in the same task.
- If changing publishing metadata, verify it still matches Gradle Plugin Portal requirements.

## Validation

Run validation from the `AndroidSignPlugin` directory:

```bash
./gradlew build
```

Before publishing, also verify the publish task:

```bash
./gradlew publishPlugins --dry-run
```

Use real publishing credentials only from user-level Gradle properties or CI secrets. Never store them in this repository.

## Release Checklist

- Version in `build.gradle.kts` is final, not `SNAPSHOT`.
- README usage example matches the published version.
- CHANGELOG contains an entry for the release.
- LICENSE is present.
- `./gradlew build` passes.
- `./gradlew publishPlugins --dry-run` passes.
- No `build/`, `.gradle/`, keystore, or local secret file is staged.
