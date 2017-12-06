package com.dream.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class PhoneSystemServlet
 */
@WebServlet("/PhoneSystemServlet")
public class PhoneSystemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public PhoneSystemServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		
		
		/*PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>HelloWorld</title></head>");
		out.println("<body>");
		out.println("<h1>HELLO WORLD</h1>");
		out.println("</body>");
		out.println("</html>");
		out.close();*/
		
		String deviceid = new String(request.getParameter("deviceid").getBytes("ISO-8859-1"), "UTF-8");
		String imsi = new String(request.getParameter("imsi").getBytes("ISO-8859-1"), "UTF-8");
		String deviceName = new String(request.getParameter("deviceName").getBytes("ISO-8859-1"), "UTF-8");
		String brandName = new String(request.getParameter("brandName").getBytes("ISO-8859-1"), "UTF-8");
		String productName = new String(request.getParameter("productName").getBytes("ISO-8859-1"), "UTF-8");
		String manufactuereName = new String(request.getParameter("manufactuereName").getBytes("ISO-8859-1"), "UTF-8");
		
		String token = new String(request.getParameter("token").getBytes("ISO-8859-1"), "UTF-8");
		String strDecode = decode(token);
		
		System.out.println("dengying token=" + token +",strDecode="+strDecode);
		//getServletContext().log("日志日志顶呱呱");
		
		add(deviceid, imsi, deviceName, brandName, productName, manufactuereName);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	public void add(String deviceid, String imsi, String deviceName, String brandName, String productName, String manufactuereName) {

		Connection conn = null;

		// Statement对象
		Statement st = null;

		// 驱动
		String driver = "com.mysql.jdbc.Driver";

		// 连接数据库的URL
		String url = "jdbc:mysql://localhost:3306/phonesystem?user=root&password=1234&useUnicode=true&characterEncoding=UTF-8";

		String sql = "insert into phone (deviceid,imsi,deviceName,brandName,productName,manufactuereName) values('" + deviceid + "','" + imsi + "','" + deviceName + "','" + brandName + "','" + productName + "','" + manufactuereName + "')";

		try {
			Class.forName(driver);
			
			conn = (Connection) DriverManager.getConnection(url);

			st = (Statement) conn.createStatement();

			st.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public String decode(String str) {
		byte[] result = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			result = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String decryptingCode = new String(result); 
		
		return decryptingCode;
	}

}
