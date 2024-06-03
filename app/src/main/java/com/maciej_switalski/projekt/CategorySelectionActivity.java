package com.maciej_switalski.projekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CategorySelectionActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
        radioGroup = findViewById(R.id.radioGroup);
        confirmButton = findViewById(R.id.confirmButton);
        checkCategory();


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    String selectedCategory = selectedRadioButton.getText().toString();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedCategory", selectedCategory);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Finish this activity
                } else {
                    Toast.makeText(CategorySelectionActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkCategory() {
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        switch (category) {
            case "Rodzina":
                radioGroup.check(R.id.categoryFamily);
                break;
            case "Przyjaciele":
                radioGroup.check(R.id.categoryFriends);
                break;
            case "Praca":
                radioGroup.check(R.id.categoryWork);
                break;
            case "Podróże":
                radioGroup.check(R.id.categoryTravel);
                break;
            case "Zdrowie":
                radioGroup.check(R.id.categoryHealth);
                break;
            case "Hobby":
                radioGroup.check(R.id.categoryHobby);
                break;
            default:
                radioGroup.check(R.id.categoryFamily); // If category does not match, do nothing
        }
    }

}
