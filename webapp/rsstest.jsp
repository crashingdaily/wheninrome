<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="synd" uri="http://crashingdaily.com/taglib/syndication" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<synd:feed 
    url="http://rss.slashdot.org/slashdot/eqWf"
    feed="merge">
http://reddit.com/.rss
http://rss.slashdot.org/slashdot/eqWf
</synd:feed>

<b>${merge.title}</b><br>
<hr>
<c:forEach items="${merge.entries}" var="e" begin="0" end="1700">
    <fmt:formatDate var="fdate" value="${e.publishedDate}" pattern="d MMMM yyyy"/>
    <a href='${e.link}'>${e.title}</a> - ${fdate}<br>
   <%-- ${e.description.value}<p> --%> 
</c:forEach>

