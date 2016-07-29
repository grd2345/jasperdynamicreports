import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.FilenameUtils;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class DynamicReportsTemplateFactory {
  final String _REPORT_PATH = "reports/";
  final String _REPORT_EXTENSION = ".jasper";

  public Map GetAllReports() throws JRException {
    Map ret = new HashMap<String,JasperReport>();
    File[] reportTemplates = DynamicReportsUtilities.GetFileNamesFromDirectory(_REPORT_PATH,_REPORT_EXTENSION);

    for (int i = 0; i < reportTemplates.length; i++) {
     File file = reportTemplates[i];
     String key = FilenameUtils.removeExtension(file.getName());
     JasperReport report = (JasperReport)JRLoader.loadObject(new File(file.getPath()));

     ret.put(key,report);

     System.err.println();
    }

    return ret;
  }
}