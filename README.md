# ABC Adventure 🎈

A standalone Android learning game for children, built with Kotlin and Jetpack Compose.

## Module 1 — Alphabet A–Z

- Child enters name and age.
- Each letter is presented as a large, colorful, tappable target.
- Tapping a letter speaks:
  - the letter name
  - its phonics sound
  - the example word (for example, “A is for Apple”)
- The lesson automatically advances to the next letter after the spoken lesson.
- Child can replay the pronunciation.
- Progress is shown from A through Z.
- Completing Z gives a celebration screen and replay option.

## Tech

- Kotlin
- Jetpack Compose
- Material 3
- Android Text-to-Speech
- minSdk 26 / targetSdk 35
- Application ID: `com.abcadventure`

## Project separation

This is a completely separate project from the NetWorth application. Future learning modules can be added without sharing the NetWorth codebase.
