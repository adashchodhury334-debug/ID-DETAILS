package com.deliverytracker.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        
        webView.setWebViewClient(new WebViewClient());
        
        String htmlData = "<!DOCTYPE html><html lang='hi'><head><meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<title>Delivery Tracker</title><style>" +
            "* { box-sizing: border-box; font-family: system-ui, -apple-system, sans-serif; margin: 0; padding: 0; }" +
            "body { background-color: #121212; color: #e0e0e0; padding: 16px; }" +
            "h1 { text-align: center; font-size: 20px; margin-bottom: 16px; color: #4caf50; padding-top: 10px; }" +
            ".card { background: #1e1e1e; padding: 16px; border-radius: 12px; margin-bottom: 16px; border: 1px solid #333; }" +
            ".card-title { font-size: 15px; font-weight: bold; margin-bottom: 12px; color: #fff; display: flex; justify-content: space-between; align-items: center; }" +
            "input { width: 100%; padding: 14px; border-radius: 8px; border: 1px solid #444; background: #2a2a2a; color: #fff; font-size: 15px; outline: none; transition: 0.3s; margin-bottom: 10px; }" +
            "input:focus { border-color: #4caf50; box-shadow: 0 0 8px rgba(76, 175, 80, 0.3); }" +
            ".btn-add { background: #4caf50; color: #fff; border: none; font-weight: bold; padding: 12px; width: 100%; border-radius: 8px; cursor: pointer; font-size: 14px; margin-top: 4px; }" +
            ".btn-add:active { background: #388e3c; }" +
            ".order-item { background: #252525; padding: 14px; border-radius: 10px; margin-bottom: 10px; border-left: 5px solid #4caf50; display: flex; justify-content: space-between; align-items: center; }" +
            ".order-info { font-size: 14px; line-height: 1.6; word-break: break-all; }" +
            ".track-id { font-size: 13px; color: #aaa; }" +
            ".order-id { font-size: 16px; font-weight: bold; color: #81c784; margin-top: 2px; }" +
            ".action-btns { display: flex; gap: 8px; flex-shrink: 0; margin-left: 10px; }" +
            ".btn-copy { background: #333; color: #fff; border: 1px solid #555; padding: 8px 12px; border-radius: 6px; font-size: 13px; cursor: pointer; }" +
            ".btn-delete { background: #c62828; color: #fff; border: none; padding: 8px 10px; border-radius: 6px; font-size: 13px; cursor: pointer; }" +
            ".no-result { text-align: center; color: #888; padding: 15px 0; font-size: 14px; }" +
            ".status-info { text-align: center; font-size: 13px; color: #4caf50; margin-bottom: 8px; font-weight: bold; }" +
            ".toggle-btn { background: none; border: 1px solid #4caf50; color: #4caf50; font-size: 11px; padding: 4px 8px; border-radius: 4px; cursor: pointer; }" +
            "</style></head><body>" +
            "<h1>Delivery Tracker</h1>" +
            
            "<div class='card'>" +
            "<div class='card-title'>➕ Add New Order</div>" +
            "<input type='text' id='add-track' placeholder='Enter Tracking ID (e.g. FMPC497...)' />" +
            "<input type='text' id='add-order' placeholder='Enter Order ID (e.g. OD9769...)' />" +
            "<button class='btn-add' onclick='addOrder()'>Save Order</button>" +
            "</div>" +

            "<div class='card'>" +
            "<div class='card-title'>🔍 Search Order</div>" +
            "<input type='text' id='search-input' placeholder='Type last 4-5 digits...' oninput='renderOrders()' style='margin-bottom:0;'>" +
            "</div>" +

            "<div class='card'>" +
            "<div class='card-title'>📦 Order Details <button class='toggle-btn' onclick='toggleShowAll()'>Show All / Search</button></div>" +
            "<div id='status-text' class='status-info'></div>" +
            "<div id='orders-list'></div>" +
            "</div>" +

            "<script>" +
            "let orders = [];" +
            "let showAllMode = false;" +
            "function loadSavedOrders() {" +
            "let saved = localStorage.getItem('local_orders');" +
            "if(saved) { try { orders = JSON.parse(saved); } catch(e){ orders = []; } }" +
            "document.getElementById('status-text').innerText = '✅ Saved Orders: ' + orders.length;" +
            "renderOrders();" +
            "}" +
            "function addOrder() {" +
            "let t = document.getElementById('add-track').value.trim();" +
            "let o = document.getElementById('add-order').value.trim();" +
            "if(!t || !o) { alert('Please enter both Tracking ID and Order ID'); return; }" +
            "orders.push({ trackingId: t, orderId: o });" +
            "localStorage.setItem('local_orders', JSON.stringify(orders));" +
            "document.getElementById('add-track').value = '';" +
            "document.getElementById('add-order').value = '';" +
            "alert('Order Saved Successfully!');" +
            "loadSavedOrders();" +
            "}" +
            "function deleteOrder(index) {" +
            "if(confirm('Are you sure you want to delete this order?')) {" +
            "orders.splice(index, 1);" +
            "localStorage.setItem('local_orders', JSON.stringify(orders));" +
            "loadSavedOrders();" +
            "}" +
            "}" +
            "function toggleShowAll() {" +
            "showAllMode = !showAllMode;" +
            "renderOrders();" +
            "}" +
            "function copyToClipboard(text) {" +
            "navigator.clipboard.writeText(text);" +
            "alert('Order ID Copied: ' + text);" +
            "}" +
            "function renderOrders() {" +
            "const list = document.getElementById('orders-list');" +
            "const search = document.getElementById('search-input').value.trim().toLowerCase();" +
            "list.innerHTML = '';" +
            "if(showAllMode) {" +
            "if(orders.length === 0) { list.innerHTML = '<div class=\"no-result\">No saved orders available.</div>'; return; }" +
            "orders.forEach((item, idx) => {" +
            "const div = document.createElement('div'); div.className = 'order-item';" +
            "div.innerHTML = `<div class='order-info'><div class='track-id'>Track: ${item.trackingId}</div><div class='order-id'>Order ID: ${item.orderId}</div></div><div class='action-btns'><button class='btn-copy' onclick='copyToClipboard(\"${item.orderId}\")'>Copy</button><button class='btn-delete' onclick='deleteOrder(${idx})'>🗑️</button></div>`;" +
            "list.appendChild(div);" +
            "});" +
            "return;" +
            "}" +
            "if(search === '') {" +
            "list.innerHTML = '<div class=\"no-result\">Type last 4-5 digits above to find Order ID</div>';" +
            "return;" +
            "}" +
            "let count = 0;" +
            "orders.forEach((item, idx) => {" +
            "let track = String(item.trackingId).toLowerCase();" +
            "let order = String(item.orderId).toLowerCase();" +
            "if (track.includes(search) || order.includes(search)) {" +
            "count++;" +
            "const div = document.createElement('div'); div.className = 'order-item';" +
            "div.innerHTML = `<div class='order-info'><div class='track-id'>Track: ${item.trackingId}</div><div class='order-id'>Order ID: ${item.orderId}</div></div><div class='action-btns'><button class='btn-copy' onclick='copyToClipboard(\"${item.orderId}\")'>Copy</button><button class='btn-delete' onclick='deleteOrder(${idx})'>🗑️</button></div>`;" +
            "list.appendChild(div); }" +
            "});" +
            "if(count === 0) list.innerHTML = '<div class=\"no-result\">❌ No matching Tracking ID found</div>';" +
            "}" +
            "loadSavedOrders();" +
            "</script></body></html>";

        webView.loadDataWithBaseURL("https://app.local", htmlData, "text/html", "UTF-8", null);
        setContentView(webView);
    }
}
