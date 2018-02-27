package com.example.gourav.pdf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public PDFView pdfView;
    static public ProgressDialog mProgressDialog;
    public DatabaseReference mchild,mref;
    public List<String> key=new ArrayList<>();
    public List<String> url=new ArrayList<>();
    public Spinner spinner;
    public String abc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button view=(Button)findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view();
            }
        });

        pdfView=(PDFView)findViewById(R.id.pdfView1);
        spinner=(Spinner)findViewById(R.id.spinner);
        mchild=FirebaseDatabase.getInstance().getReference();
        mref=mchild.child("root").child("url");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String s=snapshot.getKey().toString();
                    key.add(s);
                    s=snapshot.getValue().toString();
                    url.add(s);
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, key);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(areasAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void download(final View v)
    {
        final int a=spinner.getSelectedItemPosition();
        abc=spinner.getSelectedItem().toString();
        String ab=url.get(a);
        new DownloadFile().execute(ab, abc+".pdf");

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Runnable progressRunnable = new Runnable() {
//
//
//                    @Override
//                    public void run() {
//                        mProgressDialog.cancel();
//
//                            }
//                };
//
//                Handler pdCanceller = new Handler();
//                pdCanceller.postDelayed(progressRunnable, 7000);
//
//            }
//        }, 10000);

    }

    public void view()
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + abc+".pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        Toast.makeText(this, path.toString(), Toast.LENGTH_SHORT).show();
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfView.fromUri(path)
                .pages(0, 1) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen

                // allows to draw something on
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load();
//
//        try{
//            startActivity(pdfIntent);
//        }catch(ActivityNotFoundException e){
//            Toast.makeText(Main6Activity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
//        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "testthreepdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null ;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Create progress dialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set your progress dialog Title
            mProgressDialog.setTitle("Pdf downloading...");
            // Set your progress dialog Message
            mProgressDialog.setMessage("Downloading, Please Wait!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Show progress dialog
            mProgressDialog.show();
        }



    }
}
