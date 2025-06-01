package com.example.myapplication;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // C++ 라이브러리에서 정의된 네이티브 함수 연결 (필요 없는 경우 삭제 가능)
    public native String stringFromJNI();

    // 네이티브 라이브러리 로드
    static {
        System.loadLibrary("native-lib");
    }

    // 상세 정보가 펼쳐졌는지 여부를 저장하는 플래그
    private boolean isExpanded = false;
    public void pushAlarm(){
        // alarm_view.xml 레이아웃을 Java 코드에서 View 객체로 생성
        LayoutInflater inflater = LayoutInflater.from(this);
        View alarmView = inflater.inflate(R.layout.alarm_view, null);

        // 알림 뷰 내부의 UI 요소들을 가져오기
        TextView title = alarmView.findViewById(R.id.alarm_title);       // 알림 제목 텍스트
        TextView shortInfo = alarmView.findViewById(R.id.alarm_short);   // 요약 정보 텍스트
        TextView detail = alarmView.findViewById(R.id.alarm_detail);     // 상세 정보 텍스트
        ImageButton btnClose = alarmView.findViewById(R.id.btn_close);   // 닫기 (X) 버튼
        ImageButton btnToggle = alarmView.findViewById(R.id.btn_toggle); // 상세 토글 버튼

        // 알림 텍스트 내용 설정
        title.setText("위험지역에 접근했습니다!");
        shortInfo.setText("범죄유형: 폭행\n위험도: 높음\n범위: 150m 이내");
        detail.setText("이 지역은 최근 폭행 사건이 다수 발생한 구역입니다. 가능한 한 다른 경로로 이동하세요.");

        // [X] 닫기 버튼 클릭 시, 알림 뷰를 화면에서 숨김
        btnClose.setOnClickListener(v -> alarmView.setVisibility(View.GONE));

        // [▼/▲] 토글 버튼 클릭 시, 상세 설명을 펼치거나 숨김
        btnToggle.setOnClickListener(v -> {
            if (isExpanded) {
                detail.setVisibility(View.GONE); // 상세 숨김
                btnToggle.setImageResource(android.R.drawable.arrow_down_float); // ▼ 아이콘으로 변경
                isExpanded = false;
            } else {
                detail.setVisibility(View.VISIBLE); // 상세 보이기
                btnToggle.setImageResource(android.R.drawable.arrow_up_float); // ▲ 아이콘으로 변경
                isExpanded = true;
            }
        });

        // 알림 뷰에 둥근 테두리를 가진 붉은 배경 적용
        GradientDrawable background = new GradientDrawable();
        background.setColor(0xCCFF0000);      // 반투명 빨간색 (ARGB)
        background.setCornerRadius(30);       // 모서리를 둥글게 (30px)
        alarmView.setBackground(background);  // 배경 적용

        // 알림 뷰의 위치와 여백 설정
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,   // 너비: 전체
                FrameLayout.LayoutParams.WRAP_CONTENT    // 높이: 내용만큼
        );

        // 여백 설정 (왼쪽/오른쪽 32dp, 위쪽 60dp)
        float density = getResources().getDisplayMetrics().density;  // dp를 px로 변환하기 위한 스케일
        params.setMargins(
                (int)(32 * density),  // 왼쪽 여백
                (int)(60 * density),  // 위쪽 여백 (조금 아래로 내림)
                (int)(32 * density),  // 오른쪽 여백
                0                     // 아래쪽 여백 없음
        );

        // 화면 상단에 배치되도록 정렬 설정
        params.gravity = Gravity.TOP;

        // 알림 뷰를 메인 레이아웃에 추가
        FrameLayout rootLayout = findViewById(android.R.id.content); // 루트 레이아웃 가져오기
        rootLayout.addView(alarmView, params); // 알림 뷰를 레이아웃에 추가
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        WebView webview = findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setNeedInitialFocus(true);
        webview.loadUrl(""); // localhost제외 접속주소입력

        pushAlarm();













    }
}
