<%@ page language="java" %>
<%@ page contentType="text/html;charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<style type="text/css">
body{font-family:Georgia, "Times New Roman", Times, serif; font-size:12px; background-color: #FFC}
.input1{width:100px; height:25px; background-color: #09F; color:#FFF ; border:1px}
.font1{color:#F00; font-size:12px}
.font2{color:#000; font-size:12px}
.font3{color:#000; font-size:14px}
.font4{color:#000; font-size:18px}
</style>
<jsp:useBean id="Mysql" class="com.bar.safe.Mysql" scope="page"></jsp:useBean>

<title>��վ��װ</title><table width="960" height="334" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="76" align="center" class="font2"><p><font class="font3">��վ��װ</font></p>
<p>1��mysql���ݵ��û�������Ϊroot�������Ϊ123456</p>
	  <p>2����MySQL�д���һ����Ϊ<font class="font4"><b>bar</b></font>�����ݿ�!</p>
	  <p>3���뽫��վ��Ŀ¼�µ�bar.sql�ļ��ŵ�C�̣��ٵ���ָ����ݼ���!</p>
	  <p>4��Ϊ��ȷ����ȫ���밲װ��ɺ�ɾ����վ��Ŀ¼�µ�install.jsp�ļ�!</p>
	  <p>5����վ��̨��½��ַΪ<a href="http://localhost/bar/admin/login.jsp">http://localhost/bar/admin/login.jsp</a>����<a href="http://localhost/admin/login.jsp">http://localhost/admin/login.jsp</a></p>
	  <p>5����̨��½�û��������붼Ϊadmin</p>
	  <p>
<font class="font1">
	    <%
String Bx = request.getParameter("b");
if(Bx != null){
	Mysql.load();
	out.print("���ݿⰲװ�ɹ�");
	}

%></font>
	  </p>
    <p>&nbsp; </p></td>
  </tr>
  <tr>
    <td height="49" align="center"><form action="install.jsp" method="post" name="from2">
      <input type="hidden" name="b" value="Yes" />
      <input name="submit" type="submit" class="input1" value="��װ���ݿ�" />
    </form></td>
  </tr>
  <tr>
    <td align="center" class="font2"><p>�����װʧ���ˣ������ø�Ŀ¼�µ�create_bar.sql���жԱ�Ĵ�������Ҫ�������£�</p>
      <p><font class="font1">1</font>����������ʾ��������mysql&nbsp;&nbsp;&nbsp;&nbsp;<font class="font1">2</font>������create database bar;&nbsp;&nbsp;&nbsp;&nbsp;<font class="font1">3</font>������reate_bar.sql���������SQL���ճ����������ʾ�������enter������</p>
<p>&nbsp;</p></td>
  </tr>
</table>
