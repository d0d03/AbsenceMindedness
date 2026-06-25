@echo off
:: set path to ISCC.exe
:: if no path is provided fall back to system PATH
set ISCC_PATH=%~1
if "%ISCC_PATH%"=="" set ISCC_PATH=ISCC

if "%ISCC_PATH%"=="ISCC" (
    where ISCC >nul 2>nul
    if errorlevel 1 (
        echo ERROR: ISCC.exe not found on PATH.
        echo Either add Inno Setup to your system PATH, or pass the full path as an argument:
        echo    package.bat "<path_to_install_folder>\ISCC.exe"
        pause
        exit /b 1
    )
) else (
    if not exist "%ISCC_PATH%" (
        echo ERROR: ISCC.exe not found at %ISCC_PATH%
        pause
        exit /b 1
    )
)

:: Build the fat jar first
call mvn clean package -DskipTests

for /f "delims=" %%v in ('call mvn help:evaluate "-Dexpression=project.version" -q -DforceStdout') do set APP_VERSION=%%v

:: Create the app-image
echo Bundling app image, version: %APP_VERSION%

if exist dist rmdir /s /q dist

jpackage ^
    --type app-image ^
    --name AbsenceMindedness ^
    --app-version %APP_VERSION% ^
    --input target ^
    --main-jar absencemindedness-%APP_VERSION%.jar ^
    --main-class hr.absencemindedness.AbsenceMindednessApplication ^
    --dest dist ^
    --icon src\main\resources\favicon.ico

:: Build the installer with version
echo Building installer for version: %APP_VERSION%
if "%ISCC_PATH%"=="ISCC" (
    ISCC /DMyAppVersion=%APP_VERSION% installer.iss
) else (
    "%ISCC_PATH%" /DMyAppVersion=%APP_VERSION% installer.iss
)

pause