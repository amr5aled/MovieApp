package com.example.android.project_1;

import java.util.List;

/**
 * Created by amr5aled on 3/10/2018.
 */

public class TrailerResponse {


    private int id_trailer;

    private List<Trailer> results;

    public int getIdTrailer(){
        return id_trailer;
    }

    public void seIdTrailer(int id_trailer){
        this.id_trailer = id_trailer;
    }

    public List<Trailer> getResults(){
        return results;
    }
}