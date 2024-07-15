package com.example.mytestinggoeson;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    EditText serialTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

//        try (FileWriter writer = new FileWriter(thePathToFile, true)) {
//            writer.append(row);
//            writer.close();
//            Toast.makeText(getApplicationContext(), "data written into data.txt", Toast.LENGTH_SHORT).show();
//        }

//        try (FileOutputStream writer = new FileOutputStream(thePathToFile, true)) {
//            writer.write(row.getBytes(StandardCharsets.UTF_8));
//            Toast.makeText(getApplicationContext(), "data written into data.txt", Toast.LENGTH_SHORT).show();
//        }
        try (FileOutputStream fout = openFileOutput(thePathToFile.getPath(), MODE_APPEND)){
            fout.write(row.getBytes());
        }

    }

}