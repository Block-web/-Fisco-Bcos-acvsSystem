#!/bin/bash
# 用户模块测试 - 阶段A：注册 + 登录取 token
BASE="http://localhost:8081"
WS="/c/Users/zzw43/Desktop/AcademicCredentialVerificationSystem/api-test-workspace"
OUT="$WS/result_user_A.txt"
: > "$OUT"

log(){ echo "$@" >> "$OUT"; }

req(){
  local id="$1"; local desc="$2"; shift 2
  log "===== [$id] $desc ====="
  local resp body code
  resp=$(curl -s -w $'\n__HTTP__%{http_code}' "$@")
  body="${resp%$'\n__HTTP__'*}"
  code="${resp##*__HTTP__}"
  log "HTTP: $code"
  log "BODY: $body"
  log ""
}

# ---------- 注册接口 /user/register ----------
req R1 "正常注册-院校COLLEGE"  -X POST "$BASE/user/register" -d "username=apitest_col&password=test12345&role=COLLEGE"
req R2 "正常注册-学生STUDENT"  -X POST "$BASE/user/register" -d "username=apitest_stu&password=test12345&role=STUDENT"
req R3 "正常注册-企业COMPANY"  -X POST "$BASE/user/register" -d "username=apitest_com&password=test12345&role=COMPANY"
req R4 "异常-用户名已存在"      -X POST "$BASE/user/register" -d "username=apitest_col&password=test12345&role=COLLEGE"
req R5 "异常-用户名过短(<5位)" -X POST "$BASE/user/register" -d "username=abc&password=test12345&role=STUDENT"
req R6 "异常-非法角色ADMIN"    -X POST "$BASE/user/register" -d "username=apitest_bad&password=test12345&role=ADMIN"
req R7 "异常-密码过短(<5位)"   -X POST "$BASE/user/register" -d "username=apitest_pwd&password=123&role=STUDENT"

# ---------- 登录接口 /user/login ----------
log "===== [L1] 正常登录-COLLEGE ====="
COL_RESP=$(curl -s "$BASE/user/login" -d "username=apitest_col&password=test12345&role=COLLEGE")
log "BODY: $COL_RESP"
COL_TOKEN=$(echo "$COL_RESP" | sed -n 's/.*"data":"\([^"]*\)".*/\1/p')
log "提取COL_TOKEN长度: ${#COL_TOKEN}"
log ""

STU_RESP=$(curl -s "$BASE/user/login" -d "username=apitest_stu&password=test12345&role=STUDENT")
STU_TOKEN=$(echo "$STU_RESP" | sed -n 's/.*"data":"\([^"]*\)".*/\1/p')
log "===== [L1b] 正常登录-STUDENT ====="
log "BODY: $STU_RESP"
log ""

COM_RESP=$(curl -s "$BASE/user/login" -d "username=apitest_com&password=test12345&role=COMPANY")
COM_TOKEN=$(echo "$COM_RESP" | sed -n 's/.*"data":"\([^"]*\)".*/\1/p')
log "===== [L1c] 正常登录-COMPANY ====="
log "BODY: $COM_RESP"
log ""

req L2 "异常-密码错误"     -X POST "$BASE/user/login" -d "username=apitest_col&password=wrongpwd1&role=COLLEGE"
req L3 "异常-角色不匹配"   -X POST "$BASE/user/login" -d "username=apitest_col&password=test12345&role=STUDENT"
req L4 "异常-用户不存在"   -X POST "$BASE/user/login" -d "username=nobodyxxx&password=test12345&role=STUDENT"

# 保存 token 供后续阶段使用
{
  echo "COL_TOKEN=$COL_TOKEN"
  echo "STU_TOKEN=$STU_TOKEN"
  echo "COM_TOKEN=$COM_TOKEN"
} > "$WS/tokens.env"
log "tokens.env 已保存"

echo "DONE_A"
