package com.crashingdaily.syndication;

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

import com.crashingdaily.syndication.Aggregator;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;


public class FeedTag extends SimpleTagSupport {

    protected String url;
    protected String feed;
    protected String proxyAddress;
    protected String proxyPort;
    protected int timeout;
    protected int varScope;
    
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

    public void setUrl(String url) throws JspException {
        this.url = url;
    }
    
    public void setTimeout(int timeout) throws JspException {
        this.timeout = timeout;
    }
    
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
        Aggregator agg = new Aggregator();
        agg.setTimeout(timeout);
        SyndFeed mergedFeed = agg.merge(args);
        return mergedFeed;
    }    
}