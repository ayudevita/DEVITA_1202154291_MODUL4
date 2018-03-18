package com.example.android.devita_1202154291_studycase4;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class Listnama extends AppCompatActivity {

    //Deklrasi variable
    private ListView mListView;
    private ProgressBar mProgressBar;
    private Button mStartAsyncTask;
    //Menyimpan daftar nama mahasiswa yang akan ditampilkan
    private String [] namaMahasiswa= {"DIDOT",
            "DITO",
            "DODIT",
            "TODID",
            "DODOT",
            "DOTA",
            "ODAT",
            "DOTI",
            "DUCATI",
            "YAMAHA",
            "HONDA",
            "DEFAN"};
    //menambahkan item ke listview
    private AddItemToListView mAddItemToListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listnama);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mListView = (ListView) findViewById(R.id.listView);
        mStartAsyncTask = (Button) findViewById(R.id.button_startAsyncTask);

        //Membuat progressbar visible
        mListView.setVisibility(View.GONE);

        //Setup adapter
        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, new ArrayList<String>()));

        //Async task
        mStartAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adapter proses dengan async task
                mAddItemToListView = new AddItemToListView();
                mAddItemToListView.execute();
            }
        });
    }

    //Didalam class untuk proses async task
    public class AddItemToListView  extends AsyncTask<Void, String, Void> {

        private ArrayAdapter<String> mAdapter;
        private int counter=1;
        ProgressDialog mProgressDialog = new ProgressDialog(Listnama.this);

        @Override
        protected void onPreExecute() {
            mAdapter = (ArrayAdapter<String>) mListView.getAdapter(); //casting suggestion

            // Menjalankan progress dialog
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle("Loading Data");
            mProgressDialog.setProgress(0);
            //memperbarui kemajuan dialog dengan beberapa nilai
            //menampilkan progress dialog

            //Menghandle tombol cancel pada dialog
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel Process", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAddItemToListView.cancel(true); //Menampilkan (Visible) progress bar pada tombol cancel
                    mProgressBar.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }
            });
            mProgressDialog.show();
        }



        @Override
        protected Void doInBackground(Void... params) {
            //membuat perulangan untuk memunculkan nama mahasiswa
            for (String item : namaMahasiswa){
                publishProgress(item);
                try {
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(isCancelled()){
                    mAddItemToListView.cancel(true);
                }
            }
            return null;
        }



        @Override
        protected void onProgressUpdate(String... values) {
            mAdapter.add(values[0]); // memulai adapter array dari array 0

            Integer current_status = (int)((counter/(float)namaMahasiswa.length)*100);
            mProgressBar.setProgress(current_status);

            //Mengatur tampilan progress pada dialog progress
            mProgressDialog.setProgress(current_status);

            //Mengatur pesan berupa persentase progress pada dialog progress
            mProgressDialog.setMessage(String.valueOf(current_status+"%"));
            counter++; //mengeset hitungan di dalam progress dialog
        }

        @Override
        protected void onPostExecute(Void Void) {
            //Menyembunyikan progressbar
            mProgressBar.setVisibility(View.GONE);

            //setelah loading progress sudah full maka otomatis akan hilang progress dialognya
            mProgressDialog.dismiss();
            mListView.setVisibility(View.VISIBLE);
        }

    }
}
