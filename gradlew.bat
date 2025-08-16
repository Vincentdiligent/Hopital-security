@rem
@rem  Gradle start up script for Windows
@rem

setlocal

set DIRNAME=%~dp0
set APP_BASE_NAME=%~n0
set STARTER_MAIN_CLASS=org.gradle.wrapper.GradleWrapperMain
set CLASSPATH=%DIRNAME%gradle\wrapper\gradle-wrapper.jar

if defined JAVA_HOME (
    set JAVA_EXE=%JAVA_HOME%\bin\java.exe
) else (
    set JAVA_EXE=java.exe
)

"%JAVA_EXE%" -cp "%CLASSPATH%" %STARTER_MAIN_CLASS% %*
endlocal
