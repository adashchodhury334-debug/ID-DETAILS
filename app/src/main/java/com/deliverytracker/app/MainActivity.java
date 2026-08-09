package com.deliverytracker.app;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WebView webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        
        // इंटरनेट से डेटा लोड करने की permissions ऑन कर रहे हैं
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        
        webView.setWebViewClient(new WebViewClient());
        
        String sheetPublishedId = "2PACX-1vTNHE2l_d6VIDLvWCB7nL8DBx48IpCYbC_lLMu-4JrygEPW92zZRFwXf_UArMx_iQURYIhyEvhWyHfJ";
        String csvUrl = "https://docs.google.com/spreadsheets/d/e/" + sheetPublishedId + "/pub?output=csv";
        
        String htmlData = "<!DOCTYPE html><html lang='hi'><head><meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<title>Delivery Tracker</title><style>" +
            "* { box-sizing: border-box; font-family: system-ui, -apple-system, sans-serif; margin: 0; padding: 0; }" +
            "body { background-color: #121212; color: #e0e0e0; padding: 16px; }" +
            "h1 { text-align: center; font-size: 20px; margin-bottom: 16px; color: #4caf50; padding-top: 10px; }" +
            ".card { background: #1e1e1e; padding: 16px; border-radius: 12px; margin-bottom: 16px; border: 1px solid #333; }" +
            ".card-title { font-size: 16px; font-weight: bold; margin-bottom: 12px; color: #fff; }" +
            "input { width: 100%; padding: 12px; margin-bottom: 10px; border-radius: 8px; border: 1px solid #444; background: #2a2a2a; color: #fff; font-size: 14px; outline: none; }" +
            "input:focus { border-color: #4caf50; }" +
            ".btn-refresh { background: #4caf50; color: #fff; border: none; font-weight: bold; padding: 12px; width: 100%; border-radius: 8px; margin-bottom: 12px; cursor: pointer; font-size: 14px; }" +
            ".order-item { background: #252525; padding: 12px; border-radius: 8px; margin-bottom: 8px; border-left: 4px solid #4caf50; }" +
            ".order-info { font-size: 14px; line-height: 1.6; }" +
            ".track-id { font-weight: bold; color: #ffb74d; font-size: 15px; }" +
            ".order-id { color: #fff; font-size: 14px; }" +
            "</style></head><body>" +
            "<h1>Delivery Tracker</h1>" +
            "<button class='btn-refresh' onclick='fetchOrders()'>🔄 Refresh Live Data</button>" +
            "<div class='card'><div class='card-title'>Search Orders</div>" +
            "<input type='text' id='search-input' placeholder='Search by Tracking ID or Order ID...' oninput='renderOrders()'>" +
            "</div>" +
            "<div class='card'><div class='card-title'>Orders List</div>" +
            "<div id='orders-list'>Loading live data...</div></div>" +
            "<script>" +
            "let orders = [];" +
            "function fetchOrders() {" +
            "document.getElementById('orders-list').innerHTML = 'Fetching latest data...';" +
            "fetch('" + csvUrl + "')" +
            ".then(res => res.text())" +
            ".then(csvText => { parseCSV(csvText); })" +
            ".catch(err => { document.getElementById('orders-list').innerHTML = 'Error loading data: ' + err; });" +
            "}" +
            "function parseCSV(text) {" +
            "let lines = text.split('\\n'); orders = [];" +
            "for(let i = 1; i < lines.length; i++) {" +
            "if(!lines[i].trim()) continue;" +
            "let cols = lines[i].split(',').map(c => c.replace(/\"/g, '').trim());" +
            "if(cols[0] || cols[1]) { orders.push({ trackingId: cols[0] || '', orderId: cols[1] || '' }); }" +
            "}" +
            "renderOrders();" +
            "}" +
            "function renderOrders() {" +
            "const list = document.getElementById('orders-list');" +
            "const search = document.getElementById('search-input').value.toLowerCase();" +
            "list.innerHTML = '';" +
            "let count = 0;" +
            "orders.forEach((item) => {" +
            "if (item.trackingId.toLowerCase().includes(search) || item.orderId.toLowerCase().includes(search)) {" +
            "count++;" +
            "const div = document.createElement('div'); div.className = 'order-item';" +
            "div.innerHTML = `<div class='order-info'><div class='track-id'>Tracking ID: ${item.trackingId}</div><div class='order-id'>Order ID: ${item.orderId}</div></div>`;" +
            "list.appendChild(div); }" +
            "});" +
            "if(count === 0) list.innerHTML = 'No matching orders found.';" +
            "}" +
            "fetchOrders();" +
            "</script></body></html>";

        // base URL https:// सेट करने से CORS/Security issue हट जाता है
        webView.loadDataWithBaseURL("https://docs.google.com", htmlData, "text/html", "UTF-8", null);
        setContentView(webView);
    }
}
