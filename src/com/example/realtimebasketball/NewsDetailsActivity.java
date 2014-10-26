package com.example.realtimebasketball;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsDetailsActivity extends Activity {

	private WebView webView;
	private String html;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 1) {
				webView.getSettings().setBlockNetworkImage(false);
				webView.getSettings().setJavaScriptEnabled(false);
				webView.setVisibility(View.VISIBLE);
				System.out.println("html--->" + html);
				webView.loadDataWithBaseURL(
						null,
						html.replace("data-url", "src")
								.replace("class=\"lazy-load\"", "")
								.replace("本日点击排行榜", "").replace("查看原图", ""),
						"text/html", "utf-8", null);
			}
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_details);
		String url = getIntent().getStringExtra("url");
		System.out.println("url--->" + url);
		webView = (WebView) findViewById(R.id.news_details_webview);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setBlockNetworkImage(true);
		webView.loadUrl("http://m.baidu.com/news?tn=bdbody&src=" + url
				+ "&pu=sz@1320_2001,usm@4,ta@iphone_1_4.3_3_533&bd_page_type=1");
	}

	final class InJavaScriptLocalObj {
		private boolean isLoaded = false;

		@JavascriptInterface
		public void showSource(String html) {
			if (!isLoaded) {
				Document document = Jsoup.parse(html);
				Elements elements = document
						.getElementsByClass("page-view-article");
				NewsDetailsActivity.this.html = document.head()
						+ elements.toString();
				Message message = new Message();
				message.arg1 = 1;
				handler.sendMessage(message);
				isLoaded = true;
			}
		}
	}

	/*
	 * 监听返回按钮
	 */
	public void back(View view) {
		finish();
	}

	final class MyWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d("WebView", "onPageStarted");
			super.onPageStarted(view, url, favicon);
		}

		public void onPageFinished(WebView view, String url) {
			Log.d("WebView", "onPageFinished ");
			view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
					+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			super.onPageFinished(view, url);
		}
	}

}
