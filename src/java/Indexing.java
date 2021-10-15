/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author jabri
 */
@MultipartConfig (fileSizeThreshold = 1024 * 1024 * 1024,
                  maxFileSize = 1024 * 1024 * 20,
                  maxRequestSize = 1024 * 1024 * 50)

@WebServlet(urlPatterns = {"/Indexing"})
public class Indexing extends HttpServlet {

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
0     */
    
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter displayer = response.getWriter()) {
            
            
            /* Folders Creation (If not exist) */
        
            File fsys=null;
            fsys=new File(system);           
            boolean msys=fsys.mkdir();
            
            File f=null;
            f=new File(path);           
            boolean m=f.mkdir();
            
            File fIndex=null;
            fIndex=new File(pathIndex);           
            boolean mIndex=fIndex.mkdir();
            
            /* Upload File and adjust path */
            Part filePart=request.getPart("chemin");
            String fichier=getFileName(filePart);
            String nomFichier=fichier;
            String chemin=path+File.separator+fichier;
            File fileExtension=new File(nomFichier);
            String extension=getFileExtension(fileExtension);
            
            /* Create File (A copy in path) */
            OutputStream out=null;
            InputStream filecontent = null;
            final PrintWriter writer = response.getWriter();               
            out = new FileOutputStream(new File(path + File.separator+ fichier));
            filecontent = filePart.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = filecontent.read(bytes)) != -1) {
           	 out.write(bytes, 0, read);
            	}
            LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", new Object[]{fichier, path});
          
	
            
            /* This is the Indexing Part -- Using Lucene Library */ 

            /* Parsing file (PDF File) */
            File RecupererFile = new File(chemin);
            COSDocument cosDoc = parseDocument(RecupererFile);;
            PDFTextStripper stripper = new PDFTextStripper();
            String docText = stripper.getText(new PDDocument(cosDoc));       
            
            /* Analyzing file using Lucene Analyzer */ 
            /* Extrating Key words */
            Analyzer analyseur = new StandardAnalyzer();
            Path indexpath = FileSystems.getDefault().getPath(pathIndex);
            Directory index = FSDirectory.open(indexpath);
            IndexWriterConfig config = new IndexWriterConfig(analyseur);
	    IndexWriter write = new IndexWriter(index, config);
           
            //Associating file name to extracted keywords
            Document doc = new Document();
            File leFichierPDF = new File(chemin);
            doc.add(new Field("nom", leFichierPDF.getName(), TextField.TYPE_STORED));
            if (docText != null) {
            	doc.add(new Field("contenu", docText, TextField.TYPE_STORED));
                }
        	
	    /* End */
            
            
            /* Extracting documents meta-data */
            PDDocument pdDoc = PDDocument.load(leFichierPDF);
            PDDocumentInformation docInfo = pdDoc.getDocumentInformation();
            String auteur = docInfo.getAuthor();
            String titre = docInfo.getTitle();
            String motsCles = docInfo.getKeywords();
            String resume = docInfo.getSubject();           
            
            /* Test */
            if ((auteur != null) && !auteur.equals("")) {
            doc.add(new Field("auteur", auteur, TextField.TYPE_STORED));
            }
            
            if ((titre != null) && !titre.equals("")) {
            doc.add(new Field("titre", titre, TextField.TYPE_STORED));
            }
            
            if ((motsCles != null) && !motsCles.equals("")) {
            doc.add(new Field("motsCles", motsCles, TextField.TYPE_STORED));
            }
            
            if ((resume != null) && !resume.equals("")) {
            doc.add(new Field("resume", resume, TextField.TYPE_STORED));
            }
            /* */ 
            
            /* Saving and closing document */
            write.addDocument(doc);
            write.close();
            
	    /* redirecting to add file page */
            request.getRequestDispatcher("addFile.html").forward(request, response);
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
    private String getFileName(final Part part) {
    final String partHeader = part.getHeader("content-disposition");
    LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
    for (String content : part.getHeader("content-disposition").split(";")) {
        if (content.trim().startsWith("filename")) {
            return content.substring(
                    content.indexOf('=') + 1).trim().replace("\"", "");
        }
    }
    return null;
}

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
    
    private static COSDocument parseDocument(File is)
    throws IOException {
    PDFParser parser = new PDFParser(new RandomAccessFile(is, "r"));
    parser.parse();
    return parser.getDocument();
    }
}
