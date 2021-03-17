<head>
<jsp:directive.include file="/WEB-INF/jsp/prelude/header.jspf" />
	<title>注册</title>
    <style>
      .bd-placeholder-img {
        font-size: 1.125rem;
        text-anchor: middle;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
      }

      @media (min-width: 768px) {
        .bd-placeholder-img-lg {
          font-size: 3.5rem;
        }
      }
    </style>
</head>
<body>
	<div class="container">
		<h1>
			注册失败
		</h1>

		<p>
			<%
			  String errors=(String) request.getAttribute("errors");
			  out.println(errors);
			%>
		</p>
		<a href="/register">注册</a>
	</div>
</body>