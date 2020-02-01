package com.mvrt.pit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;


public class LocalDataActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ItemSelectListener {

    RecyclerView fileListRecycler;
    LocalDataAdapter localDataAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebase();

        setContentView(R.layout.activity_localdata);

        setTitle("Local Data");
        Toolbar t = (Toolbar)findViewById(R.id.pitscout_toolbar);
        setSupportActionBar(t);

        fileListRecycler = (RecyclerView)findViewById(R.id.localdata_listrecycler);
        //initRecycler();
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.localdata_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initFirebase(){

    }

    private void initRecycler(){
        fileListRecycler.setLayoutManager(new LinearLayoutManager(this));
        String[] filenames = getFilesDir().list(new JSONFilter());
        localDataAdapter = new LocalDataAdapter(filenames);
        localDataAdapter.setSelectListener(this);
        fileListRecycler.setAdapter(localDataAdapter);
    }

    public void syncData(){
        String[] filenames = localDataAdapter.getFilenames();
        for(String file:filenames){
            readFile(file);
        }
        this.onRefresh();
    }

    private void readFile(String filename){
        try {
            FileInputStream fis = openFileInput(filename);

            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();

            JSONObject scoutData = new JSONObject(new String(buffer));

            uploadData(scoutData, filename);

        }catch(FileNotFoundException e){
            Toast.makeText(this, "File " + filename + " not found", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", "File " + filename + " not found");
        }catch(IOException e){
            Toast.makeText(this, "File Read IOException", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", "File Read IOException");
        }catch(JSONException e){
            Toast.makeText(this, "File Read JSONException", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", "File Read JSONException");
        }

    }

    private void uploadData(JSONObject scoutData, String filename) {
        /*
        try{
            //firebase.push().setValue(JSONUtils.jsonToMap(scoutData));
            deleteFile(filename);
            Log.d("MVRT", "Deleted file " + filename);
        }catch(JSONException e){
            Toast.makeText(this, "Upload JSONException", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", "Upload JSONException");
        }
        */
    }

    @Override
    public void onRefresh() {
        String[] filenames = getFilesDir().list(new JSONFilter());
        localDataAdapter.setFilenames(filenames);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void itemSelected(String filename) {
        Toast.makeText(this, filename + " selected", Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_localdata, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_syncdata:
                syncData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static class JSONFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String filename) {
            Log.d("MVRT", "validating filename " + filename);
            return filename.matches("pitscout-\\d+.json");
        }
    }

    static class LocalDataAdapter extends RecyclerView.Adapter<LocalDataAdapter.ViewHolder>{
        String[] filenames;
        ItemSelectListener selectListener;

        public LocalDataAdapter(String[] files){
            filenames = files;
        }

        public String[] getFilenames(){
            return filenames;
        }

        public void setSelectListener(ItemSelectListener listener){
            selectListener = listener;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView filenameView;
            private ItemSelectListener selectListener;

            public ViewHolder(View itemView, ItemSelectListener selectListener) {
                super(itemView);
                this.selectListener = selectListener;
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d("MVRT", "");
                        return false;
                    }
                });
                filenameView = (TextView)itemView.findViewById(R.id.filelistitem_filename);
            }

            public String getFilename(){
                return filenameView.getText().toString();
            }

            public void setFilename(String fn){
                filenameView.setText(fn);
            }

            @Override
            public void onClick(View v) {
                if(selectListener != null)selectListener.itemSelected(getFilename());
            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filelist, parent, false);
            ViewHolder vh = new ViewHolder(v, selectListener);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setFilename(filenames[position]);
        }

        public void clear(){
            setFilenames(new String[0]);
        }

        public void setFilenames(String[] filenames){
            this.filenames = filenames;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return filenames.length;
        }

    }

}
interface ItemSelectListener {
    void itemSelected(String filename);
}