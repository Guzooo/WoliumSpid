package pl.Guzooo.WoliumSpid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import pl.Guzooo.Base.Elements.BusinessCard;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View v = findViewById(R.id.logo_g);
        BusinessCard bc = findViewById(R.id.business_card_g);
        bc.setOpenerView(v);
    }
}