<%@ page import="java.util.List" %>
<%@ page import="obj.Hotels" %>
<%@ page import="pagination.Pagination" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<%

    List<Hotels> hotelsList = (List<Hotels>) request.getAttribute("hotelsList");
    int currentPage = (int) request.getAttribute("currentPage");
    int totalPages = (int) request.getAttribute("totalPages");

    Pagination pagination = new Pagination();
    pagination.setTotalEnregistrements(totalPages * 5);
    pagination.setTotalParPage(5);
    pagination.setClassesCSS("pagination-sm");
    pagination.setUrl("TraitementPagination");

    pagination.setPageActuelle(String.valueOf(currentPage));

    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exemple de Pagination</title>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <link rel="stylesheet" href="assets/css/pagination.css">
    
    </head>
    <body>
        <div class="container">
            <h2 class="page-header">Liste des Hôtels</h2>
            <table class="table table-striped" border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Description</th>
                        <th>Adresse</th>
                        <th>Date d'Insertion</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Hotels hotel : hotelsList) { %>
                    <tr>
                        <td><%= hotel.getId_hotel() %></td>
                        <td><%= hotel.getNom_hotel() %></td>
                        <td><%= hotel.getDescription() %></td>
                        <td><%= hotel.getAdresse_hotel() %></td>
                        <td><%= hotel.getDate_insertion() %></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>

            <nav aria-label="Page navigation" class="pagination-container">
                <ul class="pagination">
                    <%= pagination.paginationNumerique() %>
                </ul>
            </nav>
        
        </div>

    </body>
</html>


<script>
        document.addEventListener('DOMContentLoaded', function() {
            var currentPage = <%= currentPage %>; // Récupérez la page actuelle côté serveur
            var paginationLinks = document.querySelectorAll('.pagination a');

            paginationLinks.forEach(function(link) {
                if (parseInt(link.innerHTML) === currentPage) {
                    link.classList.add('active');
                }
            });
        });
</script>