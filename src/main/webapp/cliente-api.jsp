<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gameverse - Cliente consumidor REST</title>
    <style>
        :root{color-scheme:dark;--bg:#0b1220;--panel:#172235;--panel2:#1e2d44;--blue:#3b82f6;--cyan:#38bdf8;--muted:#9caec5;--line:#304158;--red:#ef4444;--green:#22c55e}
        *{box-sizing:border-box}body{margin:0;background:var(--bg);color:#f8fafc;font-family:Arial,sans-serif;min-height:100vh}
        header{background:#111c2e;border-bottom:1px solid var(--line);padding:18px clamp(18px,5vw,64px);display:flex;justify-content:space-between;align-items:center;gap:18px;flex-wrap:wrap}
        .logo{font-size:25px;font-weight:800;color:#60a5fa}.logo span{color:white}nav{display:flex;gap:18px;flex-wrap:wrap}a{color:#93c5fd;text-decoration:none}a:hover{text-decoration:underline}
        main{max-width:1180px;margin:34px auto;padding:0 20px}.intro{margin-bottom:24px}.intro h1{margin:0 0 8px;font-size:clamp(26px,4vw,36px)}.intro p{color:var(--muted);margin:0;line-height:1.5}
        .status{display:flex;align-items:center;gap:9px;margin:18px 0;padding:12px 15px;background:var(--panel);border:1px solid var(--line);border-radius:10px;color:var(--muted)}.dot{width:9px;height:9px;border-radius:50%;background:#f59e0b}.dot.ok{background:var(--green)}.dot.bad{background:var(--red)}
        .layout{display:grid;grid-template-columns:minmax(280px,350px) 1fr;gap:20px;align-items:start}.panel{background:var(--panel);border:1px solid var(--line);border-radius:14px;padding:20px}.panel h2{font-size:19px;margin:0 0 16px}
        label{display:block;color:#cbd5e1;font-size:13px;margin:12px 0 6px}input,textarea{width:100%;background:#0e192a;border:1px solid #40516a;color:white;border-radius:8px;padding:11px;font:inherit}textarea{resize:vertical;min-height:75px}input:focus,textarea:focus{outline:2px solid #3b82f6;border-color:transparent}
        .grid2{display:grid;grid-template-columns:1fr 1fr;gap:10px}.actions{display:flex;gap:9px;flex-wrap:wrap;margin-top:16px}button{border:0;border-radius:8px;padding:10px 14px;font-weight:700;cursor:pointer;background:var(--blue);color:white}button:hover{filter:brightness(1.12)}button.secondary{background:#334155}button.danger{background:#b91c1c}button.small{padding:7px 10px;font-size:12px}
        .toolbar{display:flex;justify-content:space-between;align-items:center;gap:12px;flex-wrap:wrap;margin-bottom:14px}.toolbar input{max-width:260px}.table-wrap{overflow-x:auto}table{width:100%;border-collapse:collapse;min-width:650px}th,td{text-align:left;padding:12px 10px;border-bottom:1px solid var(--line);font-size:14px}th{color:#93c5fd;font-size:12px;text-transform:uppercase;letter-spacing:.04em}td{color:#e2e8f0}.muted{color:var(--muted)}.row-actions{display:flex;gap:6px;flex-wrap:wrap}.empty{text-align:center;padding:35px;color:var(--muted)}
        .notice{display:none;margin:14px 0;padding:12px;border-radius:8px;line-height:1.45}.notice.show{display:block}.notice.error{background:#451a1a;color:#fecaca;border:1px solid #7f1d1d}.notice.success{background:#123524;color:#bbf7d0;border:1px solid #166534}.api-note{margin-top:18px;color:var(--muted);font-size:12px;line-height:1.6}
        @media(max-width:850px){.layout{grid-template-columns:1fr}main{margin-top:24px}}
    </style>
</head>
<body>
<header>
    <div class="logo">🎮 Game<span>verse</span></div>
    <nav><a href="${pageContext.request.contextPath}/">Inicio</a><a href="${pageContext.request.contextPath}/catalogo">Catálogo</a><a href="${pageContext.request.contextPath}/inventario.xhtml">Inventario</a></nav>
</header>
<main>
    <section class="intro">
        <h1>Cliente consumidor de API REST</h1>
        <p>Este módulo consume los servicios REST de Gameverse mediante solicitudes HTTP y respuestas JSON. Las operaciones se ejecutan contra el inventario del servidor.</p>
    </section>
    <div class="status"><span id="statusDot" class="dot"></span><span id="apiStatus">Comprobando conexión con la API...</span><span class="muted" style="margin-left:auto">GET · POST · PUT · PATCH · DELETE</span></div>
    <div id="notice" class="notice" role="status" aria-live="polite"></div>
    <div class="layout">
        <section class="panel">
            <h2 id="formTitle">Registrar videojuego</h2>
            <form id="gameForm">
                <input type="hidden" id="gameId">
                <label for="titulo">Título</label><input id="titulo" maxlength="100" required minlength="2" placeholder="Ej. The Last Frontier">
                <label for="descripcion">Descripción</label><textarea id="descripcion" maxlength="500" required minlength="10" placeholder="Describe brevemente el videojuego..."></textarea>
                <div class="grid2">
                    <div><label for="precio">Precio ($)</label><input id="precio" type="number" min="0" max="99999999.99" step="0.01" required></div>
                    <div><label for="stock">Stock</label><input id="stock" type="number" min="0" step="1" required></div>
                </div>
                <label for="categoria">Categoría</label><input id="categoria" maxlength="50" required minlength="2" placeholder="Ej. Acción">
                <label for="imagenUrl">URL de imagen (opcional)</label><input id="imagenUrl" type="url" placeholder="https://...">
                <div class="actions"><button type="submit" id="saveButton">Guardar videojuego</button><button type="button" class="secondary" id="cancelButton" hidden>Cancelar edición</button></div>
            </form>
            <p class="api-note">Al guardar se utiliza POST para crear o PUT para actualizar. El formulario envía JSON a <code>/api/videojuegos</code>.</p>
        </section>
        <section class="panel">
            <div class="toolbar"><h2 style="margin:0">Inventario consumido desde REST</h2><div class="actions" style="margin:0"><input id="search" type="search" placeholder="Buscar videojuego..." aria-label="Buscar videojuego"><button type="button" class="secondary" id="reloadButton">Actualizar</button></div></div>
            <div class="table-wrap"><table><thead><tr><th>ID</th><th>Videojuego</th><th>Precio</th><th>Stock</th><th>Acciones</th></tr></thead><tbody id="gamesBody"><tr><td colspan="5" class="empty">Cargando inventario...</td></tr></tbody></table></div>
            <p class="api-note">Consultar utiliza GET. Los botones de stock utilizan PATCH y eliminar utiliza DELETE. Cada acción vuelve a consultar la API para reflejar los datos actuales.</p>
        </section>
    </div>
</main>
<script>
(() => {
    'use strict';
    const API = '${pageContext.request.contextPath}/api/videojuegos';
    const $ = id => document.getElementById(id);
    let games = [];
    const notice = (message, type='success') => { $('notice').textContent=message; $('notice').className='notice show '+type; };
    const clearNotice = () => { $('notice').className='notice'; $('notice').textContent=''; };
    async function request(url, options={}) {
        const response = await fetch(url, { ...options, headers: { ...(options.body ? {'Content-Type':'application/json'} : {}), ...(options.headers||{}) } });
        if (response.status === 204) return null;
        const text = await response.text();
        let data = null;
        if (text) { try { data=JSON.parse(text); } catch { throw new Error('El servidor no devolvió una respuesta JSON válida.'); } }
        if (!response.ok) throw new Error(data?.message || `Error HTTP ${response.status}`);
        return data;
    }
    function setConnection(ok, message) { $('statusDot').className='dot '+(ok?'ok':'bad'); $('apiStatus').textContent=message; }
    function escapeHtml(value) { return String(value ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c])); }
    function render() {
        const term=$('search').value.trim().toLowerCase();
        const filtered=games.filter(g => `${g.titulo} ${g.categoria} ${g.id}`.toLowerCase().includes(term));
        $('gamesBody').innerHTML = filtered.length ? filtered.map(g => `<tr>
            <td>${g.id}</td><td><strong>${escapeHtml(g.titulo)}</strong><br><span class="muted">${escapeHtml(g.categoria||'Sin categoría')}</span></td>
            <td>$${Number(g.precio).toFixed(2)}</td><td>${g.stock}</td><td><div class="row-actions">
            <button class="small secondary" data-action="view" data-id="${g.id}">Ver</button><button class="small" data-action="edit" data-id="${g.id}">Editar</button>
            <button class="small secondary" data-action="minus" data-id="${g.id}" ${g.stock<=0?'disabled':''}>−1</button><button class="small secondary" data-action="plus" data-id="${g.id}">+1</button>
            <button class="small danger" data-action="delete" data-id="${g.id}">Eliminar</button></div></td></tr>`).join('') : '<tr><td colspan="5" class="empty">No hay videojuegos que coincidan con la búsqueda.</td></tr>';
    }
    async function loadGames(showMessage=false) {
        try { games=await request(API); setConnection(true,`API REST conectada · ${games.length} videojuego(s) recibidos`); render(); if(showMessage) notice('Inventario actualizado desde la API REST.'); }
        catch(e) { setConnection(false,'No se pudo conectar con la API REST'); $('gamesBody').innerHTML=`<tr><td colspan="5" class="empty">${escapeHtml(e.message)}. Verifica que Tomcat esté iniciado y que la API esté desplegada.</td></tr>`; if(showMessage) notice(e.message,'error'); }
    }
    function resetForm() { $('gameForm').reset(); $('gameId').value=''; $('formTitle').textContent='Registrar videojuego'; $('saveButton').textContent='Guardar videojuego'; $('cancelButton').hidden=true; }
    function fillForm(g) { $('gameId').value=g.id; $('titulo').value=g.titulo||''; $('descripcion').value=g.descripcion||''; $('precio').value=g.precio; $('stock').value=g.stock; $('categoria').value=g.categoria||''; $('imagenUrl').value=g.imagenUrl||''; $('formTitle').textContent='Editar videojuego #'+g.id; $('saveButton').textContent='Guardar cambios'; $('cancelButton').hidden=false; $('titulo').focus(); }
    $('gameForm').addEventListener('submit', async e => {
        e.preventDefault(); clearNotice();
        const id=$('gameId').value;
        const payload={titulo:$('titulo').value.trim(),descripcion:$('descripcion').value.trim(),precio:Number($('precio').value),stock:Number($('stock').value),categoria:$('categoria').value.trim(),imagenUrl:$('imagenUrl').value.trim()||null};
        try { await request(id?`${API}/${id}`:API,{method:id?'PUT':'POST',body:JSON.stringify(payload)}); notice(id?'Videojuego actualizado correctamente mediante PUT.':'Videojuego registrado correctamente mediante POST.'); resetForm(); await loadGames(); }
        catch(err) { notice(err.message,'error'); }
    });
    $('gamesBody').addEventListener('click', async e => {
        const button=e.target.closest('button[data-action]'); if(!button) return;
        const id=Number(button.dataset.id), action=button.dataset.action;
        try {
            if(action==='view') { const g=await request(`${API}/${id}`); notice(`GET ${API}/${id}: ${g.titulo} · ${g.categoria} · $${Number(g.precio).toFixed(2)} · Stock: ${g.stock}`); }
            if(action==='edit') { const g=await request(`${API}/${id}`); fillForm(g); clearNotice(); }
            if(action==='plus'||action==='minus') { const delta=action==='plus'?1:-1; await request(`${API}/${id}/stock`,{method:'PATCH',body:JSON.stringify({cambio:delta})}); notice(`Stock del videojuego #${id} ajustado mediante PATCH (${delta>0?'+':''}${delta}).`); await loadGames(); }
            if(action==='delete' && confirm(`¿Seguro que deseas eliminar el videojuego #${id}?`)) { await request(`${API}/${id}`,{method:'DELETE'}); notice(`Videojuego #${id} eliminado mediante DELETE.`); if($('gameId').value===String(id)) resetForm(); await loadGames(); }
        } catch(err) { notice(err.message,'error'); }
    });
    $('reloadButton').addEventListener('click',()=>loadGames(true)); $('search').addEventListener('input',render); $('cancelButton').addEventListener('click',resetForm);
    loadGames();
})();
</script>
</body>
</html>
