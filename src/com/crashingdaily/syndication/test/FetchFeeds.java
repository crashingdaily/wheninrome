package com.crashingdaily.syndication.test;

import com.crashingdaily.syndication.Aggregator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;


public class FetchFeeds {

    public static void main(final String[] args) {
        
        Aggregator agg = new Aggregator();
        
        try {
            SyndFeed mergedFeed = agg.merge(args);
            
            mergedFeed = agg.orderByDate(mergedFeed, "desc");
            
            System.out.println( mergedFeed.getTitle() );
            for ( Object entry: mergedFeed.getEntries() ) {
                System.out.println( ((SyndEntry)entry).getPublishedDate().toString()  +
                " - " + ((SyndEntry)entry).getTitle() +
                ((SyndEntry)entry).getDescription().getValue() );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}