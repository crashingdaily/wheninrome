package com.crashingdaily.syndication;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import com.crashingdaily.syndication.Aggregator;
import com.sun.syndication.feed.synd.SyndFeed;


public class SortTag extends SimpleTagSupport {


    protected String feed;
    protected String direction;
    protected String value;


    public void doTag() throws JspException {
        Aggregator agg = new Aggregator();
        SyndFeed mergedFeed = (SyndFeed) getJspContext().getAttribute(this.feed);
        mergedFeed = agg.orderByDate(mergedFeed, this.direction);
    }
    
    
    public void setFeed(String feed) throws JspException {
        this.feed = feed;
    }

    public void setDirection(String direction) throws JspException {
        this.direction = direction;
    }

    public void setValue(String value) throws JspException {
        this.value = value;
    }

}