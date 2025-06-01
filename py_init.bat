@echo off
chcp 65001
setlocal

REM 현재 스크립트 경로 기준
set "PROJECT_DIR=%~dp0"
cd /d "%PROJECT_DIR%"
if exist venv\Scripts\activate.bat (
    echo [ℹ] venv 폴더에 이미 가상환경이 존재합니다.
    echo [⚠] 삭제 후 다시 실행시켜 주세요.
    echo -----------------------------------------------
    echo 아무 키나 입력하여 종료하세요...
    pause > nul
    exit /b
)
REM venv 폴더가 없으면 생성
if not exist venv (
    echo [ℹ] venv 폴더가 존재하지 않습니다.
    echo 가상환경 생성 중...
    python -m venv venv
    echo.
    echo [✔] 가상환경 생성 완료: %PROJECT_DIR%venv
) else (
    echo [ℹ] venv 폴더가 존재합니다.
    echo 가상환경 생성 중...
    python -m venv venv
    echo.
    echo [✔] 가상환경 생성 완료: %PROJECT_DIR%venv
)

REM 활성화 가능한지 확인 후 pip 실행
if exist venv\Scripts\activate.bat (
    call venv\Scripts\activate.bat
    echo 가상환경 활성화 성공.
    pip install -r scripts_py\requirements.txt
) else (
    echo [❌] 가상환경 활성화 실패: activate.bat가 없습니다.
)

REM requirements.txt가 있으면 설치
if exist scripts_py\requirements.txt (
    echo.
    echo [↓] requirements.txt를 기반으로 패키지 설치 중...
    pip install -r scripts_py\requirements.txt
    echo.
    echo [✔] 패키지 설치 완료.
) else (
    echo.
    echo [⚠] scripts_py\requirements.txt 파일이 없습니다. 패키지 설치를 생략합니다.
)

echo.
echo [🏁] 가상환경 위치: %PROJECT_DIR%venv
echo [✅] 설정 완료. Python 실행 준비됨.
echo.

REM 종료 대기
echo -----------------------------------------------
echo 작업이 완료되었습니다.
echo 아무 키나 입력하여 종료하세요...
pause > nul
exit /b
