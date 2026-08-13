package com.deliverytracker.app;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    
    private WebView webView;
    private DatabaseHelper dbHelper;
    // Aapka Google Apps Script Link Yahan Direct Code Me Add Kar Diya Gaya Hai
    private static final String GOOGLE_SCRIPT_URL = "https://script.google.com/macros/s/AKfycbzwOzM4Bayr7oc3ilLsFHvqxTpCDtRZT_UiIbZi9lqBU-FDZtnXHjtHeKB3SvlLvewxBA/exec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dbHelper = new DatabaseHelper(this);
        webView = new WebView(this);
        
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new WebAppInterface(), "AndroidNative");
        
        String htmlData = "<!DOCTYPE html><html lang='hi'><head><meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<title>Delivery Tracker</title><style>" +
            "* { box-sizing: border-box; font-family: system-ui, -apple-system, sans-serif; margin: 0; padding: 0; }" +
            "body { background-color: #121212; color: #e0e0e0; padding: 16px; position: relative; min-height: 100vh; }" +
            "header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; padding-top: 10px; }" +
            "h1 { font-size: 20px; color: #4caf50; margin: 0; }" +
            ".btn-lock { background: #333; color: #4caf50; border: 1px solid #4caf50; padding: 6px 12px; border-radius: 6px; font-size: 12px; font-weight: bold; cursor: pointer; }" +
            ".card { background: #1e1e1e; padding: 16px; border-radius: 12px; margin-bottom: 16px; border: 1px solid #333; }" +
            ".card-title { font-size: 15px; font-weight: bold; margin-bottom: 12px; color: #fff; display: flex; justify-content: space-between; align-items: center; }" +
            "textarea { width: 100%; height: 90px; padding: 12px; border-radius: 8px; border: 1px solid #444; background: #2a2a2a; color: #fff; font-size: 13px; outline: none; margin-bottom: 10px; resize: none; }" +
            "input { width: 100%; padding: 14px; border-radius: 8px; border: 1px solid #444; background: #2a2a2a; color: #fff; font-size: 15px; outline: none; margin-bottom: 10px; }" +
            ".btn-add { background: #4caf50; color: #fff; border: none; font-weight: bold; padding: 12px; width: 100%; border-radius: 8px; cursor: pointer; font-size: 14px; margin-bottom: 8px; }" +
            ".btn-sync { background: #0288d1; color: #fff; border: none; font-weight: bold; padding: 12px; width: 100%; border-radius: 8px; cursor: pointer; font-size: 14px; margin-bottom: 8px; }" +
            ".btn-danger { background: #c62828; color: #fff; border: none; font-weight: bold; padding: 10px; width: 100%; border-radius: 8px; cursor: pointer; font-size: 13px; margin-top: 10px; }" +
            ".order-item { background: #252525; padding: 14px; border-radius: 10px; margin-bottom: 10px; border-left: 5px solid #4caf50; display: flex; justify-content: space-between; align-items: center; }" +
            ".order-info { font-size: 14px; line-height: 1.6; word-break: break-all; }" +
            ".track-id { font-size: 13px; color: #aaa; }" +
            ".order-id { font-size: 15px; font-weight: bold; color: #81c784; margin-top: 2px; }" +
            ".action-btns { display: flex; gap: 8px; flex-shrink: 0; margin-left: 10px; }" +
            ".btn-copy { background: #333; color: #fff; border: 1px solid #555; padding: 8px 12px; border-radius: 6px; font-size: 13px; cursor: pointer; }" +
            ".btn-delete { background: #c62828; color: #fff; border: none; padding: 8px 10px; border-radius: 6px; font-size: 13px; cursor: pointer; }" +
            ".no-result { text-align: center; color: #888; padding: 15px 0; font-size: 14px; }" +
            ".status-info { text-align: center; font-size: 13px; color: #4caf50; margin-bottom: 8px; font-weight: bold; }" +
            "#admin-panel, #pass-box { display: none; }" +
            ".welcome-popup { position: fixed; bottom: 30px; left: 50%; transform: translateX(-50%); background: #4caf50; color: #121212; padding: 12px 24px; border-radius: 30px; font-weight: bold; font-size: 14px; box-shadow: 0 4px 15px rgba(76,175,80,0.4); opacity: 0; transition: opacity 0.3s ease; pointer-events: none; z-index: 9999; }" +
            ".welcome-popup.show { opacity: 1; }" +
            "</style></head><body>" +

            "<div id='welcome-toast' class='welcome-popup'>👋 Welcome, आदर्श!</div>" +

            "<header>" +
            "<h1>Delivery Tracker</h1>" +
            "<button class='btn-lock' id='lock-btn' onclick='handleAdminClick()'>🔒 Admin Login</button>" +
            "</header>" +

            "<div class='card' id='pass-box'>" +
            "<div class='card-title'>🔐 Enter Admin PIN</div>" +
            "<input type='password' id='pin-input' placeholder='Enter PIN...' />" +
            "<button class='btn-add' onclick='verifyPin()'>Login</button>" +
            "</div>" +

            "<div class='card' id='admin-panel'>" +
            "<div class='card-title'>🔄 Google Sheet Auto Sync</div>" +
            "<button class='btn-sync' onclick='syncGoogleSheet()'>🔄 Sync From Google Sheet</button>" +

            "<div class='card-title' style='margin-top:15px;'>📋 Manual Import (Copy/Paste)</div>" +
            "<textarea id='bulk-input' placeholder='Google Sheet से मैन्युअली टेक्स्ट कॉपी करके पेस्ट करें...'></textarea>" +
            "<button class='btn-add' onclick='bulkImport()'>⚡ Import Manual Orders</button>" +

            "<button class='btn-danger' onclick='clearAllOrders()'>⚠️ Clear All Saved Data</button>" +
            "</div>" +

            "<div class='card'>" +
            "<div class='card-title'>🔍 Search Order</div>" +
            "<input type='text' id='search-input' placeholder='Type last 4-5 digits...' oninput='searchOrders()' style='margin-bottom:0;'>" +
            "</div>" +

            "<div class='card'>" +
            "<div class='card-title'>📦 Order Details</div>" +
            "<div id='status-text' class='status-info'></div>" +
            "<div id='orders-list'></div>" +
            "</div>" +

            "<script>" +
            "let isAdmin = false;" +
            "const ADMIN_PIN = '7602';" +

            "function showWelcomePopup() {" +
            "let toast = document.getElementById('welcome-toast');" +
            "toast.classList.add('show');" +
            "setTimeout(() => { toast.classList.remove('show'); }, 1500);" +
            "}" +

            "function updateStatus() {" +
            "let total = AndroidNative.getTotalCount();" +
            "document.getElementById('status-text').innerText = '✅ Total Active Orders: ' + total;" +
            "}" +

            "function handleAdminClick() {" +
            "if(isAdmin) {" +
            "isAdmin = false;" +
            "document.getElementById('admin-panel').style.display = 'none';" +
            "document.getElementById('lock-btn').innerText = '🔒 Admin Login';" +
            "searchOrders();" +
            "} else {" +
            "let box = document.getElementById('pass-box');" +
            "box.style.display = box.style.display === 'block' ? 'none' : 'block';" +
            "}" +
            "}" +

            "function verifyPin() {" +
            "let inputPin = document.getElementById('pin-input').value.trim();" +
            "if(inputPin === ADMIN_PIN) {" +
            "isAdmin = true;" +
            "document.getElementById('pin-input').value = '';" +
            "document.getElementById('pass-box').style.display = 'none';" +
            "document.getElementById('admin-panel').style.display = 'block';" +
            "document.getElementById('lock-btn').innerText = '🔓 Logout Admin';" +
            "searchOrders();" +
            "alert('Admin Mode Activated!');" +
            "} else { alert('Wrong PIN!'); }" +
            "}" +

            "function syncGoogleSheet() {" +
            "alert('गूगल शीट से डाटा सिंक हो रहा है...');" +
            "setTimeout(() => {" +
            "let added = AndroidNative.syncFromSheet();" +
            "if(added >= 0) {" +
            "alert('सफलतापूर्वक ' + added + ' नए ऑर्डर्स सिंक हो गए!');" +
            "updateStatus();" +
            "searchOrders();" +
            "} else {" +
            "alert('❌ Error! इंटरनेट कनेक्शन चेक करें।');" +
            "}" +
            "}, 100);" +
            "}" +

            "function bulkImport() {" +
            "let rawText = document.getElementById('bulk-input').value.trim();" +
            "if(!rawText) { alert('पेस्ट बॉक्स खाली है!'); return; }" +
            "let lines = rawText.split(/\\r?\\n/);" +
            "let items = [];" +
            "for(let i = 0; i < lines.length; i++) {" +
            "let line = lines[i].trim();" +
            "if(!line) continue;" +
            "let parts = line.split(/[\\t,]/).map(p => p.trim());" +
            "if(parts.length >= 2 && parts[0] && parts[1]) {" +
            "if(!parts[0].toUpperCase().includes('TRACKING')) {" +
            "items.push({ t: parts[0], o: parts[1] });" +
            "}" +
            "}" +
            "}" +
            "let added = AndroidNative.insertBulk(JSON.stringify(items));" +
            "document.getElementById('bulk-input').value = '';" +
            "alert('सफलतापूर्वक ' + added + ' ऑर्डर्स मैन्युअली सेव हो गए!');" +
            "updateStatus();" +
            "searchOrders();" +
            "}" +

            "function clearAllOrders() {" +
            "AndroidNative.deleteAll();" +
            "updateStatus();" +
            "searchOrders();" +
            "alert('सारा डेटा डिलीट हो गया है!');" +
            "}" +

            "function deleteSingle(id) {" +
            "AndroidNative.deleteOrder(id);" +
            "updateStatus();" +
            "searchOrders();" +
            "}" +

            "function copyToClipboard(text) {" +
            "navigator.clipboard.writeText(text);" +
            "alert('Order ID Copied: ' + text);" +
            "}" +

            "function searchOrders() {" +
            "const list = document.getElementById('orders-list');" +
            "const search = document.getElementById('search-input').value.trim();" +
            "list.innerHTML = '';" +
            "if(search === '') {" +
            "list.innerHTML = '<div class=\"no-result\">सर्च करने के लिए लास्ट 4-5 डिजिट डालें</div>';" +
            "return;" +
            "}" +
            "let results = JSON.parse(AndroidNative.search(search));" +
            "if(results.length === 0) {" +
            "list.innerHTML = '<div class=\"no-result\">❌ No matching Tracking ID found</div>';" +
            "return;" +
            "}" +
            "results.forEach(item => {" +
            "const div = document.createElement('div'); div.className = 'order-item';" +
            "let delBtn = isAdmin ? `<button class='btn-delete' onclick='deleteSingle(${item.id})'>🗑️</button>` : '';" +
            "div.innerHTML = `<div class='order-info'><div class='track-id'>Track: ${item.t}</div><div class='order-id'>Order ID: ${item.o}</div></div><div class='action-btns'><button class='btn-copy' onclick='copyToClipboard(\"${item.o}\")'>Copy</button>${delBtn}</div>`;" +
            "list.appendChild(div);" +
            "});" +
            "}" +

            "updateStatus();" +
            "showWelcomePopup();" +
            "</script></body></html>";

        webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);
        setContentView(webView);
    }

    public class WebAppInterface {
        
        @JavascriptInterface
        public int syncFromSheet() {
            int count = 0;
            try {
                URL url = new URL(GOOGLE_SCRIPT_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setInstanceFollowRedirects(true);
                conn.setConnectTimeout(10000);
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction();
                
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String t = parts[0].replace("\"", "").trim();
                        String o = parts[1].replace("\"", "").trim();
                        if (!t.isEmpty() && !o.isEmpty() && !t.toUpperCase().contains("TRACKING")) {
                            ContentValues cv = new ContentValues();
                            cv.put("tracking_id", t);
                            cv.put("order_id", o);
                            db.insert("orders", null, cv);
                            count++;
                        }
                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                reader.close();
                return count;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        @JavascriptInterface
        public int insertBulk(String jsonStr) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            int count = 0;
            try {
                JSONArray arr = new JSONArray(jsonStr);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("tracking_id", obj.getString("t"));
                    cv.put("order_id", obj.getString("o"));
                    db.insert("orders", null, cv);
                    count++;
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
            return count;
        }

        @JavascriptInterface
        public String search(String query) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            JSONArray arr = new JSONArray();
            Cursor cursor = db.rawQuery("SELECT id, tracking_id, order_id FROM orders WHERE tracking_id LIKE ? OR order_id LIKE ? LIMIT 20", 
                    new String[]{"%" + query + "%", "%" + query + "%"});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        JSONObject obj = new JSONObject();
                        obj.put("id", cursor.getInt(0));
                        obj.put("t", cursor.getString(1));
                        obj.put("o", cursor.getString(2));
                        arr.put(obj);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
            return arr.toString();
        }

        @JavascriptInterface
        public void deleteOrder(int id) {
            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("orders", "id = ?", new String[]{String.valueOf(id)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void deleteAll() {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("orders", null, null);
        }

        @JavascriptInterface
        public int getTotalCount() {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM orders", null);
            int count = 0;
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
            return count;
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "DeliveryTracker.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Activity context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE orders (id INTEGER PRIMARY KEY AUTOINCREMENT, tracking_id TEXT, order_id TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS orders");
            onCreate(db);
        }
    }
}
