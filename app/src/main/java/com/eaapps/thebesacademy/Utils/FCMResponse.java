package com.eaapps.thebesacademy.Utils;

import java.util.List;

/**
 * Created by eslamandroid on 12/12/17.
 */

public class FCMResponse {
    public long multicast_id;
    public int sucess;
    public int failure;
    public int canonical_ids;
    public List<Result> resuls;

    public FCMResponse() {
    }

    public FCMResponse(long multicast_id, int sucess, int failure, int canonical_ids, List<Result> resuls) {
        this.multicast_id = multicast_id;
        this.sucess = sucess;
        this.failure = failure;
        this.canonical_ids = canonical_ids;
        this.resuls = resuls;
    }

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSucess() {
        return sucess;
    }

    public void setSucess(int sucess) {
        this.sucess = sucess;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public List<Result> getResuls() {
        return resuls;
    }

    public void setResuls(List<Result> resuls) {
        this.resuls = resuls;
    }
}
