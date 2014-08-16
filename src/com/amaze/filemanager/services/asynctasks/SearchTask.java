package com.amaze.filemanager.services.asynctasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.amaze.filemanager.activities.MainActivity;
import com.amaze.filemanager.fragments.Main;
import com.amaze.filemanager.utils.Futils;
import com.amaze.filemanager.utils.RootHelper;

import java.io.File;
import java.util.ArrayList;

public class SearchTask extends AsyncTask<Bundle, String, ArrayList<String[]>> {
    ProgressDialog a;
    boolean run = true;
    MainActivity m;
    Main tab;

    public SearchTask(MainActivity m, Main tab) {
        this.m = m;
        this.tab = tab;
    }

    @Override
    public void onPreExecute() {
        a = new ProgressDialog(m);
        a.setIndeterminate(true);
        a.setTitle("Searching");
        a.setButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface p1, int p2) {
                run = false;
                a.dismiss();
                // TODO: Implement this method
            }
        });
        a.setCancelable(false);
        a.show();

    }

    @Override
    public void onProgressUpdate(String... val) {
        if (a != null) {
            a.setMessage("Searching " + val[0]);
        }

    }

    protected ArrayList<String[]> doInBackground(Bundle[] p1) {
        Bundle b = p1[0];
        String FILENAME = b.getString("FILENAME");
        String FILEPATH = b.getString("FILEPATH");

        // TODO: Implement this method
        return getSearchResult(new File(FILEPATH), FILENAME);
    }

    @Override
    public void onPostExecute(ArrayList<String[]> c) {
        if (run) {

            tab.loadsearchlist(c);
        }
        a.dismiss();
    }

    ArrayList<String[]> lis = new ArrayList<String[]>();

    public ArrayList<String[]> getSearchResult(File f, String text) {
        lis.clear();


        search(f, text);

        return lis;
    }


    public void search(File file, String text) {
        if (file.isDirectory()) {

            ArrayList<String[]> f=RootHelper.getFilesList(file.getPath(),tab.rootMode,tab.showHidden);
            // do you have permission to read this directory?

                for (String[] x : f) {
                    File temp=new File(x[0]);
                    publishProgress(temp.getPath());
                    if (run) {
                        if (temp.isDirectory()) {
                            if (temp.getName().toLowerCase()
                                    .contains(text.toLowerCase())) {
                                lis.add(x);
                            }
                            //System.out
                            //.println(file.getAbsoluteFile() );

                            search(temp, text);

                        } else {
                            if (temp.getName().toLowerCase()
                                    .contains(text.toLowerCase())) {
                                lis.add(x);
                            }
                        }//	publishProgress(temp.getPath());
                    }
                }
            } else {
                System.out
                        .println(file.getAbsoluteFile() + "Permission Denied");
            }
        }
    }

