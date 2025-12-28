<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>Login</title>
</head>
<body>
  <h1>Login</h1>

  <%
    String error = request.getParameter("error");
    if ("1".equals(error)) {
  %>
    <p style="color:red;">아이디/비밀번호가 올바르지 않습니다.</p>
  <%
    }
    String next = request.getParameter("next");
    if (next == null) next = "";
  %>

  <form method="post" action="<%= request.getContextPath() %>/login">
    <input type="hidden" name="next" value="<%= next %>" />

    <div>
      <label>Username</label><br/>
      <input type="text" name="username" />
    </div>

    <div style="margin-top: 8px;">
      <label>Password</label><br/>
      <input type="password" name="password" />
    </div>

    <div style="margin-top: 12px;">
      <button type="submit">로그인</button>
    </div>
  </form>
</body>
</html>
