// Utils/JspStringRender.java (Ví dụ - bạn có thể đã có cái này hoặc cần tạo nó)
package Utils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener; 

public class JspStringRender {

    public static String renderJspToString(HttpServletRequest request, HttpServletResponse response, String jspPath)
            throws ServletException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response) {
            @Override
            public PrintWriter getWriter() throws IOException {
                return printWriter;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return new ServletOutputStream() {
                    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    @Override
                    public boolean isReady() {
                        return true; 
                    }

                    @Override
                    public void setWriteListener(WriteListener writeListener) {
                     
                    }

                    @Override
                    public void write(int b) throws IOException {
                        baos.write(b);
                    }

           
                    public byte[] getBytes() {
                        return baos.toByteArray();
                    }
                };
            }

            @Override
            public void sendRedirect(String location) throws IOException {

            }

            @Override
            public void setHeader(String name, String value) {
       
            }

            @Override
            public void addHeader(String name, String value) {
                
            }
         
        };

        RequestDispatcher dispatcher = request.getRequestDispatcher(jspPath);
        dispatcher.include(request, responseWrapper);

        return stringWriter.toString();
    }
}