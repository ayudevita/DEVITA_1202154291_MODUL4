package com.example.android.devita_1202154291_studycase4;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class Carigambar extends AppCompatActivity {

    //Deklarasi variabel
    private ImageView downloadedImage;
    private ProgressDialog mProgressDialog;
    private EditText linkUrl;
    private Button imageDownloaderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carigambar);

        imageDownloaderButton = (Button) findViewById(R.id.button_startAsyncTask);
        downloadedImage = (ImageView) findViewById(R.id.ImageView);
        linkUrl = (EditText)findViewById(R.id.urlText);

        //Saat button di klik
        imageDownloaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String downloadUrl = linkUrl.getText().toString();
                if(downloadUrl.isEmpty()){
                    //Menampilkan toast ketika button diklik namun edit text url kosong
                    Toast.makeText(Carigambar.this,"Masukkan URL gambar terlebih dahulu",Toast.LENGTH_SHORT).show();
                }else {
                    // Execute DownloadImage AsyncTask
                    new ImageDownloader().execute(downloadUrl);
                }
            }
        });
    }
    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        //Dipanggil di thread UI sebelum tugas dijalankan
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            mProgressDialog = new ProgressDialog(Carigambar.this);
            // Set judul progress dialog
            mProgressDialog.setTitle("Search Image");
            // Set pesan pada progress dialog
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progress dialog
            mProgressDialog.show();
        }

        //Dipanggil di thread latar belakang segera setelah onPreExecute () selesai mengeksekusi.
        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Mendownload image melalui URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        //Dipanggil di thread UI setelah perhitungan latar belakang selesai.
        @Override
        protected void onPostExecute(Bitmap result) {
            // Set bitmap ke dalam ImageView
            downloadedImage.setImageBitmap(result);
            // Tutup progress dialog
            mProgressDialog.dismiss();
        }

    }
}
