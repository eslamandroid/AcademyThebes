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
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callBackRetrieveList.exits(dataSnapshot.exists());
                callBackRetrieveList.hasChildren(dataSnapshot.hasChildren());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("check: " + dataSnapshot.exists());
                T object = dataSnapshot.getValue(tClass);
                if (!hasKey(object, listRetrieve)) {
                    listRetrieve.add(0, object);
                    callBackRetrieveList.onDataList(listRetrieve, (int) dataSnapshot.getChildrenCount());
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
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callBackRetrieveList.exits(dataSnapshot.exists());
                callBackRetrieveList.hasChildren(dataSnapshot.hasChildren());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                T object = dataSnapshot.getValue(tClass);
                if (!hasKey(object, listRetrieve)) {
                    listRetrieve.add(0, object);
                    callBackRetrieveList.onDataList(listRetrieve, (int) dataSnapshot.getChildrenCount());
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
                callBackRetrieveTimes.exits(dataSnapshot.exists());
                callBackRetrieveTimes.hasChildren(dataSnapshot.hasChildren());
                if (dataSnapshot.exists()) {
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

    public void RetrieveSingleTimes(final Class<T> tClass, Query reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callBackRetrieveTimes.exits(dataSnapshot.exists());
                callBackRetrieveTimes.hasChildren(dataSnapshot.hasChildren());
                if (dataSnapshot.exists()) {
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

    public void RetrieveEventTimes(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChildren()) {
                        T object = dataSnapshot.getValue(tClass);
                        callBackRetrieveTimes.onData(object);
                    } else {
                        callBackRetrieveTimes.hasChildren(false);

                    }
                } else {
                    callBackRetrieveTimes.exits(false);
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
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChildren()) {
                        T object = dataSnapshot.getValue(tClass);
                        callBackRetrieveTimes.onData(object);
                    } else {
                        callBackRetrieveTimes.hasChildren(false);

                    }
                } else {
                    callBackRetrieveTimes.exits(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Database Reference

    public void RetrieveSingleTimesRepeat(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            T object = data.getValue(tClass);
                            callBackRetrieveTimes.onData(object);
                        }
                    } else {
                        callBackRetrieveTimes.hasChildren(false);

                    }
                } else {
                    callBackRetrieveTimes.exits(false);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Database Query

    public void RetrieveSingleTimesRepeat(final Class<T> tClass, Query reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            T object = data.getValue(tClass);
                            callBackRetrieveTimes.onData(object);
                        }
                    } else {
                        callBackRetrieveTimes.hasChildren(false);

                    }
                } else {
                    callBackRetrieveTimes.exits(false);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void haveChild(DatabaseReference reference, String id, CallHaveChild callHaveChild) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callHaveChild.hasChild(dataSnapshot.hasChild(id));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void hasChild(DatabaseReference reference, CallHaveChild callHaveChild) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callHaveChild.hasChild(dataSnapshot.hasChildren());
                } else {
                    callHaveChild.hasChild(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getCountChild(DatabaseReference reference, CallCountChild callCountChild) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callCountChild.count(dataSnapshot.getChildrenCount());
                } else {
                    callCountChild.count(0);

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
        void onDataList(List<T> object, int countChild);

        void onChangeList(List<T> object, int position);

        void onRemoveFromList(int removePosition);

        void exits(boolean e);

        void hasChildren(boolean c);

    }

    public interface CallBackRetrieveTimes<T> {
        void onData(T objects);

        void hasChildren(boolean c);

        void exits(boolean e);

    }

    public interface CallHaveChild {
        void hasChild(boolean has);
    }

    public interface CallCountChild {
        void count(long c);
    }
}
