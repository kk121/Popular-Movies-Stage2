package com.krishna.popularmoviesstage2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krishna on 09/07/16.
 */
public class Trailer {
    public Integer id;
    public List<Result> results = new ArrayList<>();

    public class Result {
        public String id;
        public String iso6391;
        public String key;
        public String name;
        public String site;
        public Integer size;
        public String type;
    }
}
