package com.deliverytracker.app;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        
        String htmlData = "<!DOCTYPE html><html lang='hi'><head><meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<title>Delivery Tracker</title><style>" +
            "* { box-sizing: border-box; font-family: system-ui, -apple-system, sans-serif; margin: 0; padding: 0; }" +
            "body { background-color: #121212; color: #e0e0e0; padding: 16px; }" +
            "h1 { text-align: center; font-size: 20px; margin-bottom: 16px; color: #4caf50; }" +
            ".card { background: #1e1e1e; padding: 16px; border-radius: 12px; margin-bottom: 16px; border: 1px solid #333; }" +
            ".card-title { font-size: 16px; font-weight: bold; margin-bottom: 12px; color: #fff; }" +
            ".stats-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }" +
            ".stat-box { background: #2a2a2a; padding: 12px; border-radius: 8px; text-align: center; }" +
            ".stat-value { font-size: 18px; font-weight: bold; margin-top: 4px; }" +
            ".stat-pending { color: #ffb74d; } .stat-delivered { color: #81c784; } .stat-failed { color: #e57373; } .stat-total { color: #64b5f6; }" +
            "input, select, button { width: 100%; padding: 12px; margin-bottom: 10px; border-radius: 8px; border: 1px solid #444; background: #2a2a2a; color: #fff; font-size: 14px; outline: none; }" +
            "input:focus, select:focus { border-color: #4caf50; }" +
            ".btn-primary { background: #4caf50; color: #fff; border: none; font-weight: bold; cursor: pointer; }" +
            ".search-box { margin-bottom: 12px; }" +
            ".order-item { background: #252525; padding: 12px; border-radius: 8px; margin-bottom: 8px; display: flex; justify-content: space-between; align-items: center; border-left: 4px solid #777; }" +
            ".order-item.Pending { border-left-color: #ffb74d; } .order-item.Delivered { border-left-color: #81c784; } .order-item.Failed { border-left-color: #e57373; }" +
            ".order-info { font-size: 13px; line-height: 1.5; } .order-id { font-weight: bold; color: #fff; font-size: 14px; }" +
            ".order-status { display: inline-block; padding: 2px 6px; border-radius: 4px; font-size: 11px; font-weight: bold; margin-top: 4px; }" +
            ".bg-Pending { background: #fff3e0; color: #e65100; } .bg-Delivered { background: #e8f5e9; color: #1b5e20; } .bg-Failed { background: #ffebee; color: #b71c1c; }" +
            ".actions button { width: auto; padding: 6px 10px; margin-left: 4px; font-size: 12px; margin-bottom: 0; }" +
            ".btn-edit { background: #0288d1; border: none; } .btn-delete { background: #d32f2f; border: none; }" +
            "</style></head><body>" +
            "<h1>Delivery Tracker</h1>" +
            "<div class='card'><div class='card-title'>Dashboard & Performance</div>" +
            "<div class='stats-grid'>" +
            "<div class='stat-box'>Total Orders<div id='total-count' class='stat-value stat-total'>0</div></div>" +
            "<div class='stat-box'>Pending<div id='pending-count' class='stat-value stat-pending'>0</div></div>" +
            "<div class='stat-box'>Delivered<div id='delivered-count' class='stat-value stat-delivered'>0</div></div>" +
            "<div class='stat-box'>Failed<div id='failed-count' class='stat-value stat-failed'>0</div></div>" +
            "</div><div style='text-align: center; margin-top: 10px; font-size: 13px; color: #aaa;'>" +
            "Success Rate: <strong id='success-rate' style='color: #4caf50;'>0%</strong></div></div>" +
            "<div class='card'><div class='card-title' id='form-title'>Add New Order</div>" +
            "<input type='hidden' id='edit-id'>" +
            "<input type='text' id='order-id' placeholder='Order ID'>" +
            "<input type='text' id='tracking-id' placeholder='Tracking ID'>" +
            "<select id='order-status'><option value='Pending'>Pending</option><option value='Delivered'>Delivered</option><option value='Failed'>Failed</option></select>" +
            "<button class='btn-primary' id='save-btn' onclick='saveOrder()'>Save Order</button></div>" +
            "<div class='card'><div class='card-title'>Orders List</div>" +
            "<input type='text' class='search-box' id='search-input' placeholder='Search by Order or Tracking ID...' oninput='renderOrders()'>" +
            "<div id='orders-list'></div></div>" +
            "<script>" +
            "let orders = JSON.parse(localStorage.getItem('tracker_orders')) || [];" +
            "function saveOrder() {" +
            "const orderId = document.getElementById('order-id').value.trim();" +
            "const trackingId = document.getElementById('tracking-id').value.trim();" +
            "const status = document.getElementById('order-status').value;" +
            "const editId = document.getElementById('edit-id').value;" +
            "if (!orderId || !trackingId) { alert('Please fill Order ID and Tracking ID'); return; }" +
            "if (editId !== '') { orders[editId] = { orderId, trackingId, status }; document.getElementById('edit-id').value = ''; document.getElementById('form-title').innerText = 'Add New Order'; document.getElementById('save-btn').innerText = 'Save Order'; } else { orders.push({ orderId, trackingId, status }); }" +
            "localStorage.setItem('tracker_orders', JSON.stringify(orders)); clearForm(); renderOrders(); }" +
            "function clearForm() { document.getElementById('order-id').value = ''; document.getElementById('tracking-id').value = ''; document.getElementById('order-status').value = 'Pending'; document.getElementById('edit-id').value = ''; document.getElementById('form-title').innerText = 'Add New Order'; document.getElementById('save-btn').innerText = 'Save Order'; }" +
            "function editOrder(index) { const item = orders[index]; document.getElementById('order-id').value = item.orderId; document.getElementById('tracking-id').value = item.trackingId; document.getElementById('order-status').value = item.status; document.getElementById('edit-id').value = index; document.getElementById('form-title').innerText = 'Edit Order'; document.getElementById('save-btn').innerText = 'Update Order'; window.scrollTo({ top: 0, behavior: 'smooth' }); }" +
            "function deleteOrder(index) { if (confirm('Delete this order?')) { orders.splice(index, 1); localStorage.setItem('tracker_orders', JSON.stringify(orders)); renderOrders(); } }" +
            "function renderOrders() { const list = document.getElementById('orders-list'); const search = document.getElementById('search-input').value.toLowerCase(); list.innerHTML = ''; let pending = 0, delivered = 0, failed = 0;" +
            "orders.forEach((item, index) => { if (item.status === 'Pending') pending++; if (item.status === 'Delivered') delivered++; if (item.status === 'Failed') failed++;" +
            "if (item.orderId.toLowerCase().includes(search) || item.trackingId.toLowerCase().includes(search)) { const div = document.createElement('div'); div.className = `order-item ${item.status}`; div.innerHTML = `<div class='order-info'><div class='order-id'>Order: ${item.orderId}</div><div>Track: ${item.trackingId}</div><span class='order-status bg-${item.status}'>${item.status}</span></div><div class='actions'><button class='btn-edit' onclick='editOrder(${index})'>Edit</button><button class='btn-delete' onclick='deleteOrder(${index})'>Delete</button></div>`; list.appendChild(div); } });" +
            "document.getElementById('total-count').innerText = orders.length; document.getElementById('pending-count').innerText = pending; document.getElementById('delivered-count').innerText = delivered; document.getElementById('failed-count').innerText = failed;" +
            "const rate = orders.length > 0 ? Math.round((delivered / orders.length) * 100) : 0; document.getElementById('success-rate').innerText = `${rate}%`; }" +
            "renderOrders();" +
            "</script></body></html>";

        webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);
        setContentView(webView);
    }
}
