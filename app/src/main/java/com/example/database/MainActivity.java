package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.app.AlertDialog;


import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.database.databinding.ActivityMainBinding;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //long press dialog
    Button btnDelete, btnUpdate;
    //Edit text for save dialog
    EditText nameEt, rollNoEt, feeEt;
    //AppCompact button for save dialog
    AppCompatButton cancelBtn, saveBtn;
    //Intent putExtra Keys
    public static final String NAME_KEY = "Name";
    public static final String ROLL_NO_KEY = "RollNo";
    public static final String FEE_KEY = "Fee";
    //Binding reference
    ActivityMainBinding binding;
    //list reference
    List<ModelClass> modelClassList = new ArrayList<>();
    //Adaptor reference
    Adaptor adaptor;
    //MyHelper class reference
    MyHelper myHelper;
    //Custom broadcast action
    public static final String MY_ACTION = "com.example.database.DB_NOTIFY";
    //delete custom broadcast action
    public static final String MY_DELETE_ACTION = "com.cas.delete.data";

    ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Register custom broadcast
        registerReceiver(myBroadCast, new IntentFilter(MY_ACTION));
        //create object of MyHelper class
        myHelper = MyHelper.getHelperClassObject(this);
        //data fetch
        modelClassList = myHelper.fetchData();
        setAdaptor();
        //Floating button clickListener
        binding.dataEnterFloatingBtn.setOnClickListener(view -> {
            customDialog();
        });
        //RecyclerView clickListener
        adaptorClickListener();

        //LongClickListener
        longClick();

        //call delete broadcast
        registerReceiver(deleteBroadcast, new IntentFilter(MY_DELETE_ACTION));


        shareActionProvider=new ShareActionProvider(this);

    }

    public void customDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        rollNoEt = view.findViewById(R.id.rollNoEt);
        nameEt = view.findViewById(R.id.nameEt);
        feeEt = view.findViewById(R.id.feeEt);
        cancelBtn = view.findViewById(R.id.btnCancel);
        saveBtn = view.findViewById(R.id.btnSave);

        AlertDialog builder = new AlertDialog.Builder(this).setView(view).create();
        builder.show();
        //dialog cancel btn clickListener
        cancelBtn.setOnClickListener(v -> {
            builder.dismiss();
        });
        saveBtn.setOnClickListener(v -> {

            if (nameEt.getText().toString().isEmpty()) {
                nameEt.setError("Please enter Name");
                nameEt.hasFocus();
            } else if (rollNoEt.getText().toString().isEmpty()) {
                rollNoEt.setError("Please enter RollNo");
                rollNoEt.hasFocus();
            } else if (feeEt.getText().toString().isEmpty()) {
                feeEt.setError("Please enter fee");
                feeEt.hasFocus();
            }
            int rollNo = Integer.parseInt(rollNoEt.getText().toString());
            String name = nameEt.getText().toString();
            float fee = Float.parseFloat(feeEt.getText().toString());
            builder.dismiss();
            if (myHelper.insertData(new ModelClass(name,  +rollNo,fee))) {
                Toast.makeText(getApplicationContext(), "Dta inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Data not inserted", Toast.LENGTH_SHORT).show();
                builder.dismiss();
            }

        });
    }

    private void setAdaptor() {
        adaptor = new Adaptor((modelClassList));
        binding.rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rv.setAdapter(adaptor);
    }

    BroadcastReceiver myBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MY_ACTION)) {
                Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show();
                modelClassList = myHelper.fetchData();
                setAdaptor();


                adaptor.setMyLongClick(new Adaptor.MyLongClick() {
                    @Override
                    public void LongClick(View view, int index) {
                        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.long_layout, null);
                        AlertDialog builder = new AlertDialog.Builder(MainActivity.this).setView(v).create();
                        builder.show();
                        btnDelete = v.findViewById(R.id.btnDelete);
                        btnUpdate = v.findViewById(R.id.btnUpdate);
                        //Delete btn
                        btnDelete.setOnClickListener(view1 -> {
                            ModelClass positionList = modelClassList.get(index);
                            if (myHelper.deleteDta(new ModelClass(positionList.getName(), positionList.getRollNo(), positionList.getFee()))) {
                                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "No Deleted", Toast.LENGTH_SHORT).show();
                            }
                            builder.dismiss();
                        });
                        btnUpdate.setOnClickListener(view1 -> {
                            ModelClass modelClassPosition = modelClassList.get(index);
                            Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                            intent.putExtra(NAME_KEY, modelClassPosition.getName());
                            intent.putExtra(ROLL_NO_KEY, modelClassPosition.getRollNo());
                            intent.putExtra(FEE_KEY, modelClassPosition.getFee());
                            startActivity(intent);
                            builder.dismiss();
                        });
                    }
                });
                adaptor.setMyClick(new Adaptor.MyClick() {
                    @Override
                    public void Click(View v, int index) {
                        ModelClass modelClassListPosition = modelClassList.get(index);
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        intent.putExtra(NAME_KEY, modelClassListPosition.getName());
                        intent.putExtra(ROLL_NO_KEY, modelClassListPosition.getRollNo());
                        intent.putExtra(FEE_KEY, modelClassListPosition.getFee());
                        startActivity(intent);
                    }
                });
            }
        }
    };


    private void adaptorClickListener() {
        adaptor.setMyClick(new Adaptor.MyClick() {
            @Override
            public void Click(View v, int index) {
                ModelClass modelClassListPosition = modelClassList.get(index);
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra(NAME_KEY, modelClassListPosition.getName());
                intent.putExtra(ROLL_NO_KEY, modelClassListPosition.getRollNo());
                intent.putExtra(FEE_KEY, modelClassListPosition.getFee());
                startActivity(intent);
            }
        });
    }

    private void longClick() {
        adaptor.setMyLongClick(new Adaptor.MyLongClick() {
            @Override
            public void LongClick(View view, int index) {
                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.long_layout, null);
                AlertDialog builder = new AlertDialog.Builder(MainActivity.this).setView(v).create();
                builder.show();
                btnDelete = v.findViewById(R.id.btnDelete);
                btnUpdate = v.findViewById(R.id.btnUpdate);
                //Delete btn
                btnDelete.setOnClickListener(view1 -> {
                    ModelClass positionList = modelClassList.get(index);
                    if (myHelper.deleteDta(new ModelClass(positionList.getName(), positionList.getRollNo(), positionList.getFee()))) {
                        Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "No Deleted", Toast.LENGTH_SHORT).show();
                    }
                    builder.dismiss();
                });
                btnUpdate.setOnClickListener(view1 -> {
                    ModelClass modelClassPosition = modelClassList.get(index);
                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                    intent.putExtra(NAME_KEY, modelClassPosition.getName());
                    intent.putExtra(ROLL_NO_KEY, modelClassPosition.getRollNo());
                    intent.putExtra(FEE_KEY, modelClassPosition.getFee());
                    startActivity(intent);
                    builder.dismiss();
                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(myBroadCast);
        unregisterReceiver(deleteBroadcast);
        super.onDestroy();
    }

    BroadcastReceiver deleteBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MY_DELETE_ACTION)) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                modelClassList = myHelper.fetchData();
                setAdaptor();

                adaptor.setMyLongClick(new Adaptor.MyLongClick() {
                    @Override
                    public void LongClick(View view, int index) {
                        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.long_layout, null);
                        AlertDialog builder = new AlertDialog.Builder(MainActivity.this).setView(v).create();
                        builder.show();
                        btnDelete = v.findViewById(R.id.btnDelete);
                        btnUpdate = v.findViewById(R.id.btnUpdate);
                        //Delete btn
                        btnDelete.setOnClickListener(view1 -> {
                            ModelClass positionList = modelClassList.get(index);
                            if (myHelper.deleteDta(new ModelClass(positionList.getName(), positionList.getRollNo(), positionList.getFee()))) {
                                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "No Deleted", Toast.LENGTH_SHORT).show();
                            }
                            builder.dismiss();
                        });
                        btnUpdate.setOnClickListener(view1 -> {
                            ModelClass modelClassPosition = modelClassList.get(index);
                            Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                            intent.putExtra(NAME_KEY, modelClassPosition.getName());
                            intent.putExtra(ROLL_NO_KEY, modelClassPosition.getRollNo());
                            intent.putExtra(FEE_KEY, modelClassPosition.getFee());
                            startActivity(intent);
                            builder.dismiss();
                        });
                    }
                });
                adaptor.setMyClick(new Adaptor.MyClick() {
                    @Override
                    public void Click(View v, int index) {
                        ModelClass modelClassListPosition = modelClassList.get(index);
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        intent.putExtra(NAME_KEY, modelClassListPosition.getName());
                        intent.putExtra(ROLL_NO_KEY, modelClassListPosition.getRollNo());
                        intent.putExtra(FEE_KEY, modelClassListPosition.getFee());
                        startActivity(intent);
                    }
                });
            }
        }
    };

    /// option menu and search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (modelClassList.contains(query)) {
                    adaptor.getFilter().filter(query) ;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptor.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}





