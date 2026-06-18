#!/bin/bash
# get-certificate正常路径补测 + revoke撤销 + 撤销后FAIL闭环
BASE="http://localhost:8081"
WS="/c/Users/zzw43/Desktop/AcademicCredentialVerificationSystem/api-test-workspace"
OUT="$WS/result_revoke.txt"
: > "$OUT"
source "$WS/tokens.env"
source "$WS/newcert.env"   # NEWCERT
TESTHASH="8f257842b96d4c055793be516dedd01562590d49e84444537e9fac667df2b063"
MYSQL="mysql -uroot -p123456 -D acvsystem -N -e"

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

# ---------- get-certificate 正常路径(显式UTF-8百分号编码 测试学生apitest) ----------
req G1b "get-certificate正常路径-显式UTF-8编码查询" -X GET "$BASE/certificate/get-certificate?studentName=%E6%B5%8B%E8%AF%95%E5%AD%A6%E7%94%9Fapitest" -H "Authorization: $STU_TOKEN"

# ---------- /certificate/revoke 撤销测试证书 ----------
req RV1 "撤销测试证书(上链)" --max-time 120 -X POST "$BASE/certificate/revoke" -H "Authorization: $COL_TOKEN" -H "Content-Type: text/plain" --data "$NEWCERT"
log "[DB] 撤销后测试证书状态(status应为0):"
log "$($MYSQL "SELECT CONCAT('cert_no=',cert_no,' status=',status) FROM certificate WHERE cert_no='$NEWCERT';" 2>/dev/null)"
log ""

# ---------- 撤销后再验证 → FAIL 闭环(优雅FAIL路径) ----------
req SQL3 "数据库验证-已撤销证书应FAIL" -X POST "$BASE/verifyLog/getVerifyLogSql?companyName=6" -H "Authorization: $COM_TOKEN" -H "Content-Type: application/json" -d "{\"certNo\":\"$NEWCERT\",\"fileHash\":\"$TESTHASH\",\"collegeId\":4}"
req BC3 "区块链验证-已撤销证书应FAIL" --max-time 120 -X POST "$BASE/verifyLog/getVerifyLog?companyName=6" -H "Authorization: $COM_TOKEN" -H "Content-Type: application/json" -d "{\"certNo\":\"$NEWCERT\",\"fileHash\":\"$TESTHASH\",\"collegeId\":4}"

# ---------- revoke 边界:撤销不存在的证书 ----------
req RV2 "撤销不存在的证书(边界)" --max-time 120 -X POST "$BASE/certificate/revoke" -H "Authorization: $COL_TOKEN" -H "Content-Type: text/plain" --data "C99999999notexist"

echo "DONE_REVOKE"
