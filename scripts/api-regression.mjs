// Run against an isolated database copy. Only records created by this run are deleted.
// API_BASE=http://127.0.0.1:8080 API_USER=admin API_PASSWORD=... node scripts/api-regression.mjs
import fs from 'node:fs';
import assert from 'node:assert/strict';
const base = process.env.API_BASE || 'http://127.0.0.1:8080';
const run = `audit-${Date.now()}`;
const results = [], cleanup = [];
const plain = value => String(value ?? '').replace(/<[^>]*>/g, '').trim();
const rows = (resource, data) => resource === 'news' ? (data?.list || []) : (data || []);
let token;
const check = (name, condition, detail='') => {
  results.push({name,pass:!!condition,detail});
  console.log(`${condition?'PASS':'FAIL'} ${name}${condition?'':` ${detail}`}`);
};
async function req(method, route, data, expected=200, auth=token, extra={}) {
  const headers = {Origin:'http://127.0.0.1:5173',...(auth?{Authorization:`Bearer ${auth}`}:{})};
  let body;
  if (data instanceof FormData) body=data;
  else if(data!==undefined) {headers['Content-Type']='application/json';body=JSON.stringify(data);}
  const response=await fetch(base+route,{method,headers:{...headers,...extra},body,signal:AbortSignal.timeout(15000)});
  const text=await response.text(); let json; try{json=JSON.parse(text)}catch{}
  check(`${method} ${route} => ${expected}`,response.status===expected && (!route.startsWith('/api/') || json?.code===expected),`HTTP ${response.status} code ${json?.code} ${json?.message||text.slice(0,120)}`);
  return json;
}
async function create(resource,data) {
  const result=await req('POST',`/api/admin/${resource}`,data);
  if(result?.data?.id) cleanup.push([resource,result.data.id]);
  return result?.data;
}
async function remove(resource,id) {
  await req('DELETE',`/api/admin/${resource}/${id}`);
  const index=cleanup.findIndex(([r,i])=>r===resource&&i===id); if(index>=0)cleanup.splice(index,1);
}
try {
  // Public data contracts, detail lookup and missing resources.
  for(const route of ['home','news','team','honors','milestones','content','subsidiaries','jobs']) {
    const result=await req('GET','/api/'+route);
    check(`public ${route} data shape`,route==='home'?Array.isArray(result?.data?.news)&&Array.isArray(result?.data?.subsidiaries):Array.isArray(result?.data));
  }
  const news=await req('GET','/api/news');
  if(news?.data?.[0]?.newsId) await req('GET','/api/news/'+encodeURIComponent(news.data[0].newsId));
  await req('GET',`/api/news/${run}`,undefined,404);
  await req('POST','/api/messages',{},400);
  await req('POST','/api/messages',{name:'访客',phone:'13800138000',email:'invalid',content:'咨询'},400);
  await req('POST','/api/messages',{name:run,phone:'13800138000',email:'audit@example.com',type:'other',content:'接口验证'});
  // Legacy server-rendered routes must render actual HTML rather than a JSON error with HTTP 200.
  const pages=['/','/news','/about-intro','/about-speech','/about-events','/about-team','/about-honor','/about-party','/about-culture','/industry','/industry-construction','/industry-software','/contact','/contact-message','/recruit','/recruit-job','/news-detail?id='+encodeURIComponent(news?.data?.[0]?.newsId||'missing')];
  for(const route of pages) {
    const response=await fetch(base+route); const text=await response.text();
    check(`HTML ${route}`,response.status===200&&text.includes('<html')&&!text.includes('Internal Server Error'),`HTTP ${response.status}`);
  }
  // Authentication, invalid body, and all protected resource collections.
  await req('POST','/api/admin/login',{username:run,password:'wrong'},401,null);
  const login=await req('POST','/api/admin/login',{username:process.env.API_USER||'admin',password:process.env.API_PASSWORD},200,null);
  token=login?.data?.token; assert.ok(token,'Admin login is required for remaining tests');
  const ownId=(await req('GET','/api/admin/admins')).data.find(a=>a.username===(process.env.API_USER||'admin')).id;
  for(const resource of ['news','jobs','messages','subsidiaries','team','honors','milestones','content','admins','menus','menus/flat','menus/mine','groups']) {
    await req('GET',`/api/admin/${resource}`,undefined,401,null);
    await req('GET',`/api/admin/${resource}`,undefined,401,'invalid');
    const data=await req('GET',`/api/admin/${resource}`);
    check(`admin ${resource} array`,Array.isArray(rows(resource, data?.data)));
    if(resource==='news') {
      check('admin news is ID descending', rows(resource, data.data).every((item, index, list) => index === 0 || list[index - 1].id > item.id));
      check('admin news pagination metadata', Number.isInteger(data.data.page) && Number.isInteger(data.data.size) && typeof data.data.total === 'number');
    }
    if(resource==='admins')check('admin passwords are not exposed',data.data.every(x=>!('password'in x)));
    if(resource==='menus/mine')check('page content and site settings menus are visible',data.data.flatMap(x=>[x,...(x.children||[])]).some(x=>x.path?.startsWith('/ningshang-admin/content/'))&&data.data.flatMap(x=>[x,...(x.children||[])]).some(x=>x.path==='/ningshang-admin/site-settings'));
  }
  const messages=(await req('GET','/api/admin/messages')).data;
  const message=messages.find(x=>x.name===run); if(message)cleanup.push(['messages',message.id]);
  check('public message persisted',!!message&&message.content==='接口验证');
  if(message)await remove('messages',message.id);
  const payloads={
    news:{title:run,newsId:run,category:'group',body:'<p>中文正文</p><img src="/uploads/audit.png"><script>alert(1)</script>'},
    jobs:{title:run,department:'技术部',headcount:'1人',sortOrder:99999},
    subsidiaries:{name:run,englishName:'AUDIT COMPANY',sortOrder:99999},
    team:{name:run,position:'测试',gradient:'linear-gradient(135deg,#1a365d 0%,#2c5282 100%)',sortOrder:99999},
    honors:{title:run,description:'验证荣誉',sortOrder:99999},
    milestones:{title:run,year:'2026.09',sortOrder:99999},
    content:{contentKey:run,title:'验证文案',content:'初始内容',sortOrder:99999}
  };
  for(const [resource,payload] of Object.entries(payloads)) {
    await req('POST',`/api/admin/${resource}`,{},400);
    const item=await create(resource,payload); if(!item?.id)continue;
    const persisted=rows(resource, (await req('GET',`/api/admin/${resource}`)).data).find(x=>x.id===item.id);
    const updatedPayload={...payload,[resource==='content'?'content':('name'in payload?'name':'title')]:run+'-updated'};
    const updated=(await req('PUT',`/api/admin/${resource}/${item.id}`,updatedPayload))?.data;
    check(`${resource} update preserves ID and creation time`,updated?.id===item.id && (resource==='content'||updated?.createdAt===persisted.createdAt));
    const list=rows(resource, (await req('GET',`/api/admin/${resource}`))?.data);
    check(`${resource} update persisted`,list?.some(x=>x.id===item.id&&plain(x[resource==='content'?'content':('name'in payload?'name':'title')])===run+'-updated'));
    if('sortOrder'in payload && resource!=='content')check(`${resource} respects sortOrder`,list?.at(-1)?.id===item.id);
    if(resource==='news') {
      check('news HTML sanitization retains relative images',updated?.body.includes('src="/uploads/audit.png"')&&!updated?.body.includes('<script'));
      await req('GET',`/api/news/${run}`);
      await req('POST','/api/admin/news',payload,409);
    }
    if(resource==='subsidiaries')check('englishName round-trip',updated?.englishName==='AUDIT COMPANY');
    await req('PUT',`/api/admin/${resource}/9223372036854775806`,payload,404);
    await remove(resource,item.id);
    await req('DELETE',`/api/admin/${resource}/${item.id}`,undefined,404);
  }
  // Menu structure, group relationship replacement, roles and status changes.
  const flat=(await req('GET','/api/admin/menus/flat')).data;
  const newsMenu=flat.find(x=>x.path==='/ningshang-admin/news');
  const actionPermissions=(await req('GET','/api/admin/permissions')).data;
  const newsListPermission=actionPermissions.find(x=>x.code==='news:list');
  const group=await create('groups',{name:run,status:1,sortOrder:90});
  const parent=await create('menus',{name:run,parentId:0,status:1});
  const child=await create('menus',{name:run+'-child',parentId:parent.id,path:'/ningshang-admin/news',status:1});
  check('empty parent is retained in menu management',(await req('GET','/api/admin/menus')).data.some(x=>x.id===parent.id));
  await req('PUT',`/api/admin/menus/${child.id}`,{...child,name:run+'-renamed'});
  await req('PUT',`/api/admin/menus/${parent.id}`,{...parent,parentId:child.id},400);
  await req('POST','/api/admin/menus/batch-delete',[],400);
  await req('PUT',`/api/admin/groups/${group.id}`,{...group,description:'更新组'});
  await req('PUT',`/api/admin/groups/${group.id}/menus`,[newsMenu.id,newsMenu.id]);
  await req('PUT',`/api/admin/groups/${group.id}/permissions`,[newsListPermission.id]);
  check('action permission assignment persists',(await req('GET',`/api/admin/groups/${group.id}/permissions`)).data.includes(newsListPermission.id));
  check('duplicate permissions are deduplicated',(await req('GET',`/api/admin/groups/${group.id}/menus`)).data.length===1);
  await req('PUT',`/api/admin/groups/${group.id}/menus`,[9223372036854775000],400);
  check('invalid assignment preserves previous permissions',(await req('GET',`/api/admin/groups/${group.id}/menus`)).data.includes(newsMenu.id));
  const editor=await create('admins',{username:run,password:'AuditPass123!',nickname:'验证编辑',groupId:group.id});
  await req('PUT',`/api/admin/admins/${editor.id}`,{nickname:'更新昵称',groupId:group.id});
  await req('POST','/api/admin/admins',{username:run,password:'AuditPass123!',groupId:group.id},400);
  await req('DELETE',`/api/admin/groups/${group.id}`,undefined,400);
  const editorToken=(await req('POST','/api/admin/login',{username:run,password:'AuditPass123!'},200,null)).data.token;
  await req('GET','/api/admin/news',undefined,200,editorToken);
  await req('GET','/api/admin/menus/mine',undefined,200,editorToken);
  for(const route of ['messages','groups','admins','menus','menus/flat'])await req('GET','/api/admin/'+route,undefined,403,editorToken);
  await req('POST','/api/admin/menus',{name:'forbidden',parentId:0},403,editorToken);
  await req('POST','/api/admin/menus/batch-delete',[child.id],403,editorToken);
  await req('PUT',`/api/admin/groups/${group.id}/menus`,[]);
  await req('GET','/api/admin/news',undefined,403,editorToken);
  await req('PUT',`/api/admin/groups/${group.id}/menus`,[newsMenu.id]);
  await req('PUT',`/api/admin/groups/${group.id}`,{...group,status:0});
  await req('GET','/api/admin/news',undefined,403,editorToken);
  await req('POST','/api/admin/login',{username:run,password:'AuditPass123!'},403,null);
  await req('PUT',`/api/admin/groups/${group.id}`,{...group,status:1});
  await req('POST','/api/admin/password',{newPassword:'NewAudit123!'},400,editorToken);
  await req('POST','/api/admin/password',{oldPassword:'wrong',newPassword:'NewAudit123!'},400,editorToken);
  await req('POST','/api/admin/password',{oldPassword:'AuditPass123!',newPassword:'NewAudit123!'},200,editorToken);
  await req('POST','/api/admin/login',{username:run,password:'AuditPass123!'},401,null);
  const newToken=(await req('POST','/api/admin/login',{username:run,password:'NewAudit123!'},200,null)).data.token;
  await req('POST','/api/admin/logout',undefined,200,newToken);
  await req('GET','/api/admin/news',undefined,401,newToken);
  const liveToken=(await req('POST','/api/admin/login',{username:run,password:'NewAudit123!'},200,null)).data.token;
  await remove('admins',editor.id);
  await req('GET','/api/admin/menus/mine',undefined,401,liveToken);
  await remove('groups',group.id);
  await req('POST','/api/admin/menus/batch-delete',[parent.id,child.id]);
  for(const id of [parent.id,child.id]){const i=cleanup.findIndex(([r,n])=>r==='menus'&&n===id);if(i>=0)cleanup.splice(i,1);}
  check('batch menu deletion removes parent and child',!(await req('GET','/api/admin/menus/flat')).data.some(x=>[parent.id,child.id].includes(x.id)));
  const single=await create('menus',{name:run+'-single',parentId:0,path:'/ningshang-admin/content',status:1});await remove('menus',single.id);
  await req('DELETE',`/api/admin/admins/${ownId}`,undefined,400);
  const superGroup=(await req('GET','/api/admin/groups')).data.find(x=>x.id===1);
  await req('PUT','/api/admin/groups/1',{...superGroup,status:0},400);
  await req('PUT',`/api/admin/admins/${ownId}`,{groupId:2},400);
  // File upload format and round-trip through the static upload handler.
  const badFile=new FormData();badFile.append('file',new Blob(['test']), 'test.txt');
  await req('POST','/api/admin/upload',badFile,400);
  const imageFile=new FormData();imageFile.append('file',new Blob([Buffer.from('iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+/lZsAAAAASUVORK5CYII=','base64')],{type:'image/png'}),'audit.png');
  const upload=await req('POST','/api/admin/upload',imageFile);
  if(upload?.data?.url){const r=await fetch(base+upload.data.url);check('uploaded image is readable',r.status===200&&r.headers.get('content-type')?.includes('image'));}
  await req('GET','/api/admin/news/not-a-number',undefined,405);
  const malformed=await fetch(base+'/api/admin/news',{method:'POST',headers:{Authorization:`Bearer ${token}`,'Content-Type':'application/json'},body:'{'});
  check('malformed JSON returns HTTP 400',malformed.status===400);
  await req('POST','/api/admin/logout',undefined,200);
  await req('GET','/api/admin/news',undefined,401);
} catch(e) { check('test execution completed',false,e.stack); }
finally {
  // Reauthenticate if the final logout invalidated the token. Cleanup only this run's exact IDs.
  if(cleanup.length){const response=await fetch(base+'/api/admin/login',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({username:process.env.API_USER||'admin',password:process.env.API_PASSWORD})});token=(await response.json()).data?.token;}
  for(const [resource,id] of cleanup.reverse()){try{await req('DELETE',`/api/admin/${resource}/${id}`)}catch(e){check(`cleanup ${resource}/${id}`,false,e.message)}}
  fs.mkdirSync('.local',{recursive:true});
  fs.writeFileSync('.local/api-regression-results.json',JSON.stringify({base,run,total:results.length,passed:results.filter(x=>x.pass).length,results},null,2));
  const failed=results.filter(x=>!x.pass);console.log(`TOTAL ${results.length} PASS ${results.length-failed.length} FAIL ${failed.length}`);
  process.exitCode=failed.length?1:0;
}
