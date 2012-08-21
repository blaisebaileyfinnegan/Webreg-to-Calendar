<script src="reset.js"></script>
<%
	String error = request.getParameter("error");
	if (error != null)
	{
		if (error.equals("1")) {
%>
<p>An invalid quarter was specified! Please try again.</p>
<%
		}
		else if (error.equals("2")) {
%>
<p>Oops! Did you enter a correct study list? We were unable to process your input. Try copying and pasting your study list again. Do NOT modify the contents.</p>
<%
		}
		else if (error.equals("3")) {
%>
<p>Please enter a valid calendar name!</p>
<%
		}
	}
%>