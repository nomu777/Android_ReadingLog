package com.example.nomuradaichi.readinglog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class ReadingLogEditActivity extends AppCompatActivity {
    private Realm mRealm;
    EditText mTitleEdit;
    EditText mAuthorEdit;
    EditText mPublisherEdit;
    EditText mEndDateEdit;
    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_log_edit);
        mRealm = Realm.getDefaultInstance();
        mTitleEdit = (EditText) findViewById(R.id.titleEdit);
        mAuthorEdit = (EditText) findViewById(R.id.authorEdit);
        mPublisherEdit = (EditText) findViewById(R.id.publisherEdit);
        mEndDateEdit = (EditText) findViewById(R.id.endDateEdit);
        mDelete = (Button) findViewById(R.id.delete);

        long readingLogId = getIntent().getLongExtra("readingLog_id", -1);
        if(readingLogId != -1) {
            RealmResults<ReadingLog> results = mRealm.where(ReadingLog.class)
                    .equalTo("id", readingLogId).findAll();
            ReadingLog readingLog = results.first();
            mTitleEdit.setText(readingLog.getTitle());
            mAuthorEdit.setText(readingLog.getAuthor());
            mPublisherEdit.setText(readingLog.getPublisher());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(readingLog.getEndDate());
            mEndDateEdit.setText(date);
            mDelete.setVisibility(View.VISIBLE);
        } else {
            mDelete.setVisibility(View.INVISIBLE);
        }
    }

    public void onSaveTapped(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateParse = new Date();
        try {
            dateParse = sdf.parse(mEndDateEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date date = dateParse;
        long readingLogId = getIntent().getLongExtra("readingLog_id", -1);

        if(readingLogId != -1) {
            final RealmResults<ReadingLog> results = mRealm.where(ReadingLog.class)
                    .equalTo("id", readingLogId).findAll();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ReadingLog readingLog = results.first();
                    readingLog.setTitle(mTitleEdit.getText().toString());
                    readingLog.setAuthor(mAuthorEdit.getText().toString());
                    readingLog.setPublisher(mPublisherEdit.getText().toString());
                    readingLog.setEndDate(date);
                }
            });
            Snackbar.make(findViewById(android.R.id.content),
                    "アップデートしました", Snackbar.LENGTH_LONG)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).setActionTextColor(Color.YELLOW).show();
        } else {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Number maxId = realm.where(ReadingLog.class).max("id");
                    long nextId = 0;
                    if(maxId != null) {
                        nextId = maxId.longValue() + 1;
                    }
                    ReadingLog readingLog = realm.createObject(ReadingLog.class, new Long(nextId));
                    readingLog.setTitle(mTitleEdit.getText().toString());
                    readingLog.setAuthor(mAuthorEdit.getText().toString());
                    readingLog.setPublisher(mPublisherEdit.getText().toString());
                    readingLog.setEndDate(date);
                }
            });
            Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onDeleteTapped(View view) {
        final long readingLogId = getIntent().getLongExtra("readingLog_id", -1);
        if(readingLogId != -1) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ReadingLog readingLog = realm.where(ReadingLog.class)
                            .equalTo("id", readingLogId).findFirst();
                    readingLog.deleteFromRealm();
                }
            });
        }
    }
}
