param(
    [string]$BaseUrl = "http://127.0.0.1:8080",
    [string]$Username = "admin",
    [string]$Password = $env:API_PASSWORD
)
if (-not $Password) { throw "请通过 -Password 或 API_PASSWORD 环境变量提供测试管理员密码。请针对独立数据库副本运行。" }
$previousBase = $env:API_BASE
$previousUser = $env:API_USER
$previousPassword = $env:API_PASSWORD
Push-Location $PSScriptRoot
try {
    $env:API_BASE = $BaseUrl
    $env:API_USER = $Username
    $env:API_PASSWORD = $Password
    node scripts/api-regression.mjs
    if ($LASTEXITCODE -ne 0) { throw "接口回归失败，详见 .local/api-regression-results.json" }
} finally {
    $env:API_BASE = $previousBase
    $env:API_USER = $previousUser
    $env:API_PASSWORD = $previousPassword
    Pop-Location
}
