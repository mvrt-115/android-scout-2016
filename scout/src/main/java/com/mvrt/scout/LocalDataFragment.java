package com.mvrt.scout;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mvrt.mvrtlib.util.Constants;
import com.mvrt.mvrtlib.util.MatchInfo;

import java.io.File;
import java.io.FilenameFilter;

public class LocalDataFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ItemSelectListener {

    //TODO: Add icon showing if file has been synced

    RecyclerView fileListRecycler;
    LocalDataAdapter localDataAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

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

interface ItemSelectListener{
    void itemSelected(String filename);
}