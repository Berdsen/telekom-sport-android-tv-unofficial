#!/bin/sh

mkdir "${ANDROID_HOME}/licenses" || true
echo -n -e "\n8933bad161af4178b1185d1a37fbf41ea5269c55" > "$ANDROID_SDK/licenses/android-sdk-license"
echo -n -e "\n84831b9409646a918e30573bab4c9c91346d8abd" > "$ANDROID_SDK/licenses/android-sdk-preview-license"

./gradlew assembleDebug
