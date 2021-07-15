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

        initialization();
        setBusinessCard();
        setProfilesRecycler();
    }

    private void initialization(){

    }

    private void setBusinessCard(){
        View logo = findViewById(R.id.logo_g);
        BusinessCard businessCard = findViewById(R.id.business_card_g);
        businessCard.setOpenerView(logo);
    }

    private void setProfilesRecycler(){

    }
}