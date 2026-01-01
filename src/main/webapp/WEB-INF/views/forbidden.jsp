<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>403 Forbidden</title>
</head>
<body>
  <h1>403 Forbidden</h1>
  <p>이 페이지에 접근할 권한이 없습니다.</p>

  <p>
    <a href="<%= request.getContextPath() %>/posts/page">돌아가기</a>
  </p>
</body>
</html>
