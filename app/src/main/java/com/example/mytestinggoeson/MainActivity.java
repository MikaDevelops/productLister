package com.example.mytestinggoeson;

import static android.service.controls.ControlsProviderService.TAG;

import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<String[]> rows = new ArrayList<>();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Spinner model = findViewById(R.id.spinnerModel);
        Spinner assetType = findViewById(R.id.spinnerAssetType);
        Spinner room = findViewById(R.id.spinnerRoom);

        serialTxt = findViewById(R.id.serialTxt);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnScan = findViewById(R.id.btnScan);

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
                    String[] row = {model.getSelectedItem().toString(), assetType.toString(),serialTxt.getText().toString(), "", room.getSelectedItem().toString(), ""};
                    writeRowToFile(serialTxt.getText().toString());
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