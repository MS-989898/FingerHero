package com.example.FingerHero;

import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URLDecoder;

public class ArticleActivity extends AppCompatActivity {

    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_view);

        String url="https://knu.ac.kr/wbbs/wbbs/bbs/btin/stdViewBtin.action?menu_idx=42&btin.doc_no=1402856&btin.appl_no=000000&btin.note_div=top";

        // 웹뷰 시작
        mWebView = (WebView) findViewById(R.id.webView);

        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebView.setVerticalScrollBarEnabled(false);    //스크롤 차단
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (true);
            }
        });
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부

        mWebView.loadUrl(url); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        mWebView.setDownloadListener(new DownloadListener() {   //웹뷰어 다운로드 리스너
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                    contentDisposition = URLDecoder.decode(contentDisposition,"UTF-8"); //디코딩
                    String FileName = contentDisposition.replace("attachment; filename=", ""); //attachment; filename*=UTF-8''뒤에 파일명이있는데 파일명만 추출하기위해 앞에 attachment; filename*=UTF-8''제거

                    String fileName = FileName; //위에서 디코딩하고 앞에 내용을 자른 최종 파일명
                    request.setMimeType(mimetype);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading File");
                    request.setAllowedOverMetered(true);
                    request.setAllowedOverRoaming(true);
                    request.setTitle(fileName);
                    request.setRequiresCharging(false);

                    request.allowScanningByMediaScanner();
                    request.setAllowedOverMetered(true);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(),"파일이 다운로드됩니다.", Toast.LENGTH_LONG).show();
                }
                catch (Exception e) {
                    if (ContextCompat.checkSelfPermission(ArticleActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ArticleActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(getBaseContext(), "다운로드를 위해\n권한이 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(ArticleActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1004);
                        }
                        else {
                            Toast.makeText(getBaseContext(), "다운로드를 위해\n권한이 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(ArticleActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1004);
                        }
                    }
                }
            }
        });
    }
}