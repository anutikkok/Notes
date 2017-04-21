package com.example.notes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String LOG_TAG = "LogsDB"; //логи
    DBHelper dbHelper;
    //определяем массив типа String
    public List<String> values = new ArrayList<String>();
    ListView listView;
    public EditText Edit_Text;
    Cursor cursor;
    SQLiteDatabase database;
    SimpleCursorAdapter userAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        Edit_Text = (EditText) findViewById(R.id.editText);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Zapolnenie.class);
                intent.putExtra("id", id+1);
                startActivity(intent);
            }
        });

        dbHelper = new DBHelper(this);

        database = dbHelper.getWritableDatabase();

        Log.d(LOG_TAG, "Выборка из таблицы строк");
        cursor = database.query(DBHelper.TABLE_NOTES, null, null,
                null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (cursor!=null && cursor.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = cursor.getColumnIndex("_id");
            int textColIndex = cursor.getColumnIndex("text");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + cursor.getInt(idColIndex) + ", text = "
                                + cursor.getString(textColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false -
                // выходим из цикла
                values.add(cursor.getString(textColIndex));
                //добавляем массив строк в БД по имени в выборке
            } while (cursor.moveToNext());

        } else
            Log.d(LOG_TAG, "0 rows");

        //ArrayAdapter является простейшим адаптером, который специально
        // предназначен для работы с элементами списка типа ListView
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, values);

        listView.setAdapter(adapter);
        database.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create:
                startActivity(new Intent(MainActivity.this, Zapolnenie.class));
                cursor.close();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}





    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " выбран", Toast.LENGTH_LONG).show();
    }
*/



