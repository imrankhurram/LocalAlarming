package com.nextcontrols.pagebeans;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.event.CloseEvent;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

@ManagedBean(name="exporter")
public class DataExporterBean {
	private static boolean rendered=false;
	private static String fileName="";
	private boolean temp;
	private int count=0;
	private static String pageTitle;
	private static List<String> columnNames = new ArrayList<String>();
	private static List<String> columnValues = new ArrayList<String>();
	private Set<String> set= new LinkedHashSet<String>();
	private static Map<String, String> headerContent = new LinkedHashMap<String, String>();
	private static Map<String,Float> tabColumnsWithSize = new LinkedHashMap<String, Float>();
	private ByteArrayOutputStream baos;
	private static String logo;


	
	public void preProcessor(Object document){
		setRendered(true); //renders the columns that are hidden in the datatable
	}
	public void generatePDF() throws DocumentException, MalformedURLException, IOException, ParseException{
		count++;
	//	setTemp(true);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		setRendered(true);
		Document pdf= new Document(PageSize.A4, 50,50,50,50);
		baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(pdf, baos);
		String after= "\n" +"Next control Systems Ltd. 6, Farnborough Business Centre, Eelmoor Road, Farnborough, Hampshire, GU14 7XA" 
        + "Telephone.. 01252 406398 Fax.. 01252 406401 Bureau.. 01252 406339 Fax.. 01252 406402 " +
        "Tech Support Email.. engineersupport@nextcontrols.com" +"\n" + "Registered No.. 2540171 England";
        String before = "Commercial in confidence- Copyright @ Next Control Systems Ltd 2012";
        String pageNumber = "\n"+"Page ";
        Font footer_font = new Font(Font.TIMES_ROMAN, 8); 
        HeaderFooter pgFooter1 = new HeaderFooter(new Phrase(before+after+pageNumber, footer_font), true );
        pgFooter1.setBorder(Rectangle.NO_BORDER);
        pgFooter1.setBorder(Rectangle.TOP);
        pgFooter1.setAlignment(Element.ALIGN_CENTER);
        pdf.setFooter(pgFooter1);
        
        Image nextLogo=Image.getInstance(logo);
		nextLogo.setAlignment(Image.RIGHT);
		nextLogo.scalePercent(50);
		HeaderFooter header = new HeaderFooter(new Phrase(new Chunk(nextLogo, 0, -16)), false);
		header.setBorder(Rectangle.NO_BORDER);
		header.setAlignment(Element.ALIGN_RIGHT);
        pdf.setHeader(header);
        //writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
        //writer.setPageEvent(new PdfListener());
        pdf.open();
		pdf.add(new Paragraph());
//		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//		String logo = servletContext.getRealPath("") + File.separator + "images" + File.separator + "nextCntrlsHeader.jpg";
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		//String page = ectx.getRequestServletPath();
		HttpSession session = (HttpSession)ectx.getSession(false);
		pdf.add(Chunk.NEWLINE);
	    

		Font font = new Font(Font.TIMES_ROMAN, 10);
		Font font1 = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
		
		
		pdf.addAuthor(session.getAttribute("name").toString());
     	LineSeparator line = new LineSeparator();
		pdf.add(line);
		this.set= headerContent.keySet();
		java.util.Iterator<String> itr = set.iterator(); 
		
		while(itr.hasNext()) {
			String itr_val = itr.next(); 
			Chunk chunk_key = new Chunk(itr_val, font);
			pdf.add(chunk_key);
		    String s_val = headerContent.get(itr_val);
			Chunk chunk_val = new Chunk(s_val, font1);
			pdf.add(chunk_val);
			pdf.add(Chunk.NEWLINE);
			}
		
		pdf.add(Chunk.NEWLINE);
		pdf.add(line);
		Font fontForTitle = new Font(Font.TIMES_ROMAN, 11);
		Chunk pgTitle=new Chunk(getPageTitle().toUpperCase() ,fontForTitle);
		Paragraph ph = new Paragraph(pgTitle);
		ph.setAlignment(Element.ALIGN_LEFT);
		pdf.add(ph);
		pdf.add(Chunk.NEWLINE);	
		List<String> columnValues = new ArrayList<String>();
		columnValues = getColumnValues();
		float[] columnWidths = new float[tabColumnsWithSize.size()];
		Set<String> set = tabColumnsWithSize.keySet();
		java.util.Iterator<String> itr1 = set.iterator();
		int j=0;
		while(itr1.hasNext()) {
			String s = itr1.next();
			columnWidths[j]=tabColumnsWithSize.get(s);
			j++;		
		}
		PdfPTable table = new PdfPTable(columnWidths);
		table.setWidthPercentage(95);
		table.setHeaderRows(1);
		table.getDefaultCell().setPadding(5);
	
		columnValues =  this.getColumnValues();
	    Font fontForCol = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);
	    java.util.Iterator<String> it = set.iterator();
	    PdfPCell cell;
	   
