package com.example.mytestinggoeson;

import static android.service.controls.ControlsProviderService.TAG;

import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText serialTxt;
    String valittuMalli = "";
    String valittuTyyppi = "";
    String valittuHuone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String[] mallit = EquipmentData.mallit;

        final String[] tyypit = EquipmentData.tyypit;

        final String[] huoneet = EquipmentData.huoneet;

        ArrayList<String[]> rows = new ArrayList<>();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Spinner model = findViewById(R.id.spinnerModel);
        Spinner assetType = findViewById(R.id.spinnerAssetType);
        Spinner room = findViewById(R.id.spinnerRoom);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mallit);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tyypit);
        ArrayAdapter arrayAdapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, huoneet);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        model.setAdapter(arrayAdapter);
        assetType.setAdapter(arrayAdapter2);
        room.setAdapter(arrayAdapter3);

        serialTxt = findViewById(R.id.serialTxt);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnScan = findViewById(R.id.btnScan);

        model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                valittuMalli = arg0.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(), valittuMalli, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        assetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                valittuTyyppi = arg0.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(), valittuTyyppi, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                valittuHuone = arg0.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(), valittuHuone, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanOptions scanOptions = new ScanOptions();
                barcodeLauncher.launch(scanOptions);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Tämä add row buttonille toiminnoksi.
                    String row = valittuMalli +"\t"+ valittuTyyppi +"\t" + serialTxt.getText().toString() +"\t"+ "\t"+ valittuHuone +"\t"+ "\n";
                    writeRowToFile(row);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(), result -> {
                if(result.getContents() != null){
                    serialTxt.setText(result.getContents());
                    //Toast.makeText(getApplicationContext(), "scanned: "+result.getContents(), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "didn't work: ", Toast.LENGTH_LONG).show();
                }
            }
    );

    protected void writeRowToFile(String row) throws IOException {
        File[] path = getApplicationContext().getExternalMediaDirs();
        File thePathToFile = new File(path[0], "data.txt");

        try (FileWriter writer = new FileWriter(thePathToFile, true)) {
            writer.append(row);
            writer.close();
            Toast.makeText(getApplicationContext(), "data written into data.txt", Toast.LENGTH_SHORT).show();
        }

//        try (FileOutputStream writer = new FileOutputStream(thePathToFile, true)) {
//            writer.write(row.getBytes(StandardCharsets.UTF_8));
//            Toast.makeText(getApplicationContext(), "data written into data.txt", Toast.LENGTH_SHORT).show();
//        }
//        try (FileOutputStream fout = openFileOutput(thePathToFile.getPath(), MODE_APPEND)){
//            fout.write(row.getBytes());
//        }

    }

}