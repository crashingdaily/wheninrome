package com.crashingdaily.wheninrome;

import java.io.StringWriter;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.JspFragment;

import com.crashingdaily.wheninrome.Aggregator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;


public class FeedTag extends SimpleTagSupport {

    protected String url;
    protected String feed;
    protected String proxyAddress;
    protected String proxyPort;
    protected int timeout;
    protected int varScope;

    protected String title;
    protected String description;
    protected String feedType;
    protected String channelLink;
    protected String author;
    
    public FeedTag() {
        super();
        varScope = PageContext.PAGE_SCOPE;    
    }
    
    public void doTag() throws JspException { 

        JspFragment body = getJspBody(); 
        
        if (body != null) {
            StringWriter buffer = new StringWriter();
            try {
                body.invoke(buffer);
            } catch (IOException ex) {
                throw new JspException(ex);
            }
            if (buffer.getBuffer().length() > 1) setUrl(buffer.toString());
        }

        String[] urlList = url.trim().split("\\s+");

        try {
            SyndFeed mergedFeed = getMergedFeed(urlList);            
            getJspContext().setAttribute(feed, mergedFeed, varScope);
        } catch (Exception e) {
            throw new JspException("FeedTag error. " + e);
        }
    }   

    public void setTitle(String title) throws JspException {
        this.title = title;
    }

    public void setDescription(String description) throws JspException {
        this.description = description;
    }

    public void setFeedType(String feedType) throws JspException {
        this.feedType = feedType;
    }

    public void setChannelLink(String channelLink) throws JspException {
        this.channelLink = channelLink;
    }

    public void setAuthor(String author) throws JspException {
        this.author = author;
    }

    /* list of urls for feeds */
    public void setUrl(String url) throws JspException {
        this.url = url;
    }
    
    /* how long to wait for feed to return */
    public void setTimeout(int timeout) throws JspException {
        this.timeout = timeout;
    }
    
    /* name for handle on a SyndFeed */
    public void setFeed(String feed) throws JspException {
        this.feed = feed;
    }
    
    public void setProxyAddress(String proxyAddress) throws JspException {
        this.proxyAddress = proxyAddress;
    }

    public void setProxyPort(String proxyPort) throws JspException {
        this.proxyPort = proxyPort;
    }

    public SyndFeed getMergedFeed(final String[] args) 
                throws MalformedURLException, IOException, Exception  {
        Aggregator agg = new Aggregator(title, description, feedType, 
                                        channelLink, author);
        agg.setTimeout(timeout);
        SyndFeed mergedFeed = agg.merge(args);
        return mergedFeed;
    }    
}
