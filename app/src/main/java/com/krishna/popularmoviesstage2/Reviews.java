package com.krishna.popularmoviesstage2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krishna on 09/07/16.
 */
public class Reviews {
    public Integer id;
    public Integer page;
    public List<Result> results = new ArrayList<Result>();
    public Integer totalPages;
    public Integer totalResults;

    public class Result {
        public String id;
        public String author;
        public String content;
        public String url;

    }
}
