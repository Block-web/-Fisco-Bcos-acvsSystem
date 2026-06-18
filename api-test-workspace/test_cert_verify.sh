#!/bin/bash
# 证书查询 + 验证模块测试(证书 status=1 阶段)
BASE="http://localhost:8081"
WS="/c/Users/zzw43/Desktop/AcademicCredentialVerificationSystem/api-test-workspace"
OUT="$WS/result_cert_verify.txt"
: > "$OUT"
source "$WS/tokens.env"
source "$WS/newcert.env"   # NEWCERT
TESTHASH="8f257842b96d4c055793be516dedd01562590d49e84444537e9fac667df2b063"

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

log "测试证书号 NEWCERT=$NEWCERT"
log ""

# ---------- /certificate/get-certificate ----------
req G1 "按学生姓名查证书-存在" -G "$BASE/certificate/get-certificate" -H "Authorization: $STU_TOKEN" --data-urlencode "studentName=测试学生apitest"
req G2 "按学生姓名查证书-不存在(验证NPE)" -G "$BASE/certificate/get-certificate" -H "Authorization: $STU_TOKEN" --data-urlencode "studentName=ghoststudentzzz"

# ---------- /verifyLog/getVerifyLogSql (数据库验证) ----------
req SQL1 "数据库验证-命中正常证书PASS" -X POST "$BASE/verifyLog/getVerifyLogSql?companyName=6" -H "Authorization: $COM_TOKEN" -H "Content-Type: application/json" -d "{\"certNo\":\"$NEWCERT\",\"fileHash\":\"$TESTHASH\",\"collegeId\":4}"
req SQL2 "数据库验证-证书不存在(验证NPE)" -X POST "$BASE/verifyLog/getVerifyLogSql?companyName=6" -H "Authorization: $COM_TOKEN" -H "Content-Type: application/json" -d "{\"certNo\":\"NOTEXIST\",\"fileHash\":\"deadbeefdeadbeef\",\"collegeId\":4}"

# ---------- /verifyLog/getVerifyLog (区块链验证) ----------
req BC1 "区块链验证-三要素匹配PASS" --max-time 120 -X POST "$BASE/verifyLog/getVerifyLog?companyName=6" -H "Authorization: $COM_TOKEN" -H "Content-Type: application/json" -d "{\"certNo\":\"$NEWCERT\",\"fileHash\":\"$TESTHASH\",\"collegeId\":4}"
req BC2 "区块链验证-fileHash篡改(验证NPE)" --max-time 120 -X POST "$BASE/verifyLog/getVerifyLog?companyName=6" -H "Authorization: $COM_TOKEN" -H "Content-Type: application/json" -d "{\"certNo\":\"$NEWCERT\",\"fileHash\":\"0000000000000000000000000000000000000000000000000000000000000000\",\"collegeId\":4}"

echo "DONE_CERTVERIFY"
