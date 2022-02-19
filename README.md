<h1 align="center">GhostJS - To Evaluate JavaScript in Android</h1>

<p align="center">
    <a href="https://jitpack.io/#gumify/ghostjs"><img src="https://img.shields.io/jitpack/v/github/gumify/ghostjs?style=for-the-badge" alt="Release"></a>
    <a href="https://travis-ci.com/gumify/ghostjs"><img src="https://img.shields.io/travis/com/gumify/ghostjs/master?style=for-the-badge" alt="Build Status"></a>
    <a href="https://github.com/gumify/ghostjs/blob/master/LICENSE.txt"><img src="https://img.shields.io/github/license/gumify/ghostjs.svg?style=for-the-badge" alt="License"></a>
<!--     <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/gumify/ghostjs?logo=GitHub&style=for-the-badge"> -->
    <img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/gumify/ghostjs?logo=GitHub&style=for-the-badge">
    <a href="https://github.com/gumify/ghostjs/issues"><img alt="GitHub open issues" src="https://img.shields.io/github/issues/gumify/ghostjs?style=for-the-badge"></a>
</p>

### Getting Started

Add it in your root build.gradle at the end of repositories

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```gradle
implementation "com.github.gumify:ghostjs:$ghost_version"
```

Create an icecream instance

```kotlin
val ghostjs = Ghostjs()
```

### Usage

```kotlin
ghostjs.executeFile("test.js")
scope.launch {
    val output: String? = ghostjs.eval("""return dio;""")
    Log.d("Ghostjs", output.toString())
}
```

```kotlin
ghostjs.loadUrl("https://www.google.com")
```
