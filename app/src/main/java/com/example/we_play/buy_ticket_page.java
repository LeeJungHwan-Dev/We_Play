package com.example.we_play;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.we_play.kakaopayModule.KakaoPay;
import com.iamport.sdk.domain.core.Iamport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class buy_ticket_page extends AppCompatActivity {

    String title , pic_link , location , name;
    ImageView buy_img;
    TextView buy_title,buy_location,chose_day,count_peple,total;
    CalendarView set_day;
    ImageButton up, down, go_pay;


    int people_count = 0;
    int totalpay = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket_page);


        Iamport.INSTANCE.init(this);


        buy_img = findViewById(R.id.buy_img);
        buy_title = findViewById(R.id.buy_title);
        buy_location = findViewById(R.id.buy_location);
        chose_day = findViewById(R.id.chose_day);
        count_peple = findViewById(R.id.cout_peple);
        total = findViewById(R.id.total);
        set_day = findViewById(R.id.set_day);
        up = findViewById(R.id.peple_up);
        down = findViewById(R.id.peple_down);
        go_pay = findViewById(R.id.go_pay);


        setData();
        try {
            name = readname("name.txt");
        }catch (Exception e){}


        Glide.with(this).load(pic_link).into(buy_img);
        buy_title.setText(title);
        buy_location.setText(location);

        set_day.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                chose_day.setText("날짜 : "+String.valueOf(year)+"/"+String.valueOf(month)+"/"+String.valueOf(dayOfMonth));
            }
        });


        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                people_count++;
                totalpay += 10000;
                count_peple.setText("인원 : " + String.valueOf(people_count) +" 명");
                total.setText("총 금액 : " + String.valueOf(totalpay)+" 원");
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                people_count--;
                totalpay -= 10000;
                count_peple.setText("인원 : " + String.valueOf(people_count) +" 명");
                total.setText("총 금액 : " + String.valueOf(totalpay)+" 원");
                if(totalpay < 0 || people_count < 0){
                    people_count = 0;
                    totalpay = 0;
                    total.setText("총 금액 : 0 원");
                    count_peple.setText("인원 : 0 명");
                }
            }
        });

        go_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalpay <= 0){
                    Toast.makeText(getApplicationContext(),"표를 한장 이상 구매해주세요.",Toast.LENGTH_SHORT).show();
                }else {
                    KakaoPay kakaopay = new KakaoPay(title, String.valueOf(totalpay), "asd", getApplication());
                    kakaopay.pay();
                }
            }
        });





    }

    public void setData(){
        Intent intent = getIntent();
        title = intent.getStringExtra("제목");
        pic_link = intent.getStringExtra("사진");
        location = intent.getStringExtra("장소");
    }

    public String readname(String fileName){

        try {
            // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
            StringBuffer data = new StringBuffer();
            FileInputStream fs = openFileInput(fileName);//파일명
            BufferedReader buffer = new BufferedReader
                    (new InputStreamReader(fs));
            String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
            if(str != null) {
                while (str != null) {
                    data.append(str+"\n");
                    str = buffer.readLine();
                }
                buffer.close();
                return data.toString();
            }
        } catch (Exception e) {

        }
        return null;
    }

}