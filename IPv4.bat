@echo off
for /f "tokens=2 delims=:" %%f in ('ipconfig ^| findstr /i "IPv4.*Address.*:"') do (
    for /f "tokens=1-4 delims=." %%g in ("%%f") do (
        set "ip=%%g.%%h.%%i.%%j"
        setlocal enabledelayedexpansion
        set "ip=!ip: =!"
        set "url=http://!ip!:3000/graphql"
        echo !url!
	pause
        endlocal
        exit /b
    )
)