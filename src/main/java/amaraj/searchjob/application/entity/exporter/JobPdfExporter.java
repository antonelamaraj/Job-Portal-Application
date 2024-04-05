package amaraj.searchjob.application.entity.exporter;

import amaraj.searchjob.application.dto.jobdto.JobDTO;
import amaraj.searchjob.application.dto.PageDTO;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;

public class JobPdfExporter extends AbstractExporter{
    public void export(PageDTO<JobDTO> jobList, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "job/pdf", ".pdf");

        Document document = new Document(PageSize.A2);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph("List of Jobs", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        //krijojme tabelen //ose rreshtat
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{1.0f, 2.0f, 3.5f, 2.0f, 1.2f, 1.1f, 1.2f, 1.1f});

        writeTableHeader(table);
        writeTableData(table, jobList);

        document.add(table);
        document.close();

    }

    private void writeTableData(PdfPTable table, PageDTO<JobDTO> jobList) {
        for (JobDTO job: jobList.getContent()){
            table.addCell(String.valueOf(job.getId()));
            table.addCell(job.getTitle());
            table.addCell(job.getDescritpion());
            table.addCell(job.getJobType().name());
            table.addCell(job.getExperienceLevel().name());
            table.addCell(job.getLocation().name());
            table.addCell(String.valueOf(job.getSalary()));
            table.addCell(job.getDatePosted().toString());
        }

    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(12);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Titles", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("JobType", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Experience Level", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Location", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Salary", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Date Posted", font));
        table.addCell(cell);

    }

}
