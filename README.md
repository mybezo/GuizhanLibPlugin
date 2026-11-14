# GuizhanLibPlugin

[![Build Status](https://builds.guizhanss.com/api/badge/ybw0014/GuizhanLibPlugin/master/latest)](https://builds.guizhanss.com/ybw0014/GuizhanLibPlugin/master)
[![Maven Central](https://img.shields.io/maven-central/v/net.guizhanss/GuizhanLibPlugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.guizhanss%22%20AND%20a:%22GuizhanLibPlugin%22)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-blue.svg)](LICENSE)

A library plugin for [Slimefun](https://github.com/Slimefun/Slimefun4) addon development.

GuizhanLibPlugin bundles the [GuizhanLib](https://github.com/ybw0014/GuizhanLib) library into a single server plugin, so multiple addons can share one copy of the library instead of shading it individually. It also provides an auto-updater with a selectable update site.

## Features

- **Shared library** – ships GuizhanLib so addons can depend on it at runtime.
- **Auto update** – keeps GuizhanLibPlugin (and addons that use its updater) up to date.
- **Selectable update site** – choose between the global site and a mainland China mirror.
- **Localization helpers** – English display names for materials, entities, enchantments, biomes, potion effects, mob variants and more, read from the server's own `en_us.json` (downloaded automatically as a fallback).

## Requirements

| Requirement | Version |
|-------------|---------|
| Java        | 17+     |
| Server      | Spigot / Paper 1.18+ |
| Slimefun    | Required |

## Download

Download the latest build from the build site: [**builds.guizhanss.com**](https://builds.guizhanss.com/ybw0014/GuizhanLibPlugin/master)

## Usage

### For server owners

1. Download the jar from the link above.
2. Put it into your server's `plugins` folder.
3. Restart the server.

### Configuration

After the first start, `plugins/GuizhanLibPlugin/config.yml` is generated:

```yaml
# Whether to enable the auto update module
auto-update: true

updater:
  # The website used for auto update
  #   global - (default) deployed on Cloudflare Pages: https://builds.guizhanss.com/
  #   cn     - mirror hosted in mainland China (Tencent Cloud): https://builds.guizhanss.cn/
  location: global

  # Updater message language: en_US, zh_CN
  lang: en_US

# Whether to print debug messages in the console
debug: false
```

### For developers

Add GuizhanLibPlugin as a `provided` / `compileOnly` dependency. Replace `VERSION` with the latest version shown in the Maven Central badge above.

**Maven**

```xml
<dependency>
    <groupId>net.guizhanss</groupId>
    <artifactId>GuizhanLibPlugin</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
</dependency>
```

**Gradle (Kotlin DSL)**

```kotlin
dependencies {
    compileOnly("net.guizhanss:GuizhanLibPlugin:VERSION")
}
```

#### Declaring the dependency in `plugin.yml`

As a hard dependency:

```yaml
depend:
  - GuizhanLibPlugin
```

Or as a soft dependency:

```yaml
softdepend:
  - GuizhanLibPlugin
```

If you use a soft dependency, check that the plugin is enabled at the start of your `onEnable` method:

```java
if (!getServer().getPluginManager().isPluginEnabled("GuizhanLibPlugin")) {
    getLogger().log(Level.SEVERE, "This plugin requires GuizhanLibPlugin to run!");
    getLogger().log(Level.SEVERE, "Download it here: https://50l.cc/gzlib");
    getServer().getPluginManager().disablePlugin(this);
    return;
}
```

## Building from source

```bash
git clone https://github.com/ybw0014/GuizhanLibPlugin.git
cd GuizhanLibPlugin
./gradlew shadowJar
```

The built jar is in `build/libs/`. To start a local test server with Slimefun preinstalled, run `./gradlew runServer`.

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).
