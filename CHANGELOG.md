# Changelog

## 1.0.0 - 2026-05-26

- Initial stable release.
- Read signing credentials from root `local.properties` first.
- Fall back to root `key.properties` when local signing credentials are incomplete or absent.
- Configure Android application `release` signing automatically.
- Optionally reuse release signing for `debug` builds.
- Optionally fall back to debug signing when release signing credentials are absent.
