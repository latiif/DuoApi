# DuoApi
[![](https://img.shields.io/github/languages/top/latiif/DuoApi.svg)]()
[![](https://img.shields.io/github/last-commit/latiif/DuoApi)]()
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
```xml
<dependency>
    <groupId>com.github.llusx</groupId>
    <artifactId>DuoApi</artifactId>
    <version>0.1-beta</version>
</dependency>
```
Finally, enjoy!

# Usage

## DuoApi
Main class containing functionality for various operations allowed by Duolingo API.

### Creating a DuoApi instance
You need to authenticate with Duolingo before being able to use DuoApi.

```java
DuoApi duoApi = new DuoApi("<USERNAME>","<PASSWORD>");
```
It throws an `java.lang.IllegalArgumentException: Incorrect username or password` if the credentials are incorrect.
### Getting current language
```java
duoApi.getCurrentLanguage();
```
This will return a string of the current language's abbreviation. For example `es` for Spanish. 

### Get the full name of the language
```java
duoApi.getLanguageFromAbbreviation("sv");
```
This will print the full name of the language. In this example you'll get `Swedish`.


### Get a list of languages a user is learning
```java
duoApi.getLanguages(true);
```
Will return a `List<String>` where the elements are abbreviations.
```java
duoApi.getLanguages(false);
```
Will return a `List<String>` where the elements are the full names.
Example:
```java
List<String> fullLangs = duoApi.getLanguages(false);
System.out.println("Full names: " + fullLangs);
List<String> abbrLangs = duoApi.getLanguages(true);
System.out.println("Abbreviated: " + abbrLangs);
```
prints out:

``Full names: [Esperanto, Spanish, Russian, Dutch, French, German, Hebrew, Swedish, Arabic]``

``Abbreviated: [eo, es, ru, dn, fr, de, he, sv, ar]``

### Get known words
Use `getKnownWords()` to get a `List<String>` of known words in the current language being learned.
```java 
duoApi.getKnownWords();
```

## DuolingoProfileInfo
New wrapper dataclass for information extracted from the `DuoApi` class.
## AudioMapper
Singleton class to fetch url of the audio file for a word
