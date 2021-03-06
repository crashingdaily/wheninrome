<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wir" uri="http://crashingdaily.com/taglib/wheninrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
multiple urls will be split on space so spaces in url must be encoded
--%>
<wir:feed 
    url="http://rss.slashdot.org/slashdot/eqWf"
    feed="merge">
http://reddit.com/.rss
http://rss.slashdot.org/slashdot/eqWf
</wir:feed>

<b>${merge.title}</b><br>
<hr>
<c:forEach items="${merge.entries}" var="e" begin="0" end="1700">
    <fmt:formatDate var="fdate" value="${e.publishedDate}" pattern="d MMMM yyyy"/>
    <a href='${e.link}'>${e.title}</a> - ${fdate}<br>
   <%-- ${e.description.value}<p> --%> 
</c:forEach>
