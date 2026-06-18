#!/bin/bash
# 用户模块测试 - 阶段B/C：userInfo + update + update-password(含bug验证与密码恢复)
BASE="http://localhost:8081"
WS="/c/Users/zzw43/Desktop/AcademicCredentialVerificationSystem/api-test-workspace"
OUT="$WS/result_user_B.txt"
: > "$OUT"
source "$WS/tokens.env"
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

# ---------- /user/userInfo ----------
req U1 "查询用户信息-带COL token(密码应被隐藏)" -X GET "$BASE/user/userInfo" -H "Authorization: $COL_TOKEN"
req U2 "异常-无token(应401)"   -X GET "$BASE/user/userInfo"
req U3 "异常-非法token(应401)" -X GET "$BASE/user/userInfo" -H "Authorization: invalid.token.xxx"

# ---------- /user/update ----------
req UP1 "正常更新基本信息-COLLEGE" -X PUT "$BASE/user/update" -H "Authorization: $COL_TOKEN" -H "Content-Type: application/json" -d @"$WS/update_col.json"
req UP2 "异常-真实姓名缺失"   -X PUT "$BASE/user/update" -H "Authorization: $COL_TOKEN" -H "Content-Type: application/json" -d '{"username":"apitest_stu","idCard":"110101199003078913"}'
req UP3 "异常-身份证格式错误" -X PUT "$BASE/user/update" -H "Authorization: $COL_TOKEN" -H "Content-Type: application/json" -d '{"username":"apitest_stu","realName":"TestName","idCard":"123"}'
req UP4 "异常-身份证号已存在" -X PUT "$BASE/user/update" -H "Authorization: $COL_TOKEN" -H "Content-Type: application/json" -d '{"username":"apitest_stu","realName":"TestName","idCard":"350603200209010031"}'

log "[DB] apitest_col 更新后记录:"
log "$($MYSQL "SELECT id,username,real_name,id_card FROM user WHERE username='apitest_col';" 2>/dev/null)"
log ""

# ---------- /user/update-password (验证全表改密码缺陷) ----------
log "########## update-password 测试 ##########"
log "[DB] 测试前全表密码快照:"
log "$($MYSQL "SELECT id,username,password FROM user;" 2>/dev/null)"
log ""
req PW1 "正常修改密码-apitest_col" -X PUT "$BASE/user/update-password?username=apitest_col&password=newpass99" -H "Authorization: $COL_TOKEN"
log "[DB] 测试后全表密码快照(观察非目标用户密码是否被连带修改):"
log "$($MYSQL "SELECT id,username,password FROM user;" 2>/dev/null)"
log "[INFO] 字符串 newpass99 的标准MD5 = $(printf '%s' 'newpass99' | md5sum | cut -d' ' -f1)"
log ""
req PW2 "异常-用户不存在" -X PUT "$BASE/user/update-password?username=ghost_user_zzz&password=newpass99" -H "Authorization: $COL_TOKEN"

# ---------- 立即恢复现有用户(id=1,2,3)密码 ----------
mysql -uroot -p123456 -D acvsystem < "$WS/restore_password.sql" 2>/dev/null
log "[RESTORE] 已执行 restore_password.sql"
log "[DB] 恢复后 id=1,2,3 密码(应恢复为123456的MD5:4297f44b13955235245b2497399d7a93):"
log "$($MYSQL "SELECT id,username,password FROM user WHERE id IN (1,2,3);" 2>/dev/null)"

echo "DONE_B"
