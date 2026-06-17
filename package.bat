@echo off

:: Build the fat jar first
call mvn clean package -DskipTests

if exist dist rmdir /s /q dist
:: Create the app-image
echo Running jpackage...
jpackage ^
    --type app-image ^
    --name AbsenceMindedness ^
    --app-version 0.1.0 ^
    --input target ^
    --main-jar absencemindedness-0.1.0.jar ^
    --main-class hr.absencemindedness.AbsenceMindednessApplication ^
    --dest dist ^
    --icon src\main\resources\favicon.ico