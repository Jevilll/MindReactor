package com.example.jevil.mindreactor.Events.TabEvents;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.jevil.mindreactor.BDHelpers.DBHelper;
import com.example.jevil.mindreactor.R;

public class AddNewEventActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etMark, etMinutes;
    RadioButton rbCont, rbOnce, rbUseful, rbHarmful;

    private DBHelper dbHelper;
    private Intent intent;
    private SQLiteDatabase db;
    private String typeEvent = "cont", benefit = "useful", name, mark;
    Context context;
    public Animation shakeAnimation;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initialize(); // инициализируем все сподряд

        if (intent.getStringExtra("type").equals("edit")) {// при редактировании события
            this.setTitle("Редактирование");
            isEdit(); // метод, заполняющий поля при редактировании
        } else { // при добавлении события
            this.setTitle("Добавление новой задачи");
        }
    }

    @Override
    public void onClick(View v) {
        name = deleteSpace(etName.getText().toString());
        mark = etMark.getText().toString();
        ContentValues contentValues = new ContentValues(); // добавляем строки в таблицу (ключ - значение)

        switch (v.getId()) {
            case R.id.fab_add_event: // добавляем запись
                if (checkFields()) {
                    contentValues.put(DBHelper.KEY_NAME, name);
                    contentValues.put(DBHelper.KEY_MARK, mark);
                    contentValues.put(DBHelper.KEY_TYPE, typeEvent);
                    contentValues.put(DBHelper.KEY_BENEFIT, benefit);
                    if (intent.getStringExtra("type").equals("edit")) { // при редактировании изменяем
                        db.update(DBHelper.TABLE_TASKS, contentValues, DBHelper.KEY_ID + "= ?", new String[]{String.valueOf(intent.getIntExtra("id", -1))}); //изменение данных в БД
                    } else { // добавляем новое
                        db.insert(DBHelper.TABLE_TASKS, null, contentValues); //добавление данных в БД
                    }
                    dbHelper.close();
                    db.close();
                    finish();
                }
                break;
            case R.id.rbCont:
                typeEvent = "cont";
                break;
            case R.id.rbOnce:
                typeEvent = "once";
                etMinutes.setVisibility(View.INVISIBLE);
                break;
            case R.id.rbUseful:
                benefit = "useful";
                break;
            case R.id.rbHarmful:
                benefit = "harmful";
                break;
        }

    }

    protected void initialize() {
        intent = getIntent();
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        context = this;

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        fab = (FloatingActionButton) findViewById(R.id.fab_add_event);
        fab.setOnClickListener(this);

        rbCont = (RadioButton) findViewById(R.id.rbCont);
        rbCont.setOnClickListener(this);

        rbOnce = (RadioButton) findViewById(R.id.rbOnce);
        rbOnce.setOnClickListener(this);

        rbUseful = (RadioButton) findViewById(R.id.rbUseful);
        rbUseful.setOnClickListener(this);

        rbHarmful = (RadioButton) findViewById(R.id.rbHarmful);
        rbHarmful.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etMark = (EditText) findViewById(R.id.etMark);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void isEdit() {
        Cursor c = db.query(DBHelper.TABLE_TASKS, null, "_id" + " = " + intent.getIntExtra("id", -1), null, null, null, null);
        c.moveToFirst();
        etName.setText(c.getString(c.getColumnIndex("name")));
        etMark.setText(String.valueOf(c.getInt(c.getColumnIndex("mark"))));
        typeEvent = c.getString(c.getColumnIndex("type"));
        // устанавливаем полезность события
        String isHarmful = String.valueOf(c.getString(c.getColumnIndex(DBHelper.KEY_BENEFIT)));
        if (isHarmful.equals("harmful")) {
            rbHarmful.setChecked(true);
            benefit = "harmful";
        }
        // устанавливаем продолжительность
        if (typeEvent.equals("once")) {
            rbOnce.setChecked(true);
        }
        c.close();
    }

    public boolean checkFields() {
        if (name.equals("")) { // проверка заполнения
            Toast.makeText(context, "Поля обязательны для заполнения", Toast.LENGTH_SHORT).show();
            etName.startAnimation(shakeAnimation);
        } else if (mark.equals("")) {
            Toast.makeText(context, "Поля обязательны для заполнения", Toast.LENGTH_SHORT).show();
            etMark.startAnimation(shakeAnimation);
        } else { // если все нужные поля заполнены, добавляем (изменяем)
            return true;
        }
        return false;
    }

    private String deleteSpace(String text) {
        String space;
        if (text.length() != 0) {
            space = text.substring(text.length() - 1);
            if (space.equals(" ")) {
                text = text.substring(0, text.length()-1);
            }
        }
        return text;
    }
}

