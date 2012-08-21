<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="edu.uci.mvu1.webreg2cal.QuarterManager" %>

<html>
<head>

</head>
  <body>
	<link rel="stylesheet" type="text/css" href="css/main.css" />
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
   <script src="http://malsup.github.com/jquery.form.js"></script> 
   <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
   <script src="welcome.js"></script>
   
<div class="main">
<h1>UCI Webreg-to-Calendar</h1>
<p>I can convert your raw study list within Webreg into a neat calendar. Both Google Calendar and iCal (e.g. Apple iCal, Microsoft Outlook) formats are natively supported.<br/>
<a href="instructions.html" target="_blank">Instructions</a></p>

  <form id="mainform" action="/calendar" method="post">
  <p>
    <label>Paste study list from WebReg here:</label><br/>
    <textarea name="content" rows="14" cols="73" class="required"></textarea>
    </p>
    <p>
    <label>What would you like the calendar to be named?</label>
    <input type="text" name="name" class="required"/><br/>
    </p>
    <p>
    <label>Which quarter does this schedule belong to?</label>
    <select name="quarter">
    		<%=QuarterManager.retrieveQuartersHTML()%>
    </select>
    </p>
    <p>
    <label id="export">Export into:</label>
    <input id="submit_to_google" name="submit" type="submit" value="Google Calendar" />
    <input id="submit_to_ical" name="submit" type="submit" value="iCalendar" />
    </p>
  </form>
<div id="status"></div>
<p class="credits">
Classes which are TBA will not be included. You should always check to make sure that classes were correctly interpreted by the app before you start using any downloaded calendar.<br/> Created by Michael Vu<br/> To report a bug, send an email to mvu1.at.uci.edu</p>
</div>


  </body>
</html>