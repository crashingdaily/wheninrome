package com.crashingdaily.wheninrome;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import com.crashingdaily.wheninrome.Aggregator;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.FeedException;

public class OutputTag extends SimpleTagSupport {

    protected String feed;

    public void doTag() throws JspException {
        SyndFeed mergedFeed = (SyndFeed) getJspContext().getAttribute(this.feed);
        
        try {
          SyndFeedOutput output = new SyndFeedOutput();
       //   getJspContext().getOut().write("Hello, world.");
          StringWriter writer = new StringWriter();
  
          output.output(mergedFeed, writer);
  //        writer.close();
  
          getJspContext().getOut().write(writer.toString());
        } catch (FeedException fe) {
            throw new JspException(fe);
        } catch (IOException ioe) {
            throw new JspException(ioe);
        }
    }
    
    
    public void setFeed(String feed) throws JspException {
        this.feed = feed;
    }

}


