#!/bin/sh

mkdir "${ANDROID_HOME}/licenses" || true

echo -n -e "\n8933bad161af4178b1185d1a37fbf41ea5269c55" > "$ANDROID_HOME/licenses/android-sdk-license"
echo -n -e "\nd56f5187479451eabf01fb78af6dfcb131a6481e" > "$ANDROID_HOME/licenses/android-sdk-license"

echo -n -e "\n84831b9409646a918e30573bab4c9c91346d8abd" > "$ANDROID_HOME/licenses/android-sdk-preview-license"

echo -n -e "\n601085b94cd77f0b54ff86406957099ebe79c4d6" > "$ANDROID_HOME/licenses/android-googletv-license"

echo -n -e "\n33b6a2b64607f11b759f320ef9dff4ae5c47d97a" > "$ANDROID_HOME/licenses/google-gdk-license"

echo -n -e "\ne9acab5b5fbb560a72cfaecce8946896ff6aab9d" > "$ANDROID_HOME/licenses/mips-android-sysimage-license"

echo -n -e "\nd975f751698a77b662f1254ddbeed3901e976f5a" > "$ANDROID_HOME/licenses/intel-android-extra-license"


./gradlew assembleDebug
