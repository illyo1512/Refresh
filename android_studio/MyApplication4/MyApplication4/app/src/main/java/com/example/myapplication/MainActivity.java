package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView; // TextView 사용을 위해 필요
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // C++ 함수와 연결되는 native 메서드 선언
    public native String stringFromJNI();

    static {
        System.loadLibrary("native-lib"); // ✅ CMakeLists.txt에 맞춤
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 시스템 인셋 처리 (기본 템플릿 코드)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TextView에 C++ 코드에서 온 문자열 출력
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }
}