	    while(it.hasNext()) {
	    	cell = new PdfPCell(new Phrase(it.next(), fontForCol));
	    	cell.setPadding(3);
	    	table.addCell(cell);
	    }
	    for(int i=0; i<columnValues.size(); i++) {
	    	Font fontForData = new Font(Font.TIMES_ROMAN, 8);
	    	table.addCell(new Phrase(columnValues.get(i),fontForData));	
	    	
	    }
	    pdf.add(table);
	    pdf.close();
		writePDFToResponse(((HttpServletResponse) facesContext.getExternalContext().getResponse()), baos, fileName);
		setRendered(false);
		facesContext.responseComplete();	
	}
	
	

	public void writePDFToResponse(HttpServletResponse response,  ByteArrayOutputStream baos, String fileName ) throws IOException, DocumentException {
		response.setContentType("application/pdf");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must revalidate, post-check=0, pre-check=0");
		response.setHeader("pragma", "public");
		response.setHeader("Content-disposition", "attachment;filename="+getFileName()+".pdf");
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream(); 
		baos.writeTo(out);
		baos.flush();
	}


	
	
	public void preProcessPDF(Object document) throws DocumentException, MalformedURLException, IOException, ParseException{
		setRendered(true);
		Document pdf= new Document(PageSize.A4);
		pdf=(Document) document;
		pdf.open();
		pdf.add(new Paragraph());
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String logo = servletContext.getRealPath("") + File.separator + "images" + File.separator + "nextCntrlsHeader.jpg";
		Image nextLogo=Image.getInstance(logo);
		
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		String page = ectx.getRequestServletPath();
		HttpSession session = (HttpSession)ectx.getSession(false);
		
		pdf.add(nextLogo);
		pdf.add(Chunk.NEWLINE);
		pdf.add(Chunk.NEWLINE);
		Paragraph paragraph=new Paragraph();
		paragraph.setAlignment(Element.ALIGN_CENTER);
		pdf.add(paragraph);
		Font font = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
		Chunk export=new Chunk("Data Exported From Page: " + page,font);
		
		pdf.addAuthor(session.getAttribute("name").toString());
		
		Chunk name=new Chunk("Created by: " + session.getAttribute("name").toString(),font);
		
		Calendar cal=Calendar.getInstance();
		Date now=cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String parsedDate=dateFormat.format(now);
		String parsedTime=timeFormat.format(now);
		Chunk currDate=new Chunk("Date: " + parsedDate,font);
		Chunk currTime=new Chunk("Time: " + parsedTime,font);
		
		pdf.add(export);
		pdf.add(Chunk.NEWLINE);
		pdf.add(name);
		pdf.add(Chunk.NEWLINE);
		pdf.add(currDate);
		pdf.add(Chunk.NEWLINE);
		pdf.add(currTime);
		pdf.add(Chunk.NEWLINE);
	}
	
	public void preProcessPDFSite(Object document) throws DocumentException, MalformedURLException, IOException, ParseException{
		setRendered(true);
		Document pdf= new Document(PageSize.A4);
		pdf=(Document) document;
		pdf.open();
		pdf.add(new Paragraph());
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String logo = servletContext.getRealPath("") + File.separator + "images" + File.separator + "nextCntrlsHeader.jpg";
		Image nextLogo=Image.getInstance(logo);
		
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		String page = ectx.getRequestServletPath();
		HttpSession session = (HttpSession)ectx.getSession(false);
		pdf.addAuthor(session.getAttribute("name").toString());
		pdf.add(nextLogo);
		pdf.add(Chunk.NEWLINE);
		pdf.add(Chunk.NEWLINE);
		Paragraph paragraph=new Paragraph();
		paragraph.setAlignment(Element.ALIGN_CENTER);
		pdf.add(paragraph);
		Font font = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
		
		Chunk export=new Chunk("Data Exported From Page: " + page,font);
		Chunk site=new Chunk("Site Code: " + session.getAttribute("sitecode").toString(),font);
		Chunk name=new Chunk("Created by: " + session.getAttribute("name").toString(),font);

		
		Calendar cal=Calendar.getInstance();
		Date now=cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String parsedDate=dateFormat.format(now);
		String parsedTime=timeFormat.format(now);
		Chunk currDate=new Chunk("Date: " + parsedDate,font);
		Chunk currTime=new Chunk("Time: " + parsedTime,font);
		
		pdf.add(export);
		pdf.add(Chunk.NEWLINE);
		pdf.add(site);
		pdf.add(Chunk.NEWLINE);
		pdf.add(name);
		pdf.add(Chunk.NEWLINE);
		pdf.add(currDate);
		pdf.add(Chunk.NEWLINE);
		pdf.add(currTime);
		pdf.add(Chunk.NEWLINE);
	}
	
	public void postProcessor(Object document){
		setRendered(false);
	}

	public void setRendered(boolean Rendered) {
		rendered = Rendered;
	}

	public boolean isRendered() {
		return rendered;
	}
	public void setFileName(String fileName) {
		DataExporterBean.fileName=fileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setTemp(boolean temp) {
		this.temp=temp;
	}
	public boolean getTemp() {
		if(this.count>0 && this.temp== false) {
			setTemp(true);
		}
		return temp;
	}
	public void setPageTitle(String pageTitle) {
		DataExporterBean.pageTitle= pageTitle;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setColumnNames(List<String> columnNames) {
		DataExporterBean.columnNames= columnNames;
	}
	public List<String> getColumnNames() {
		return columnNames;
	}
	public void setColumnValues(List<String> columnValues) {
		DataExporterBean.columnValues=columnValues;
	}
	public List<String> getColumnValues() {
		return columnValues;
	}
	public void setHeaderContent(Map<String, String> headerContent) {
		DataExporterBean.headerContent=headerContent;
	}
	public Map<String, String> getHeaderContent() {
		return headerContent;
	}
	public void setTabColumnsWithSize(Map<String,Float> tabColumnsWithSize) {
		DataExporterBean.tabColumnsWithSize= tabColumnsWithSize;
	}
	public Map<String, Float> getTabColumnsWithSize() {
		return tabColumnsWithSize;
	}
	public void setLogo(String logo) {
		DataExporterBean.logo=logo;
	}
	public String getLogo() {
		return logo;
	}
	public void closeListener(CloseEvent e){
		this.count=0;
		setTemp(false);
	}

}
	