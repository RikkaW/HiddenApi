# Hidden API

![Maven Central](https://img.shields.io/maven-central/v/dev.rikka.hidden/stub)

Unify all hidden APIs used by Rikka apps into a library.

## Usage

- Setup [HiddenApiRefinePlugin](https://github.com/RikkaApps/HiddenApiRefinePlugin)
- `implementation 'dev.rikka.hidden:compat:<version>'`
- `compileOnly 'dev.rikka.hidden:stub:<version>'`

## Changelog

### 3.4.0

- Add some DisplayManager APIs

### 3.2.0

- Add "historical ops" related APIs

### 3.1.0

- Add `android.provider.DeviceConfig`

### 3.0.0

- (Breaking change) Update `UidObserverAdapter`
- Add `ActivityManager#UID_OBSERVER_` values

### 2.3.2

- Add `checkSignatures` `checkUidSignatures`
