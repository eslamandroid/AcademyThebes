package com.eaapps.thebesacademy.Utils;

import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public abstract class RetrieveData<T> {
    Context context;
    List<T> listRetrieve;

    public RetrieveData(Context context) {
        this.context = context;
        listRetrieve = new ArrayList<>();
    }


    // Database Reference
    public void RetrieveList(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveList<T> callBackRetrieveList) {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    if (!hasKey(object, listRetrieve)) {
                        listRetrieve.add(0, object);
                        callBackRetrieveList.onDataList(listRetrieve, dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    if (hasKey(object, listRetrieve)) {
                        int position = getPosition(object, listRetrieve);
                        listRetrieve.set(position, object);
                        callBackRetrieveList.onChangeList(listRetrieve, position);
                    } else {
                        onChildAdded(dataSnapshot, s);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    if (hasKey(object, listRetrieve)) {
                        int position = getPosition(object, listRetrieve);
                        listRetrieve.remove(position);
                        callBackRetrieveList.onRemoveFromList(position);
                    }
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //Database Query

    public void RetrieveList(final Class<T> tClass, Query reference, final CallBackRetrieveList<T> callBackRetrieveList) {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    if (!hasKey(object, listRetrieve)) {
                        listRetrieve.add(0, object);
                        callBackRetrieveList.onDataList(listRetrieve, dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    if (hasKey(object, listRetrieve)) {
                        int position = getPosition(object, listRetrieve);
                        listRetrieve.set(position, object);
                        callBackRetrieveList.onChangeList(listRetrieve, position);
                    } else {
                        onChildAdded(dataSnapshot, s);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    if (hasKey(object, listRetrieve)) {
                        int position = getPosition(object, listRetrieve);
                        listRetrieve.remove(position);
                        callBackRetrieveList.onRemoveFromList(position);
                    }
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //Database Reference
    public void RetrieveSingleTimes(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    //error
                    callBackRetrieveTimes.onData(object);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //Database Query

    public void RetrieveSingleTimes(final Class<T> tClass, Query reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    //error
                    callBackRetrieveTimes.onData(object);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void RetrieveSingleTimesRepeat(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        T object = data.getValue(tClass);
                        callBackRetrieveTimes.onData(object);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Database Query

    public void RetrieveSingleTimesRepeat(final Class<T> tClass, Query reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        T object = data.getValue(tClass);
                        callBackRetrieveTimes.onData(object);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //Database Reference

    public void RetrieveEventTimes(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    callBackRetrieveTimes.onData(object);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Database Query

    public void RetrieveEventTimes(final Class<T> tClass, Query reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    callBackRetrieveTimes.onData(object);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Database Reference

    public void RetrieveEventTimesRepeat(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        T object = data.getValue(tClass);
                        callBackRetrieveTimes.onData(object);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Database Query

    public void RetrieveEventTimesRepeat(final Class<T> tClass, Query reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        T object = data.getValue(tClass);
                        callBackRetrieveTimes.onData(object);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void haveChild(Class<T> tClass, DatabaseReference reference, String id, CallHaveChild<T> callHaveChild) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                T object = dataSnapshot.getValue(tClass);
                callHaveChild.hasChild(dataSnapshot.hasChild(id), object);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void haveChild(Class<T> tClass, Query reference, String id, CallHaveChild<T> callHaveChild) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                T object = dataSnapshot.getValue(tClass);
                callHaveChild.hasChild(dataSnapshot.hasChild(id), object);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void hasChild(Class<T> tClass, Query query, CallHasChild<T> callHasChild) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    T object = data.getValue(tClass);
                    callHasChild.hasChild(object);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean hasKey(T object, List<T> listRetrieve) {
        return false;
    }

    public int getPosition(T object, List<T> listRetrieve) {
        return 0;
    }

    public interface CallBackRetrieveList<T> {
        void onDataList(List<T> object, String key);

        void onChangeList(List<T> object, int position);

        void onRemoveFromList(int removePosition);
    }

    public interface CallBackRetrieveTimes<T> {
        void onData(T objects);
    }

    public interface CallHaveChild<T> {
        void hasChild(boolean has, T object);
    }

    public interface CallHasChild<T> {
        void hasChild(T object);
    }
}
