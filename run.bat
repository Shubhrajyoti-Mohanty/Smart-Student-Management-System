@echo off
echo Compiling the project...
if not exist "bin" mkdir bin
javac -d bin src/com/sms/model/*.java src/com/sms/util/*.java src/com/sms/dao/*.java src/com/sms/service/*.java src/com/sms/handler/*.java src/com/sms/Main.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)
echo Compilation successful.
echo Starting Smart Student Management System Server...
java -cp "bin;mysql-connector-j-9.6.0.jar" com.sms.Main
pause
