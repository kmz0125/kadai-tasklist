<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<label for="content_tsk">タスク</label><br/>
<input type="text" name="content" id="content_tsk" value="${tasks.content}"/>
<br/><br/>

<input type="hidden" name="_token" value="${_token }"/>
<button type="submit">投稿</button>