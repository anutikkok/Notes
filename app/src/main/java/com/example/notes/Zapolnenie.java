package com.example.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class Zapolnenie  extends AppCompatActivity {


    public String LOG_TAG = "LogsDB";
    public EditText Edit_Text;
    long zametka_Id=0;
    DBHelper dbHelper;
    SQLiteDatabase database;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zapolnenie);
        Edit_Text = (EditText) findViewById(R.id.editText);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        Cursor cursor;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            zametka_Id= extras.getLong("id");
        }
        // если 0, то добавление
        if (zametka_Id > 0) {
            // получаем элемент по id из бд
            cursor = database.rawQuery("select * from " + DBHelper.TABLE_NOTES + " where " +
                    DBHelper.KEY_ID + "=?", new String[]{String.valueOf(zametka_Id)});
            cursor.moveToFirst();
            Edit_Text.setText(cursor.getString(1));

            cursor.close();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_zapoln, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name = Edit_Text.getText().toString();

        switch (item.getItemId()) {

            case R.id.menu_del:
                //удаление заметки
                database.delete(DBHelper.TABLE_NOTES, "_id = ?", new String[]{String.valueOf(zametka_Id)});
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_save:
                //сохранение заметки
                Log.d(LOG_TAG, name);
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.NOTES_TEXT, Edit_Text.getText().toString());

                if (zametka_Id > 0) {
                    database.update(DBHelper.TABLE_NOTES, cv, DBHelper.KEY_ID + "=" + String.valueOf(zametka_Id), null);
                } else {
                    database.insert(DBHelper.TABLE_NOTES, null, cv);
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
