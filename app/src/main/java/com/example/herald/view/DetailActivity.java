package com.example.herald.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.herald.R;
import com.example.herald.model.API;
import com.example.herald.model.Indicator;

public class DetailActivity extends AppCompatActivity {
    private ImageView apiIcon;
    private TextView apiName;
    private API api;
    private View colorIndicator;
    private TextView stateValue;
    private TextView descriptionValue;
    private TextView updateDate;
    private TextView updateHour;
    private TextView healthLink;
    private ImageView leaveButton;


    public DetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        api = (API) getIntent().getSerializableExtra("api");

        apiIcon = findViewById(R.id.api_icon);
        Glide.with(this).load(this.api.getUrlIcon()).into(this.apiIcon);

        apiName = findViewById(R.id.api_name);
        apiName.setText(this.api.getName());

        colorIndicator = findViewById(R.id.color_indicator);
        colorIndicator.setBackground(this.api.getIndicator().asDrawable(this));

        stateValue = findViewById(R.id.state_value);
        stateValue.setText(this.api.getIndicator().toString().toLowerCase());

        descriptionValue = findViewById(R.id.description_value);
        descriptionValue.setText(this.api.getDescription());

        updateDate = findViewById(R.id.update_date);
        updateHour = findViewById(R.id.update_hour);
        updateDate.setText(this.api.getUpdateDate());
        updateHour.setText(this.api.getUpdateTime());

        healthLink = findViewById(R.id.health_link);
        healthLink.setText(this.api.getShortUrl());

        leaveButton = findViewById(R.id.leave_button);
        leaveButton.setOnClickListener(view -> finish());
    }
}
