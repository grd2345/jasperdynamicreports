import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.codec.binary.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.*;

public class DynamicReportsServiceHandler {
  Map _reports;

	public DynamicReportsServiceHandler() throws JRException,FileNotFoundException {
    DynamicReportsTemplateFactory templateFactory = new DynamicReportsTemplateFactory();
    _reports = templateFactory.GetAllReports();
	}

	public String GetPDFReport(String reportName,String locale, String timeZone,String rawReportParameters, String jsonData) {
		String ret = "";

		try
    {
      long start = System.currentTimeMillis();
      Map reportParameters = DynamicReportsUtilities.GetMapFromString(rawReportParameters);
      try
      {
        JasperReport report = (JasperReport)_reports.get(reportName);
        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(jsonData.getBytes());
        JsonDataSource ds = new JsonDataSource(jsonDataStream);
        Map parameters = new HashMap();

        parameters.put("REPORT_LOCALE", new java.util.Locale(locale));
        parameters.put("REPORT_TIME_ZONE", java.util.TimeZone.getTimeZone(timeZone));

        for (Object rawKey : reportParameters.keySet()) {
          String key = rawKey.toString();
          parameters.put(key, reportParameters.get(key));
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, ds);

        ret = Base64.encodeBase64String(JasperExportManager.exportReportToPdf(jasperPrint));
      }
      catch(JRException ex) {
        ex.printStackTrace();
        throw new RuntimeException(ex);
      }
      catch(Exception ex) {
        ex.printStackTrace();
        throw new RuntimeException(ex);
      }

      System.err.println("Report creation time : " + (System.currentTimeMillis() - start));
		}
    catch(Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
			
		return ret;
	}

  public String GetHTMLReport(String reportName,String locale, String timeZone,String rawReportParameters, String jsonData) {
    String ret = "";

    try
    {
      JRHtmlExporter exporter = new JRHtmlExporter();
      long start = System.currentTimeMillis();
      Map reportParameters = DynamicReportsUtilities.GetMapFromString(rawReportParameters);

      try 
      {      
        JasperReport report = (JasperReport)_reports.get(reportName);
        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(jsonData.getBytes());
        JsonDataSource ds = new JsonDataSource(jsonDataStream);
        Map parameters = new HashMap();

        parameters.put("REPORT_LOCALE", new java.util.Locale(locale));
        parameters.put("REPORT_TIME_ZONE", java.util.TimeZone.getTimeZone(timeZone));

        for (Object rawKey : reportParameters.keySet()) {
          String key = rawKey.toString();

          parameters.put(key, reportParameters.get(key));
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, ds); 
        ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();
        
        exporter.setParameter(JRHtmlExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
        exporter.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM, reportOutput);

        exporter.exportReport();
        ret = reportOutput.toString();
      }
      catch(JRException ex) {
        ex.printStackTrace();
        throw new RuntimeException(ex);
      }
      catch(Exception ex) {
        ex.printStackTrace();
        throw new RuntimeException(ex);
      }

      System.err.println("Report creation time : " + (System.currentTimeMillis() - start));
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }
    
    return ret;
  }

  public String GetExcelReport(String reportName,String locale, String timeZone,String rawReportParameters, String jsonData) {
    String ret = "";

    try
    {
      JRXlsExporter exporter = new JRXlsExporter();
      long start = System.currentTimeMillis();
      Map reportParameters = DynamicReportsUtilities.GetMapFromString(rawReportParameters);

      try
      {
        JasperReport report = (JasperReport)_reports.get(reportName);
        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(jsonData.getBytes());
        JsonDataSource ds = new JsonDataSource(jsonDataStream);
        Map parameters = new HashMap();

        parameters.put("IS_IGNORE_PAGINATION",true);
        parameters.put("REPORT_LOCALE", new java.util.Locale(locale));
        parameters.put("REPORT_TIME_ZONE", java.util.TimeZone.getTimeZone(timeZone));

        for (Object rawKey : reportParameters.keySet()) {
          String key = rawKey.toString();

          parameters.put(key, reportParameters.get(key));
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, ds);
        ByteArrayOutputStream reportOutput = new ByteArrayOutputStream();

        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, reportOutput);
        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS,Boolean.TRUE);

        exporter.exportReport();
        ret = Base64.encodeBase64String(reportOutput.toByteArray());
      }
      catch(JRException ex){
        ex.printStackTrace();
        throw new RuntimeException(ex);
      }
      catch(Exception ex) {
        ex.printStackTrace();
        throw new RuntimeException(ex);
      }

      System.err.println("Report creation time : " + (System.currentTimeMillis() - start));
    }
    catch(Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex);
    }

    return ret;
  }
}
