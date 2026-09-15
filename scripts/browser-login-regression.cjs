// PLAYWRIGHT_MODULE may point to an existing playwright-core installation.
const {chromium}=require(process.env.PLAYWRIGHT_MODULE || 'playwright-core');
const assert=require('node:assert/strict');
(async()=>{
 const browser=await chromium.launch({headless:true,...(process.env.CHROME_PATH?{executablePath:process.env.CHROME_PATH}:{})});
 try {
  const page=await browser.newPage();
  await page.goto('http://127.0.0.1:5173/ningshang-admin/login');
  await page.getByPlaceholder('用户名',{exact:true}).fill(process.env.API_USER||'admin');
  await page.getByPlaceholder('密码',{exact:true}).fill(process.env.API_PASSWORD);
  const loginResponse=page.waitForResponse(r=>r.url().endsWith('/api/admin/login')&&r.request().method()==='POST');
  await page.getByRole('button',{name:'登 录'}).click();
  const response=await loginResponse;
  assert.equal(response.status(),200);
  assert.equal((await response.request().allHeaders()).origin,'http://127.0.0.1:5173');
  await page.waitForURL('**/ningshang-admin/dashboard');
  await page.getByText('网站基本设置',{exact:true}).waitFor();
  const user=await page.evaluate(()=>JSON.parse(localStorage.getItem('admin_user')));
  assert.equal(user.groupId,1);
  console.log('PASS real browser login: Origin 127.0.0.1:5173, HTTP 200, dashboard, settings menu, groupId');
  // Browser preflight to the API port uses the same allowlist.
  const direct=await page.evaluate(async()=>{
    const response=await fetch('http://127.0.0.1:8080/api/admin/login',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({username:'cors-nonexistent',password:'wrong'})});
    return {status:response.status,code:(await response.json()).code};
  });
  assert.equal(direct.status,401);assert.equal(direct.code,401);
  console.log('PASS browser cross-origin preflight and readable HTTP 401 for invalid credentials');
 } finally {await browser.close();}
})().catch(e=>{console.error(e);process.exitCode=1});
