<%-- 
    Document   : result
    Created on : 21 oct. 2020, 19:48:09
    Author     : jabri
--%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        
    <style>
table, td, th {  
  border: 1px solid #ddd;
  text-align: left;
}

table {
  border-collapse: collapse;
  width: 100%;
}

th, td {
  padding: 15px;
}
</style>    
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Required meta tags-->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Colrolib Templates">
    <meta name="author" content="Colrolib">
    <meta name="keywords" content="Colrolib Templates">
        <title>Searching Result</title>
        
    <!-- Icons font CSS-->
    <link href="vendor/mdi-font/css/material-design-iconic-font.min.css" rel="stylesheet" media="all">
    <link href="vendor/font-awesome-4.7/css/font-awesome.min.css" rel="stylesheet" media="all">
    <!-- Font special for pages-->
    <link href="https://fonts.googleapis.com/css?family=Lato:100,100i,300,300i,400,400i,700,700i,900,900i" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Poppins:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <!-- Vendor CSS-->
    <link href="vendor/select2/select2.min.css" rel="stylesheet" media="all">
    <link href="vendor/datepicker/daterangepicker.css" rel="stylesheet" media="all">

    <!-- Main CSS-->
    <link href="css/main.css" rel="stylesheet" media="all">
    
    </head>
   <body>
       <c:if test="${totalHits eq 0}"> 
    <div class="page-wrapper bg-color-1 p-t-165 p-b-100">
        <div class="wrapper wrapper--w680">
            <div class="card card-2">
                <div class="card-body">
                    <ul class="tab-list">
                        <li class="tab-list__item active">
                            <a class="tab-list__link" href="#tab1" data-toggle="tab"><b><img src="error.png" height="40" width="40">  Result :  
                                    
                                    No Files Found! 
                                </b></a>
                        </li>                                            
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="tab1">
                         
                        </div>   
                        </div> 
                </div>
            </div>
        </div>
    </div>
</c:if>
       
       <c:if test="${totalHits > 0}"> 
    <div class="page-wrapper bg-color-1 p-t-165 p-b-100">
        <div class="wrapper wrapper--w680">
            <div class="card card-2">
                <div class="card-body">
                    <ul class="tab-list">
                        <li class="tab-list__item active">
                            <a class="tab-list__link" href="#tab1" data-toggle="tab"><b><p style="font-size:15px"><img src="ok.png" height="50" width="50"><u> Result: ${totalHits} File(s) that match your key-words found.</u></p> </b></a>
                        </li> 
                    </ul>

                        <div class="tab-content">
                        <div class="tab-pane active" id="tab1">
                            
                    <table>
                <thead>
                    <tr>
                        <th></th>
                        <th>File's Name</th>                       
                        
                    </tr>
                </thead>
                <tbody>                  
                    <c:forEach items="${listName}" var="listN">                   
                    <tr>						
                        <td><a style="text-decoration:none" href="DisplayBROWSER?pdf=${listN}"><img src="pdf.png" height="50" width="50">&nbsp;</a></td>                   
                        <td>${listN}</td> 
                        
                     </tr> 
                     </c:forEach>
                </tbody>
            </table>
                        </div>   
                        </div> 
                </div>                       
            </div>                       
        </div>                                                
    </div>
</c:if>
    <!-- Jquery JS-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <!-- Vendor JS-->
    <script src="vendor/select2/select2.min.js"></script>
    <script src="vendor/jquery-validate/jquery.validate.min.js"></script>
    <script src="vendor/bootstrap-wizard/bootstrap.min.js"></script>
    <script src="vendor/bootstrap-wizard/jquery.bootstrap.wizard.min.js"></script>
    <script src="vendor/datepicker/moment.min.js"></script>
    <script src="vendor/datepicker/daterangepicker.js"></script>

    <!-- Main JS-->
    <script src="js/global.js"></script>

</body>
</html>
