package controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Tasks;
import models.validators.TasksValidator;
import utils.DBUtil;
/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token=request.getParameter("_token");
        if(_token!=null&&_token.equals(request.getSession().getId())) {
            EntityManager em=DBUtil.createEntityManager();
            em.getTransaction().begin();

            Tasks t=new Tasks();

            String content=request.getParameter("content");
            t.setContent(content);

            Timestamp curreTime=new Timestamp(System.currentTimeMillis());
            t.setCreated_at(curreTime);
            t.setUpdated_at(curreTime);

            //バリデーションを実行してエラーがあったら新規登録のフォームに戻る
            List<String> errors=TasksValidator.validate(t);
            if(errors.size()>0) {
                em.close();

                //フォームに初期値を設定、さらにエラーメッセージを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("tasks", t);
                request.setAttribute("errors", errors);

                RequestDispatcher rd=request.getRequestDispatcher("WEB-INF/views/tasks/new.jsp");
                rd.forward(request, response);
            }else {
                //データベースに保存
                em.persist(t);
                em.getTransaction().commit();
                em.close();

            }

            //indexのページにリダイレクト
            response.sendRedirect(request.getContextPath()+"/index");
        }
    }

}
