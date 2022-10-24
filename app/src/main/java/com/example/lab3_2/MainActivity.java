package com.example.lab3_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    public Context mainActivity = this;
    EditText inputLiters;
    TextView viewStatistics;
    Button buttonNext, buttonFinish;
    Spinner inputOilTypeSelect;
    ArrayList<Order> orders = new ArrayList<Order>();

    String[] oilTypes;
    String[] oilPrices;

    boolean firstPress = true;//первое ли нажатие на кнопку

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputOilTypeSelect = findViewById(R.id.planets_spinner);
        inputLiters = findViewById(R.id.liters);
        buttonNext = findViewById(R.id.button);
        buttonFinish = findViewById(R.id.button2);
        viewStatistics = findViewById(R.id.textView5);

        oilTypes  = getResources().getStringArray(R.array.oilTypes);
        oilPrices = getResources().getStringArray(R.array.oilTypesPrice);
        if(oilTypes.length==oilPrices.length){
            ArrayList<String> dropDownList = new ArrayList<>();
            Order.oilPrices = new Hashtable<>();
            for(int i = 0; i< oilTypes.length; i++){
                Order.oilPrices.put(oilTypes[i], Double.parseDouble(oilPrices[i]));
                dropDownList.add(oilTypes[i]+" : "+oilPrices[i]+" руб");
            }


            // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dropDownList);
            //ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.oilTypes, android.R.layout.simple_spinner_item);
            // Определяем разметку для использования при выборе элемента
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Применяем адаптер к элементу spinner
            inputOilTypeSelect.setAdapter(adapter);
        }else{
            Toast toast = Toast.makeText(this, "Проверьте ресурсы приложения! Количество цен и наименований видов топлива не совпадают! ",Toast.LENGTH_LONG);
            toast.show();
            this.finishAffinity();
        }



        if (getIntent().getExtras() != null) {
            String statistics = getIntent().getExtras().getString("statistics");
            viewStatistics.setText(statistics);
        }


    }



    public void NextOrder(View view) {
        if (firstPress) {

            inputLiters.setVisibility(View.VISIBLE);
            viewStatistics.setVisibility(View.VISIBLE);
            buttonNext.setText("Новый заказ");
            buttonNext.setVisibility(View.VISIBLE);
            buttonFinish.setVisibility(View.VISIBLE);
            inputOilTypeSelect.setVisibility(View.VISIBLE);

            viewStatistics.setText("");


            firstPress = false;
        }else{
            String oiltype = inputOilTypeSelect.getSelectedItem().toString().split(" : ")[0];

            if(!inputLiters.getText().toString().isEmpty()) {
                double liters = Double.parseDouble(inputLiters.getText().toString());
                Order newOrder = new Order(oiltype, liters);
                orders.add(newOrder);
                inputLiters.setText("");
                viewStatistics.setText("");
            } else {
                Toast toast = Toast.makeText(this, "Не все поля заполнены!",Toast.LENGTH_LONG);
                toast.show();
            }
        }


    }

    public void FinishRegistration(View view) {


        String statistics = new String();
        double[] totalPrices = new double[oilTypes.length];
        float[] totalLiters = new float[oilTypes.length];
        double totalPrice = 0;
        for (Order order : orders) {
            int indexOfType = Arrays.asList(oilTypes).indexOf(order.oilType);
            totalPrices[indexOfType]+=order.getPrice();
            totalLiters[indexOfType]+=order.liters;
            totalPrice+=order.getPrice();
        }
        statistics+= String.format("%-15s %-15s %-15s %n", "Вид","Литры","Общая стоимость");
        for(int i=0; i< oilTypes.length;i++){
            statistics+= String.format("%-15s %-15s %-15s %n",oilTypes[i],totalLiters[i],totalPrices[i] +" руб");
        }
        statistics+= "Общая выручка : "+ totalPrice +"руб \r\n";

        Intent intent = new Intent(this.mainActivity, MainActivity.class);
        intent.putExtra("statistics", statistics);
        startActivity(intent);
        Toast toast = Toast.makeText(this, "Смена окончена", Toast.LENGTH_SHORT);
        toast.show();
    }

}
