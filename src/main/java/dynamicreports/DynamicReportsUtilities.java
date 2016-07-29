import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class DynamicReportsUtilities {
  public static Map GetMapFromString(String json) throws IOException {
    return new ObjectMapper().readValue(json,new TypeReference<HashMap<String,String>>(){});
  }

  public static File[] GetFileNamesFromDirectory(String path, final String extension) {
    return new File(path).listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(extension);
      }
    });
  }
}
