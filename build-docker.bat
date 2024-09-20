@echo off

gradlew.bat wrapper

call gradlew.bat clean test
set GRADLE_EXIT_CODE=%ERRORLEVEL%
if %GRADLE_EXIT_CODE% neq 0 (
    exit /b %GRADLE_EXIT_CODE%
)

for /f "tokens=2 delims=:" %%a in ('.\gradlew.bat properties ^| findstr "^version:"') do (
    set "PROJECT_VER=%%a"
)
set "PROJECT_VER=%PROJECT_VER: =%"

for /f "delims=" %%i in ('git symbolic-ref --short HEAD') do set "GIT_BRANCH=%%i"
docker build --build-arg version=%PROJECT_VER% -t .server:%PROJECT_VER%-%GIT_BRANCH% .