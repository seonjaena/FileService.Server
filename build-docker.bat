@echo off

for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /format:list') do set "TIMESTAMP=%%I"
set "TIMESTAMP=%TIMESTAMP:~0,14%"

for /f "delims=" %%i in ('git symbolic-ref --short HEAD') do set "GIT_BRANCH=%%i"

docker build -t file.server:%GIT_BRANCH%-%TIMESTAMP% .