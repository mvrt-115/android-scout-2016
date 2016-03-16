package com.mvrt.scout;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.JSONUtils;
import com.mvrt.mvrtlib.util.MatchInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

public class LocalDataFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ItemSelectListener {

    RecyclerView fileListRecycler;
    LocalDataAdapter localDataAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    Firebase firebase;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initFirebase();
    }

    private void initFirebase(){
        Firebase.setAndroidContext(getActivity().getApplication());
        firebase = new Firebase("http://teamdata.firebaseio.com");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_localdata, container, false);
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

    public void syncData(){
        String[] filenames = localDataAdapter.getFilenames();
        for(String file:filenames){
            readFile(file);
        }
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
            firebase.child("matches").child(info.toDbKey(scoutId)).updateChildren(JSONUtils.jsonToMap(scoutData));
            getActivity().deleteFile(filename);
            Log.d("MVRT", "Deleted file " + filename);
        }catch(JSONException e){
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

    static class JSONFilter implements FilenameFilter{

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