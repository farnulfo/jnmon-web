/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jnmon.jnmonweb;

import com.jnmon.NMon;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author Franck
 */
public class NMonServlet extends HttpServlet {

  /** 
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    String filename = request.getParameter("file");
    File file = new File(filename);
    NMon nmon;
    try {
      nmon = new NMon(file);
    } catch (ParseException ex) {
      Logger.getLogger(NMonServlet.class.getName()).log(Level.SEVERE, null, ex);
      throw new ServletException(ex);
    }

    final DateAxis domainAxis = new DateAxis("Time");
    domainAxis.setVerticalTickLabels(true);
    domainAxis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 1));
    domainAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
    domainAxis.setLowerMargin(0.01);
    domainAxis.setUpperMargin(0.01);
    final ValueAxis rangeAxis = new NumberAxis("Number of active CPU");
    
    rangeAxis.setRange(0, nmon.getNumberOfActiveCPU());

    final StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
    final XYPlot plot = new XYPlot(nmon.createLPARDataSet(), domainAxis, rangeAxis, renderer);

    final JFreeChart chart = new JFreeChart("LPAR : " + file.getCanonicalPath(), plot);
    chart.getLegend().setPosition(RectangleEdge.TOP);


    response.setContentType("image/png");


    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ChartUtilities.writeChartAsPNG(baos, chart, 800, 600);
    baos.close();

    byte[] img = baos.toByteArray();

    ByteArrayInputStream bais = new ByteArrayInputStream(img);

    BufferedOutputStream bos = new BufferedOutputStream(
            response.getOutputStream());
    byte[] input = new byte[1024];
    boolean eof = false;
    while (!eof) {
      int length = bais.read(input);
      if (length == -1) {
        eof = true;
      } else {
        bos.write(input, 0, length);
      }
    }
    bos.flush();
    bais.close();
    bos.close();

  }

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /** 
   * Handles the HTTP <code>GET</code> method.
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
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>
}
