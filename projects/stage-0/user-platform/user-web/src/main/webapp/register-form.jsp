<head>
<jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
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
		<form class="form-signin" method="post">
			<h1 class="h3 mb-3 font-weight-normal">注册</h1>
			<LABEL for="username" class="sr-only">请输入用户名</LABEL>
			<input type="text" id="username" name="username" class="form-control" placeholder="请输入用户名" required autofocus>
			<label for="password" class="sr-only">密码</label>
			<input type="password" id="password" name="password" class="form-control" placeholder="请输入密码" required autofocus>
			<label for="email" class="sr-only">请入电子邮件</label>
			<input type="email" id="email" name="email" class="form-control" placeholder="请输入电子邮件" required autofocus>
			<label for="phoneNumber" class="sr-only">手机号</label>
			<input type="text" id="phoneNumber" name="phoneNumber" class="form-control" placeholder="请输入手机号" required autofocus>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
			<p class="mt-5 mb-3 text-muted">&copy; 2017-2021</p>
		</form>
	</div>
</body>