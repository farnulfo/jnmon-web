<%-- 
    Document   : list
    Created on : 22 mars 2011, 22:39:08
    Author     : Franck
--%>

<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
  </head>
  <body>
    <%
      File currentDirectory = new File(".");
      out.println("<h2>Current directory : " + currentDirectory.getCanonicalPath() + "</h1>");
      out.println("<h2><a href=\"list.jsp?dir=..\">[..]</a></h2>");
      File files[] = currentDirectory.listFiles();
      for (File f : files) {
        if (f.isDirectory()) {
          out.println("<h3><a href=\"list.jsp?dir=f.getCanonicalPath()\">" + f.getCanonicalPath() + "</a></h3>");
        } else {
          out.println("<h3>" + f.getCanonicalPath() + "</h3>");
        }

      }
    %>
  </body>
</html>
