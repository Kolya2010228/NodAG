#!/bin/sh

# Gradle wrapper script for Android builds
# This is a simplified wrapper - in production use the official Gradle wrapper

echo "NodAG Gradle Wrapper"
echo "===================="

if [ ! -f "app/build.gradle" ]; then
    echo "Error: build.gradle not found"
    exit 1
fi

case "$1" in
    build)
        echo "Building project..."
        # In real scenario: ./gradlew build
        echo "Build completed (simulated)"
        ;;
    test)
        echo "Running tests..."
        # In real scenario: ./gradlew test
        echo "Tests completed (simulated)"
        ;;
    assembleDebug)
        echo "Assembling debug APK..."
        # In real scenario: ./gradlew assembleDebug
        echo "Debug APK assembled (simulated)"
        ;;
    assembleRelease)
        echo "Assembling release APK..."
        # In real scenario: ./gradlew assembleRelease
        echo "Release APK assembled (simulated)"
        ;;
    clean)
        echo "Cleaning build..."
        # In real scenario: ./gradlew clean
        echo "Clean completed (simulated)"
        ;;
    *)
        echo "Usage: gradlew [build|test|assembleDebug|assembleRelease|clean]"
        ;;
esac
