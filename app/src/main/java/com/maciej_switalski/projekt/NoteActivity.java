package com.maciej_switalski.projekt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private final String DATE_FORMAT = "yyyy-MM-dd";
    private long id;
    private EditText contentEditText;
    private EditText titleEditText;
    private EditText dateEditText;
    private Button saveButton;
    private Calendar calendar;
    private EntryDbHelper dbHelper;

    private FloatingActionButton categoriesButton;
    private String category;
    private ActivityResultLauncher<Intent> categoryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("selectedCategory")) {
                        this.category = data.getStringExtra("selectedCategory");
                    }
                }
            });

    @Override
    protected void onPause() {
        super.onPause();
        saveNote();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initializeFields();

        // Set click listener to show DatePickerDialog
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Button click listener
        saveButton.setOnClickListener(v -> {
            saveNote();
            Toast.makeText(this, "Zapisano zmiany", Toast.LENGTH_SHORT).show();
        });
        categoriesButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CategorySelectionActivity.class);
            intent.putExtra("category", category);
            categoryLauncher.launch(intent);
        });
    }

    private void saveNote() {
        String contentText = contentEditText.getText().toString();
        String titleText = titleEditText.getText().toString();
        String dateText = dateEditText.getText().toString();
        if( contentText.isEmpty()){
            return;
        }

        Entry entry = new Entry(
                id,
                titleText,
                contentText,
                dateText,
                category
        );
        if(entry.getId() != 0){
            dbHelper.updateEntry(entry);
            return;
        }
        id = dbHelper.addEntry(entry);
    }

    private void initializeFields() {
        contentEditText = findViewById(R.id.contentEditText);
        titleEditText = findViewById(R.id.titleEditText);
        dateEditText = findViewById(R.id.dateEditText);
        saveButton = findViewById(R.id.savebutton);
        categoriesButton = findViewById(R.id.categoriesButton);
        calendar = Calendar.getInstance();
        dbHelper = new EntryDbHelper(this);

        Intent intent = getIntent();

        contentEditText.setText(intent.getStringExtra("entry_content"));
        titleEditText.setText(intent.getStringExtra("entry_title"));
        id = intent.getLongExtra("id",0);
        category = intent.getStringExtra("category");
        String entryDateStr = intent.getStringExtra("entry_date");
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        try {
            Date entryDate = dateFormat.parse(entryDateStr);
            calendar.setTime(entryDate);
            updateDateEditText();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showDatePickerDialog() {
        // Create DatePickerDialog with current date and set listener
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Update calendar instance with selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Update EditText with selected date
                    updateDateEditText();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void updateDateEditText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        dateEditText.setText(dateFormat.format(calendar.getTime()));
    }
}