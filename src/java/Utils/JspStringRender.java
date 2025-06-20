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
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletOutputStream; // Import cho ServletOutputStream
import jakarta.servlet.WriteListener; // Import cho WriteListener

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
                        return true; // Triển khai đơn giản
                    }

                    @Override
                    public void setWriteListener(WriteListener writeListener) {
                        // Chưa triển khai cho ví dụ đơn giản này
                    }

                    @Override
                    public void write(int b) throws IOException {
                        baos.write(b);
                    }

                    // Bạn có thể cần thêm một phương thức để lấy các byte đã thu thập
                    public byte[] getBytes() {
                        return baos.toByteArray();
                    }
                };
            }
            // Thêm các phương thức khác nếu JSP của bạn có thể gọi chúng và chúng commit phản hồi,
            // ví dụ: setBufferSize, flushBuffer, sendRedirect, v.v.
            // Đối với việc render JSP đơn giản sang chuỗi, getWriter thường là đủ.
            @Override
            public void sendRedirect(String location) throws IOException {
                // Ngăn chặn chuyển hướng thực tế khi render sang chuỗi
                // Bạn có thể ghi log hoặc ném ngoại lệ nếu chuyển hướng không mong muốn
            }

            @Override
            public void setHeader(String name, String value) {
                // Ngăn chặn việc đặt các header có thể commit phản hồi gốc
            }

            @Override
            public void addHeader(String name, String value) {
                // Ngăn chặn việc thêm các header có thể commit phản hồi gốc
            }
            // ... và cứ như vậy đối với các phương thức khác có thể commit phản hồi
        };

        RequestDispatcher dispatcher = request.getRequestDispatcher(jspPath);
        dispatcher.include(request, responseWrapper); // Sử dụng include thay vì forward

        // Nếu JSP của bạn ghi nội dung nhị phân, bạn sẽ cần xử lý điều đó thông qua getOutputStream()
        // Đối với HTML/văn bản, StringWriter thường là đủ.
        return stringWriter.toString();
    }
}