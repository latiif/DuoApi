# DuoApi
[![](https://img.shields.io/github/languages/top/llusx/DuoApi.svg)]()
[![](https://img.shields.io/github/last-commit/llusx/DuoApi)]()
[![](https://img.shields.io/maintenance/yes/2019)]()



Unofficial Java API for Duolingo
---

This project was started in 2017. 
Throughout the years, Duolingo changed their API significantly, rendering older versions of the code useless.

## Get it
 
[![](https://jitpack.io/v/llusx/DuoApi.svg)](https://jitpack.io/#llusx/DuoApi)

You can start using DuoApi in your project via JitPack.

First, add the repo
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Then, add the dependency
```$xslt
<dependency>
    <groupId>com.github.llusx</groupId>
    <artifactId>DuoApi</artifactId>
    <version>0.1-beta</version>
</dependency>
```
Finally, enjoy!

## DuoApi
Main class containing functionality for various operations allowed by Duolingo API.
## DuolingoProfileInfo
New wrapper dataclass for information extracted from the `DuoApi` class.
## AudioMapper
Singleton class to fetch url of the audio file for a word