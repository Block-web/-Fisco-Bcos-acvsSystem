#!/bin/bash
# 证书模块测试 - 阶段1：issue 颁发证书(上链)
BASE="http://localhost:8081"
WS="/c/Users/zzw43/Desktop/AcademicCredentialVerificationSystem/api-test-workspace"
OUT="$WS/result_cert_issue.txt"
: > "$OUT"
source "$WS/tokens.env"
MYSQL="mysql -uroot -p123456 -D acvsystem -N -e"

log(){ echo "$@" >> "$OUT"; }
req(){
  local id="$1"; local desc="$2"; shift 2
  log "===== [$id] $desc ====="
  local resp body code t
  resp=$(curl -s -w $'\n__HTTP__%{http_code}__T__%{time_total}' "$@")
  t="${resp##*__T__}"
  body="${resp%__T__*}"; body="${body%$'\n__HTTP__'*}"
  code="${resp##*__HTTP__}"; code="${code%%__T__*}"
  log "HTTP: $code  耗时: ${t}s"
  log "BODY: $body"
  log ""
}

# ---------- /certificate/issue ----------
req C1 "正常颁发证书(上链)" --max-time 150 -X POST "$BASE/certificate/issue" -H "Authorization: $COL_TOKEN" -H "Content-Type: application/json" -d @"$WS/issue_cert.json"
req C2 "异常-缺少必填字段studentName" --max-time 30 -X POST "$BASE/certificate/issue" -H "Authorization: $COL_TOKEN" -H "Content-Type: application/json" -d @"$WS/issue_cert_bad.json"

log "[DB] 新增的测试证书(id>4):"
log "$($MYSQL "SELECT CONCAT('id=',id,' | cert_no=',cert_no,' | college=',college_id,' | status=',status,' | tx=',IFNULL(LEFT(tx_hash,20),'NULL')) FROM certificate WHERE id>4;" 2>/dev/null)"

# 把新证书cert_no保存,供后续 get/revoke/verify 使用
NEWCERT=$($MYSQL "SELECT cert_no FROM certificate WHERE id>4 ORDER BY id DESC LIMIT 1;" 2>/dev/null)
echo "NEWCERT=$NEWCERT" > "$WS/newcert.env"
log "[INFO] 保存测试证书号: $NEWCERT"

echo "DONE_ISSUE"
