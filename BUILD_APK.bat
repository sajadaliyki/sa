@echo off
setlocal
title YOOK APK BUILDER

echo ======================================
echo          YOOK APK BUILDER
echo ======================================
echo.

set "SDK="

if defined ANDROID_SDK_ROOT if exist "%ANDROID_SDK_ROOT%\platforms" set "SDK=%ANDROID_SDK_ROOT%"
if not defined SDK if defined ANDROID_HOME if exist "%ANDROID_HOME%\platforms" set "SDK=%ANDROID_HOME%"
if not defined SDK if exist "%LOCALAPPDATA%\Android\Sdk\platforms" set "SDK=%LOCALAPPDATA%\Android\Sdk"
if not defined SDK if exist "C:\Android\Sdk\platforms" set "SDK=C:\Android\Sdk"

if not defined SDK (
    echo [ERROR] Android SDK not found.
    echo.
    echo In Android Studio open:
    echo File ^> Settings ^> Languages ^& Frameworks ^> Android SDK
    echo.
    echo Recommended SDK path:
    echo %LOCALAPPDATA%\Android\Sdk
    echo.
    pause
    exit /b 1
)

echo SDK: %SDK%
>local.properties echo sdk.dir=%SDK:\=\\%

echo.
echo Building...
call gradlew.bat assembleDebug
if errorlevel 1 (
    echo.
    echo [ERROR] Build failed.
    pause
    exit /b 1
)

if exist "app\build\outputs\apk\debug\app-debug.apk" (
    copy /Y "app\build\outputs\apk\debug\app-debug.apk" "Yook.apk" >nul
    echo.
    echo ======================================
    echo SUCCESS: Yook.apk created
    echo ======================================
) else (
    echo [ERROR] APK output was not found.
)
echo.
pause
