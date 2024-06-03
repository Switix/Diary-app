package com.maciej_switalski.projekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    List<Entry> allEntryList;
    List<Entry> entryList = new ArrayList<>();
    EntryDbHelper dbHelper;
    Spinner sortSpinner;

    EntryAdapter adapter;
    FloatingActionButton newNoteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newNoteButton = findViewById(R.id.newNote_button);
        sortSpinner = findViewById(R.id.sort_spinner);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        dbHelper = new EntryDbHelper(this);
        // Insert entries
        // Execute SQL statement to delete all entries
        //dbHelper.deleteAllEntries();
        if (dbHelper.getAllEntries().isEmpty()) {
            dbHelper.addEntry(new Entry("Rodzinny Obiad", "Dziś mieliśmy cudowny rodzinny obiad u babci. Wszyscy się śmialiśmy i opowiadaliśmy historie z dzieciństwa.", "2024-06-01", "Rodzina"));
            dbHelper.addEntry(new Entry("Wypad z Przyjaciółmi", "Spędziliśmy cały dzień na plaży, grając w siatkówkę i pływając. Wieczorem zrobiliśmy ognisko.", "2024-05-15", "Przyjaciele"));
            dbHelper.addEntry(new Entry("Nowy Projekt w Pracy", "Dostałem nowy projekt w pracy, który jest naprawdę ekscytujący. Będę odpowiedzialny za prowadzenie zespołu.", "2024-05-20", "Praca"));
            dbHelper.addEntry(new Entry("Wyprawa do Japonii", "Rozpoczęliśmy naszą podróż do Japonii. Zwiedziliśmy Tokio i spróbowaliśmy sushi w lokalnej restauracji.", "2024-04-25", "Podróże"));
            dbHelper.addEntry(new Entry("Bieganie w Parku", "Dziś rano poszedłem biegać do parku. Czuję się świetnie po tym treningu. Zdrowie to podstawa.", "2024-05-05", "Zdrowie"));
            dbHelper.addEntry(new Entry("Nowy Obraz", "Dziś ukończyłem malowanie nowego obrazu. Jestem naprawdę dumny z efektu końcowego.", "2024-05-30", "Hobby"));

            dbHelper.addEntry(new Entry("Spotkanie Rodzinne", "Zebraliśmy się całą rodziną na weekendowy piknik. Dzieci biegały i bawiły się, a dorośli rozmawiali i śmiali się.", "2024-05-25", "Rodzina"));
            dbHelper.addEntry(new Entry("Wieczór z Przyjaciółmi", "Spędziliśmy wieczór grając w planszówki i jedząc domową pizzę. To był świetny czas pełen śmiechu.", "2024-06-02", "Przyjaciele"));
            dbHelper.addEntry(new Entry("Awans w Pracy", "Dostałem awans na stanowisko kierownika projektu. Jestem bardzo podekscytowany nowymi wyzwaniami.", "2024-05-28", "Praca"));
            dbHelper.addEntry(new Entry("Weekend w Paryżu", "Spędziliśmy romantyczny weekend w Paryżu, zwiedzając wieżę Eiffla i spacerując po Champs-Élysées.", "2024-05-18", "Podróże"));
            dbHelper.addEntry(new Entry("Zdrowe Śniadanie", "Dziś zaczęliśmy dzień od zdrowego śniadania z owsianką i świeżymi owocami. Czuję się pełen energii.", "2024-05-22", "Zdrowie"));
            dbHelper.addEntry(new Entry("Warsztaty Fotograficzne", "Wziąłem udział w warsztatach fotograficznych i nauczyłem się wielu nowych technik. Zrobiłem kilka świetnych zdjęć.", "2024-06-01", "Hobby"));

        }


        entryList = dbHelper.getAllEntries();
        allEntryList = new ArrayList<>(entryList);
        adapter = new EntryAdapter(entryList);
        recyclerView.setAdapter(adapter);
        int spanCount = 2;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(categoryAdapter);
        sortSpinner.setSelection(categoryAdapter.getPosition("Wszystkie"));
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected category string
                String category = parentView.getItemAtPosition(position).toString();

                // Filter the entryList based on the selected category
                List<Entry> filteredList = new ArrayList<>();
                if ("Wszystkie".equals(category)) {
                    // Show all entries
                    filteredList.addAll(dbHelper.getAllEntries());
                } else {
                    // Filter entries by category
                    for (Entry entry : allEntryList) {
                        if (entry.getCategory().equals(category)) {
                            filteredList.add(entry);
                        }
                    }
                }
                entryList.clear();
                entryList.addAll(filteredList);
                adapter.updateList();
                // adapter.updateEntries(filteredList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        newNoteButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NoteActivity.class);

            intent.putExtra("entry_title", "");
            intent.putExtra("entry_content", "");
            intent.putExtra("category", "");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());
            intent.putExtra("entry_date", currentDate);

            v.getContext().startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        allEntryList = dbHelper.getAllEntries();
        String category = sortSpinner.getSelectedItem().toString();

        // Filter the entryList based on the selected category
        List<Entry> filteredList = new ArrayList<>();
        if ("Wszystkie".equals(category)) {
            // Show all entries
            filteredList.addAll(dbHelper.getAllEntries());
        } else {
            // Filter entries by category
            for (Entry entry : allEntryList) {
                if (entry.getCategory().equals(category)) {
                    filteredList.add(entry);
                }
            }
        }
        entryList.clear();
        entryList.addAll(filteredList);
        adapter.updateList();
    }

}