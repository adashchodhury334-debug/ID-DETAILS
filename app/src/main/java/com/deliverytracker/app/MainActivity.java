package com.deliverytracker.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WebView webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        
        String htmlData = "<!DOCTYPE html><html lang='hi'><head><meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<title>Delivery Tracker</title><style>" +
            "* { box-sizing: border-box; font-family: system-ui, -apple-system, sans-serif; margin: 0; padding: 0; }" +
            "body { background-color: #121212; color: #e0e0e0; padding: 16px; }" +
            "header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; padding-top: 10px; }" +
            "h1 { font-size: 20px; color: #4caf50; margin: 0; }" +
            ".btn-lock { background: #333; color: #4caf50; border: 1px solid #4caf50; padding: 6px 12px; border-radius: 6px; font-size: 12px; font-weight: bold; cursor: pointer; }" +
            ".card { background: #1e1e1e; padding: 16px; border-radius: 12px; margin-bottom: 16px; border: 1px solid #333; }" +
            ".card-title { font-size: 15px; font-weight: bold; margin-bottom: 12px; color: #fff; display: flex; justify-content: space-between; align-items: center; }" +
            "textarea { width: 100%; height: 110px; padding: 12px; border-radius: 8px; border: 1px solid #444; background: #2a2a2a; color: #fff; font-size: 13px; outline: none; margin-bottom: 10px; resize: none; }" +
            "input { width: 100%; padding: 14px; border-radius: 8px; border: 1px solid #444; background: #2a2a2a; color: #fff; font-size: 15px; outline: none; margin-bottom: 10px; }" +
            "input:focus, textarea:focus { border-color: #4caf50; box-shadow: 0 0 8px rgba(76, 175, 80, 0.3); }" +
            ".btn-add { background: #4caf50; color: #fff; border: none; font-weight: bold; padding: 12px; width: 100%; border-radius: 8px; cursor: pointer; font-size: 14px; }" +
            ".btn-danger { background: #c62828; color: #fff; border: none; font-weight: bold; padding: 10px; width: 100%; border-radius: 8px; cursor: pointer; font-size: 13px; margin-top: 10px; }" +
            ".order-item { background: #252525; padding: 14px; border-radius: 10px; margin-bottom: 10px; border-left: 5px solid #4caf50; display: flex; justify-content: space-between; align-items: center; }" +
            ".order-info { font-size: 14px; line-height: 1.6; word-break: break-all; }" +
            ".track-id { font-size: 13px; color: #aaa; }" +
            ".order-id { font-size: 16px; font-weight: bold; color: #81c784; margin-top: 2px; }" +
            ".action-btns { display: flex; gap: 8px; flex-shrink: 0; margin-left: 10px; }" +
            ".btn-copy { background: #333; color: #fff; border: 1px solid #555; padding: 8px 12px; border-radius: 6px; font-size: 13px; cursor: pointer; }" +
            ".btn-delete { background: #c62828; color: #fff; border: none; padding: 8px 10px; border-radius: 6px; font-size: 13px; cursor: pointer; }" +
            ".no-result { text-align: center; color: #888; padding: 15px 0; font-size: 14px; }" +
            ".status-info { text-align: center; font-size: 13px; color: #4caf50; margin-bottom: 8px; font-weight: bold; }" +
            "#admin-panel { display: none; }" +
            "</style></head><body>" +
            "<header>" +
            "<h1>Delivery Tracker</h1>" +
            "<button class='btn-lock' id='lock-btn' onclick='toggleAdmin()'>🔒 Admin Login</button>" +
            "</header>" +

            "<div class='card' id='admin-panel'>" +
            "<div class='card-title'>📋 Admin Panel (Bulk Import / Data Reset)</div>" +
            "<textarea id='bulk-input' placeholder='Google Sheet से ऑर्डर्स कॉपी करके यहाँ पेस्ट करें...'></textarea>" +
            "<button class='btn-add' onclick='bulkImport()'>⚡ Import All Orders</button>" +
            "<button class='btn-danger' onclick='clearAllOrders()'>⚠️ Clear All Saved Data</button>" +
            "</div>" +

            "<div class='card'>" +
            "<div class='card-title'>🔍 Search Order</div>" +
            "<input type='text' id='search-input' placeholder='Type last 4-5 digits...' oninput='renderOrders()' style='margin-bottom:0;'>" +
            "</div>" +

            "<div class='card'>" +
            "<div class='card-title'>📦 Order Details</div>" +
            "<div id='status-text' class='status-info'></div>" +
            "<div id='orders-list'></div>" +
            "</div>" +

            "<script>" +
            "let orders = [];" +
            "let isAdmin = sessionStorage.getItem('is_admin') === 'true';" +
            "const ADMIN_PIN = '7602';" +

            "function updateAdminUI() {" +
            "if(isAdmin) {" +
            "document.getElementById('admin-panel').style.display = 'block';" +
            "document.getElementById('lock-btn').innerText = '🔓 Logout Admin';" +
            "} else {" +
            "document.getElementById('admin-panel').style.display = 'none';" +
            "document.getElementById('lock-btn').innerText = '🔒 Admin Login';" +
            "}" +
            "}" +

            "function loadSavedOrders() {" +
            "let saved = localStorage.getItem('local_orders');" +
            "if(saved) { try { orders = JSON.parse(saved); } catch(e){ orders = []; } }" +
            "else { orders = []; }" +
            "document.getElementById('status-text').innerText = '✅ Total Active Orders: ' + orders.length;" +
            "updateAdminUI();" +
            "renderOrders();" +
            "}" +

            "function toggleAdmin() {" +
            "if(!isAdmin) {" +
            "let pin = prompt('Enter Admin PIN:');" +
            "if(pin === ADMIN_PIN) {" +
            "isAdmin = true;" +
            "sessionStorage.setItem('is_admin', 'true');" +
            "updateAdminUI();" +
            "alert('Admin Mode Activated!');" +
            "renderOrders();" +
            "} else if(pin !== null) { alert('Wrong PIN!'); }" +
            "} else {" +
            "isAdmin = false;" +
            "sessionStorage.removeItem('is_admin');" +
            "updateAdminUI();" +
            "renderOrders();" +
            "}" +
            "}" +

            "function bulkImport() {" +
            "let rawText = document.getElementById('bulk-input').value.trim();" +
            "if(!rawText) { alert('पेस्ट बॉक्स खाली है!'); return; }" +
            "let lines = rawText.split(/\\r?\\n/);" +
            "let addedCount = 0;" +
            "for(let i = 0; i < lines.length; i++) {" +
            "let line = lines[i].trim();" +
            "if(!line) continue;" +
            "let parts = line.split(/[\\t,]/).map(p => p.trim());" +
            "if(parts.length >= 2 && parts[0] && parts[1]) {" +
            "if(!parts[0].toUpperCase().includes('TRACKING')) {" +
            "orders.push({ trackingId: parts[0], orderId: parts[1] });" +
            "addedCount++;" +
            "}" +
            "}" +
            "}" +
            "localStorage.setItem('local_orders', JSON.stringify(orders));" +
            "document.getElementById('bulk-input').value = '';" +
            "alert('सफलतापूर्वक ' + addedCount + ' ऑर्डर्स इंपोर्ट हो गए!');" +
            "location.reload();" +
            "}" +

            "function clearAllOrders() {" +
            "if(confirm('क्या आप पूरा डेटा डिलीट करना चाहते हैं?')) {" +
            "localStorage.removeItem('local_orders');" +
            "alert('सारा डेटा डिलीट हो गया है!');" +
            "location.reload();" +
            "}" +
            "}" +

            "function deleteOrder(index) {" +
            "orders.splice(index, 1);" +
            "localStorage.setItem('local_orders', JSON.stringify(orders));" +
            "location.reload();" +
            "}" +

            "function copyToClipboard(text) {" +
            "navigator.clipboard.writeText(text);" +
            "alert('Order ID Copied: ' + text);" +
            "}" +

            "function renderOrders() {" +
            "const list = document.getElementById('orders-list');" +
            "const search = document.getElementById('search-input').value.trim().toLowerCase();" +
            "list.innerHTML = '';" +

            "if(search === '') {" +
            "list.innerHTML = '<div class=\"no-result\">सर्च करने के लिए लास्ट 4-5 डिजिट डालें</div>';" +
            "return;" +
            "}" +

            "let count = 0;" +
            "for(let idx = 0; idx < orders.length; idx++) {" +
            "let item = orders[idx];" +
            "let track = String(item.trackingId).toLowerCase();" +
            "let order = String(item.orderId).toLowerCase();" +
            "if (track.includes(search) || order.includes(search)) {" +
            "count++;" +
            "const div = document.createElement('div'); div.className = 'order-item';" +
            "let deleteBtnHtml = isAdmin ? `<button class='btn-delete' onclick='deleteOrder(${idx})'>🗑️</button>` : '';" +
            "div.innerHTML = `<div class='order-info'><div class='track-id'>Track: ${item.trackingId}</div><div class='order-id'>Order ID: ${item.orderId}</div></div><div class='action-btns'><button class='btn-copy' onclick='copyToClipboard(\"${item.orderId}\")'>Copy</button>${deleteBtnHtml}</div>`;" +
            "list.appendChild(div);" +
            "if(count >= 20) break;" +
            "}" +
            "}" +
            "if(count === 0) list.innerHTML = '<div class=\"no-result\">❌ No matching Tracking ID found</div>';" +
            "}" +

            "loadSavedOrders();" +
            "</script></body></html>";

        webView.loadDataWithBaseURL("https://app.local", htmlData, "text/html", "UTF-8", null);
        setContentView(webView);
    }
}
