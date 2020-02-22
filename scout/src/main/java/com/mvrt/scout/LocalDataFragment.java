package com.mvrt.scout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.JSONUtils;
import com.mvrt.mvrtlib.util.MatchInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocalDataFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ItemSelectListener, ConfirmSyncListener {

    RecyclerView fileListRecycler;
    LocalDataAdapter localDataAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        fileListRecycler = (RecyclerView)view.findViewById(R.id.localdata_listrecycler);
        initRecycler();
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.localdata_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecycler(){
        fileListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        String[] filenames = getActivity().getFilesDir().list(new JSONFilter());
        localDataAdapter = new LocalDataAdapter(filenames);
        localDataAdapter.setSelectListener(this);
        fileListRecycler.setAdapter(localDataAdapter);
    }

    public void syncData() {
        ConfirmSyncDialog csd = new ConfirmSyncDialog();
        csd.setConfirmSyncListener(this);
        csd.show(getFragmentManager(), "");
    }

    @Override
    public void confirmSync() {
        String[] filenames = localDataAdapter.getFilenames();
        for(String file:filenames){
            readFile(file);
        }
        localDataAdapter.notifyDataSetChanged();
    }

    private void readFile(String filename){
        try {
            FileInputStream fis = getActivity().openFileInput(filename);

            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();

            JSONObject scoutData = new JSONObject(new String(buffer));

            MatchInfo matchInfo = MatchInfo.parse(scoutData.getString(Constants.JSON_DATA_MATCHINFO));
            int scoutId = scoutData.getInt(Constants.JSON_DATA_SCOUTID);
            uploadData(matchInfo, scoutId, scoutData, filename);

        }catch(FileNotFoundException e){
            Toast.makeText(getActivity(), "File " + filename + " not found", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", "File " + filename + " not found");
        }catch(IOException e){
            Toast.makeText(getActivity(), "File Read IOException", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", "File Read IOException");
        }catch(JSONException e){
            Toast.makeText(getActivity(), "File Read JSONException", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", "File Read JSONException");
        }
    }

    private void uploadData(MatchInfo info, int scoutId, JSONObject scoutData, String filename){
        try{
            final JSONObject obj = scoutData;
            new Thread(){
                public void run(){
                    try {
                        Log.e("JSON", obj.toString());
                        URL url = new URL(Constants.ScoutUrl);
                        String data = obj.toString();
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setFixedLengthStreamingMode(data.getBytes().length);

                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.connect();

                        OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                        os.write(data.getBytes());
                        os.flush();
                        os.close();

                        InputStream ip = conn.getInputStream();
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(ip));

                        // Print the response code
                        // and response message from server.
                        System.out.println("Response Code:" + conn.getResponseCode());
                        System.out.println("Response Message:" + conn.getResponseMessage());

                        Log.e("response", "Response Code:" + conn.getResponseCode());
                        Log.e("response", "Response Message:" + conn.getResponseMessage());

                        // to print the 1st header field.
                        System.out.println("Header field 1:" + conn.getHeaderField(1));

                        // print the response
                        StringBuilder response = new StringBuilder();
                        String responseSingle = null;
                        while ((responseSingle = br1.readLine()) != null)
                        {
                            response.append(responseSingle);
                        }

                        Log.e("response", response.toString());
                        ip.close();
                        conn.disconnect();
                    } catch(Exception e) {
                        Log.e("response", e.toString());
                    }
                }
            }.start();
            getActivity().deleteFile(filename);
            Log.d("MVRT", "Deleted file " + filename);
        }catch(Exception e){
            Toast.makeText(getActivity(), "Upload JSONException", Toast.LENGTH_SHORT).show();
            Log.e("MVRT", "Upload JSONException");
        }
    }

    @Override
    public void onRefresh() {
        String[] filenames = getActivity().getFilesDir().list(new JSONFilter());
        localDataAdapter.setFilenames(filenames);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void itemSelected(String filename) {
        Intent i = new Intent(getActivity(), MatchScoutingDataActivity.class);
        i.putExtra(Constants.INTENT_EXTRA_FILENAME, filename);
        startActivity(i);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_localdata, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
            return MatchInfo.validateFilename(filename);
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

interface ConfirmSyncListener {
    void confirmSync();
}

