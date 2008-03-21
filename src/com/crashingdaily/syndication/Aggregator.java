package com.crashingdaily.syndication;

// derivative of Randy J. Ray's DeliciousMerger2.java
// http://today.java.net/pub/a/today/2006/02/02/tour-of-rome.html

import java.io.PrintWriter;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.io.FeedException;

public class Aggregator {

    protected int timeout = 0;

    public Aggregator() {
    
    }
    
    public SyndFeed merge (String[] feedUrls) throws MalformedURLException, IOException, Exception {
        
        if (feedUrls.length == 0) return null;
        if (feedUrls.length == 1) return fetch(feedUrls[0]);
        
        final SyndFeed mergedFeed = new SyndFeedImpl();
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        final Map<String, SyndEntry> seenUrls = new HashMap<String, SyndEntry>();

        final StringBuffer tagList = new StringBuffer(feedUrls[0]);
        
        for (int argidx = 1; argidx < feedUrls.length; argidx++) {
            tagList.append(", ");
            tagList.append(feedUrls[argidx]);
        }

        mergedFeed.setTitle("Merged feeds:");
        mergedFeed.setDescription("Aggregation of feeds: " + tagList);
        mergedFeed.setFeedType("rss_2.0");
        mergedFeed.setAuthor("Aggregator");

        for (int idx = 0; idx < feedUrls.length; idx++) {
            try {
                SyndFeed feed = fetch(feedUrls[idx]);
                mergedFeed.setTitle(mergedFeed.getTitle() + " '" + feed.getTitle() + "'");
                for (SyndEntry entry: (List<SyndEntry>)feed.getEntries()) {
                    if (! seenUrls.containsKey(entry.getLink())) {
                        entries.add(entry);
                        seenUrls.put(entry.getLink(), entry);
                    }
                }
            } catch (FeedException fex) {
                throw new Exception(fex);
            }
        }

        mergedFeed.setEntries(entries);

        return mergedFeed;
    }
    
    public SyndFeed fetch (String url) throws MalformedURLException, IOException, FeedException {
        
        final SyndFeedInput input = new SyndFeedInput();
        URL feedUrl;
        SyndFeed feed = null;
        feedUrl = new URL(url);
        
        URLConnection connection = feedUrl.openConnection();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);

        feed = input.build(new XmlReader(connection));
        
        return feed;
    }

    public SyndFeed orderByDate (SyndFeed feed, String direction) {
        feed.setEntries( orderByDate(feed.getEntries(), direction) );        
        return feed;
    }
    
    public List orderByDate(List entries, String direction) {

        final Comparator datesorter = new Aggregator().new OrderByDate(direction);

        // copy entries from List to Array for sorting, 
        // then copy back to List
        SyndEntry[] entriesArray = new SyndEntry[entries.size()];
        entriesArray = (SyndEntry[])entries.toArray(entriesArray);
        Arrays.sort(entriesArray, datesorter);
        
        return Arrays.asList( entriesArray );
    }
    
    // setting for URLConnection connectTimeout and readTimeout
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private class OrderByDate implements Comparator {
        private boolean ascending;
    
        public OrderByDate() {
           ascending = true;
        }
        
        public OrderByDate (String direction) {
            if ( "asc".equalsIgnoreCase(direction) || 
                 "ascending".equalsIgnoreCase(direction)
                ) ascending = true;
            else if ( "desc".equalsIgnoreCase(direction) || 
                 "descending".equalsIgnoreCase(direction)
                ) ascending = false;
            else
                throw new IllegalArgumentException(
                  "'asc' or 'desc' must be passed to the " + 
                  this.getClass().getName() + " constructor."
                );
        }
    
        public int compare(final Object aObj, final Object bObj) {
            final Date aDate = ((SyndEntry)aObj).getPublishedDate();
            final Date bDate = ((SyndEntry)bObj).getPublishedDate();
            if (aDate == null || bDate == null) return -1;
            else if (ascending) return aDate.compareTo(bDate);
            else return bDate.compareTo(aDate);
        }
    }
}

