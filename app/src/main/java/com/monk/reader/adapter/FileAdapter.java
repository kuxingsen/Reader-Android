package com.monk.reader.adapter;

import android.app.Instrumentation;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monk.reader.R;
import com.monk.reader.ui.fragment.FileFragment;
import com.monk.reader.utils.FileUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private static final String TAG = "FileAdapter";

    private File dir;
    private File[] files;

    private String filter;

    private Set<String> addedSet;

    public FileAdapter(File dir, String filenameFilter){
        this(dir, filenameFilter, null);
    }
    public FileAdapter(File[] files, String filenameFilter){
        this(files,filenameFilter,null);
    }
    public FileAdapter(File[] files, String filenameFilter,Set<String> addedSet){
        if(addedSet == null) addedSet = new HashSet<>();
        this.filter = filenameFilter;
        this.addedSet = addedSet;
        setFiles(files);
    }
    public FileAdapter(File dir, String filenameFilter,Set<String> addedSet){
        if(addedSet == null) addedSet = new HashSet<>();
        this.filter = filenameFilter;
        this.addedSet = addedSet;
        setDir(dir);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent,false);
//        View view = View.inflate(parent.getContext(),R.layout.item_file,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: ");
        File file;
        if(position == 0 &&dir != null){
            file = dir;
            holder.tvName.setText("..");
        }else {
            if(dir != null)  position--;
            file = files[position];
            holder.tvName.setText(file.getName());
        }
        if(file.isDirectory()){
            holder.tvType.setBackgroundResource(R.mipmap.folder);
            holder.tvSize.setText("目录");
            holder.tvAdd.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(new DirectoryClick(position,file,holder));
        }else {
            holder.tvType.setBackgroundResource(R.mipmap.file_type_txt);
            holder.tvSize.setText(FileUtils.formatFileSize(file.length()));
            holder.tvAdd.setVisibility(View.VISIBLE);
            Log.i(TAG, "onBindViewHolder: " + addedSet + "    " + file.getAbsolutePath() + "   " + addedSet.contains(file.getAbsolutePath()));
            if(addedSet !=null && addedSet.contains(file.getAbsolutePath())){

                holder.tvAdd.setText("已在书架");
                holder.tvAdd.setTextColor(holder.itemView.getResources().getColor(R.color.gray));
                holder.itemView.setOnClickListener(null);
            }else {
                holder.tvAdd.setText("加入书架");
                holder.itemView.setOnClickListener(new FileClick(position,file,holder));
            }
        }
    }

    @Override
    public int getItemCount() {
        return dir==null?files.length:files.length+1;
    }

    public void setDir(File dir) {
        this.dir = dir;
        files = FileUtils.getAllFiles(dir, filter);
        notifyDataSetChanged();
    }

    public File getDir() {
        return dir;
    }

    public void setFiles(File[] files){
        this.files = files;
        this.dir = null;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_add)
        TextView tvAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    private FileClickCallBack clickFilterFileCallBack;
    public void setClickFilterFileCallBack(FileClickCallBack clickDirectoryCallBack) {
        this.clickFilterFileCallBack = clickDirectoryCallBack;
    }

    public FileClickCallBack getClickFilterFileCallBack() {
        return clickFilterFileCallBack;
    }
    private ReplaceFileFragmentCallBack replaceFileFragmentCallBack;
    public void setReplaceFileFragmentCallBack(ReplaceFileFragmentCallBack replaceFileFragmentCallBack) {
        this.replaceFileFragmentCallBack = replaceFileFragmentCallBack;
    }

    public ReplaceFileFragmentCallBack getReplaceFileFragmentCallBack() {
        return replaceFileFragmentCallBack;
    }

    private FileClickCallBack clickDirectoryCallBack = (v,p,d)->{

        FileAdapter adapter = new FileAdapter(d, filter, addedSet);
        adapter.setClickFilterFileCallBack(clickFilterFileCallBack);
        adapter.setReplaceFileFragmentCallBack(replaceFileFragmentCallBack);
        replaceFileFragmentCallBack.replace(new FileFragment(adapter));
    };

    public interface ReplaceFileFragmentCallBack {
        void replace(FileFragment fileFragment);
    }

    public interface FileClickCallBack {
        void click(View v,int position,File file);
    }

    class DirectoryClick implements OnClickListener {

        private  ViewHolder viewHolder;
        private  File file;
        private int position;
        public DirectoryClick(int position,File file, ViewHolder viewHolder){
            this.position = position;
            this.file = file;
            this.viewHolder = viewHolder;
        }
        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: "+position+file.getName());
            if ("..".equals(viewHolder.tvName.getText().toString())) {
                new Thread(()->{
                    Instrumentation inst= new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent. KEYCODE_BACK);
                }).start();
                return;
            }
            if(clickDirectoryCallBack != null) {
                clickDirectoryCallBack.click(v,position,this.file);
            }
        }
    }
    class FileClick implements OnClickListener {

        private  ViewHolder viewHolder;
        private  File file;
        private int position;
        public FileClick(int position, File file, ViewHolder viewHolder){
            this.position = position;
            this.file = file;
            this.viewHolder = viewHolder;
        }
        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: "+position+file.getName());

            if(clickFilterFileCallBack != null)
            {
                clickFilterFileCallBack.click(v,position,this.file);
                if(addedSet != null) {
                    addedSet.add(file.getAbsolutePath());
                }
                viewHolder.tvAdd.setText("已在书架");
                viewHolder.tvAdd.setTextColor(viewHolder.itemView.getResources().getColor(R.color.gray));
                viewHolder.itemView.setOnClickListener(null);
            }
        }
    }
}
