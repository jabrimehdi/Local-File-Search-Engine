/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import org.apache.lucene.document.Document;
/**
 *
 * @author jabri
 */
@WebServlet(urlPatterns = {"/SearchByKeyWords"})
public class SearchByKeyWords extends HttpServlet {

/* Change The PATH */ 
    /* Create 3 Folders, System Files, Files (A folder contain uploaded PDF files) and Indexer (Contain Index files created by Lucene library) */
    private static final String system="C:\\Users\\**\\System Files";
    private static final String path="C:\\Users\\**\\System Files\\Files";
    private static final String pathIndex="C:\\Users\\**\\System Files\\Indexer"; 
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter Displayer = response.getWriter()) {
            
/* Change MAX_RESULTS if you want that your system returns more than 1000 files */
            int MAX_RESULTS = 1000;
            String motsCles=request.getParameter("motsCles");
            Analyzer analyseur = new StandardAnalyzer();
            File indexDir=new File(pathIndex);
            Directory fsDir = FSDirectory.open(indexDir.toPath());
            DirectoryReader ireader = DirectoryReader.open(fsDir);
            IndexSearcher is = new IndexSearcher(ireader);
            QueryParser qp = new QueryParser("contenu", analyseur);
            
            Query requete=null;
            try {
                requete = qp.parse(motsCles);
            } catch (ParseException ex) {
                Logger.getLogger(SearchByKeyWords.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            TopDocs resultats = is.search(requete, MAX_RESULTS); 
            

            Document d=null; 
            List<String> list=new ArrayList<String>();
            List<String> listName=new ArrayList<String>();
            
            for(int i=0; i<resultats.scoreDocs.length; i++) {
	    	int docId = resultats.scoreDocs[i].doc;
	    	d = is.doc(docId);   
                list.add(path+d.get("nom"));
                listName.add(d.get("nom"));
             }
            
            String info=null;
            if (resultats.totalHits>0){
                info="Done";
            }
            
            request.setAttribute("list", list);
            request.setAttribute("listName", listName);
            request.setAttribute("totalHits", resultats.totalHits);
            request.setAttribute("info", info);
            request.getRequestDispatcher("result.jsp").forward(request, response);                                 
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
