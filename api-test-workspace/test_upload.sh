#!/bin/bash
# 文件上传模块测试 /upload
BASE="http://localhost:8081"
WS="/c/Users/zzw43/Desktop/AcademicCredentialVerificationSystem/api-test-workspace"
OUT="$WS/result_upload.txt"
: > "$OUT"
source "$WS/tokens.env"

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

# ---------- /upload ----------
req F1 "正常上传文件到OSS-带token" -X POST "$BASE/upload" -H "Authorization: $COL_TOKEN" -F "file=@$WS/apitest_upload.txt"
req F2 "异常-无token(应401)"       -X POST "$BASE/upload" -F "file=@$WS/apitest_upload.txt"
req F3 "边界-不携带文件参数"        -X POST "$BASE/upload" -H "Authorization: $COL_TOKEN"

# ---------- 顺带验证中文 realName 是否正确落库(经接口返回UTF-8) ----------
req VR "验证-apitest_col中文realName写入" -X GET "$BASE/user/userInfo" -H "Authorization: $COL_TOKEN"

echo "DONE_UPLOAD"
