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
        
        String csvUrl = "https://docs.google.com/spreadsheets/d/e/2PACX-1vTNHE2l_d6VIDLvWCB7nL8DBx48IpCYbC_lLMu-4JrygEPW92zZRFwXf_UArMx_iQURYIhyEvhWyHfJ/pub?output=csv";
        
        String htmlData = "<!DOCTYPE html><html lang='hi'><head><meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<title>Delivery Tracker</title><style>" +
            "* { box-sizing: border-box; font-family: system-ui, -apple-system, sans-serif; margin: 0; padding: 0; }" +
            "body { background-color: #121212; color: #e0e0e0; padding: 16px; }" +
            "h1 { text-align: center; font-size: 20px; margin-bottom: 16px; color: #4caf50; padding-top: 10px; }" +
            ".card { background: #1e1e1e; padding: 16px; border-radius: 12px; margin-bottom: 16px; border: 1px solid #333; }" +
            ".card-title { font-size: 15px; font-weight: bold; margin-bottom: 12px; color: #fff; }" +
            "input { width: 100%; padding: 14px; border-radius: 8px; border: 1px solid #444; background: #2a2a2a; color: #fff; font-size: 15px; outline: none; transition: 0.3s; }" +
            "input:focus { border-color: #4caf50; box-shadow: 0 0 8px rgba(76, 175, 80, 0.3); }" +
            ".btn-refresh { background: #4caf50; color: #fff; border: none; font-weight: bold; padding: 12px; width: 100%; border-radius: 8px; margin-bottom: 16px; cursor: pointer; font-size: 14px; }" +
            ".order-item { background: #252525; padding: 14px; border-radius: 10px; margin-bottom: 10px; border-left: 5px solid #4caf50; display: flex; justify-content: space-between; align-items: center; }" +
            ".order-info { font-size: 14px; line-height: 1.6; }" +
            ".track-id { font-size: 13px; color: #aaa; }" +
            ".order-id { font-size: 16px; font-weight: bold; color: #81c784; margin-top: 2px; }" +
            ".btn-copy { background: #333; color: #fff; border: 1px solid #555; padding: 6px 12px; border-radius: 6px; font-size: 12px; cursor: pointer; }" +
            ".btn-copy:active { background: #4caf50; border-color: #4caf50; }" +
            ".no-result { text-align: center; color: #888; padding: 20px 0; font-size: 14px; }" +
            ".status-info { text-align: center; font-size: 12px; color: #4caf50; margin-bottom: 8px; font-weight: bold; }" +
            "</style></head><body>" +
            "<h1>Delivery Tracker</h1>" +
            "<button class='btn-refresh' onclick='fetchOrders()'>🔄 Refresh Data</button>" +
            "<div class='card'>" +
            "<div class='card-title'>🔍 Tracking ID (Full or Last 4-5 Digits)</div>" +
            "<input type='text' id='search-input' placeholder='Type digits e.g. 43199...' oninput='renderOrders()'>" +
            "</div>" +
            "<div class='card'>" +
            "<div class='card-title'>📦 Order Details</div>" +
            "<div id='status-text' class='status-info'></div>" +
            "<div id='orders-list'>Loading data...</div>" +
            "</div>" +
            "<script>" +
            "let orders = [];" +
            "function fetchOrders() {" +
            "document.getElementById('orders-list').innerHTML = '<div class=\"no-result\">Fetching latest data...</div>';" +
            "document.getElementById('status-text').innerText = '';" +
            "fetch('" + csvUrl + "')" +
            ".then(res => res.text())" +
            ".then(csvText => { parseCSV(csvText); })" +
            ".catch(err => { document.getElementById('orders-list').innerHTML = '<div class=\"no-result\">Error loading data. Check Internet.</div>'; });" +
            "}" +
            "function parseCSV(text) {" +
            "let lines = text.split('\\n'); orders = [];" +
            "for(let i = 1; i < lines.length; i++) {" +
            "let line = lines[i].trim();" +
            "if(!line) continue;" +
            "let cols = line.split(',').map(c => c.replace(/\"/g, '').trim());" +
            "if(cols[0] || cols[1]) { orders.push({ trackingId: cols[0] || '', orderId: cols[1] || '' }); }" +
            "}" +
            "document.getElementById('status-text').innerText = '✅ Total Orders Loaded: ' + orders.length;" +
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
            "if(search === '') {" +
            "list.innerHTML = '<div class=\"no-result\">Type last 4-5 digits above to find Order ID</div>';" +
            "return;" +
            "}" +
            "let count = 0;" +
            "orders.forEach((item) => {" +
            "let track = item.trackingId.toLowerCase();" +
            "if (track.includes(search)) {" +
            "count++;" +
            "const div = document.createElement('div'); div.className = 'order-item';" +
            "div.innerHTML = `<div class='order-info'><div class='track-id'>Track: ${item.trackingId}</div><div class='order-id'>Order ID: ${item.orderId}</div></div><button class='btn-copy' onclick='copyToClipboard(\"${item.orderId}\")'>Copy</button>`;" +
            "list.appendChild(div); }" +
            "});" +
            "if(count === 0) list.innerHTML = '<div class=\"no-result\">❌ No matching Tracking ID found</div>';" +
            "}" +
            "fetchOrders();" +
            "</script></body></html>";

        webView.loadDataWithBaseURL("https://docs.google.com", htmlData, "text/html", "UTF-8", null);
        setContentView(webView);
    }
}
