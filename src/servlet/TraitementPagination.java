package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import obj.Hotels;
import pagination.Pagination;

public class TraitementPagination extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new ServletException("Erreur lors du traitement de la requête", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new ServletException("Erreur lors du traitement de la requête", e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String pageStr = request.getParameter("page");
        int page = Pagination.getPageNumber(pageStr);
        int recordsPerPage = 5;
        
        Hotels hotel = new Hotels();
        List<Hotels> hotelsList = hotel.getAll();
        
        List<Hotels> paginatedList = Pagination.paginateList(hotelsList, page, recordsPerPage);
        
        int totalRecords = hotelsList.size();
        int totalPages = Pagination.calculateTotalPages(totalRecords, recordsPerPage);
        
        request.setAttribute("hotelsList", paginatedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/page/pagination.jsp");
        dispatcher.forward(request, response);
}



}
