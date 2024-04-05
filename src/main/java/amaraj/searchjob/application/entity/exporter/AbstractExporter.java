package amaraj.searchjob.application.entity.exporter;

import jakarta.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractExporter {
    protected void setResponseHeader(HttpServletResponse response, String  contenttype, String extension) {
//
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        String timestamp = dateFormatter.format(new Date());
//        String fileName = "companies_" + timestamp + extension;
//
//        response.setContentType(contenttype);
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment ; fileName=" + fileName;
//        response.setHeader(headerKey, headerValue);
    }
}
