import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.common.ApiException;
import com.example.common.JsonResponse;

public class ExceptionHandlingFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);
        } catch (ApiException e) {
            JsonResponse.error(req, resp, e.getStatus(), e.getCode(), e.getMessage());
        } catch (Exception e) {
            // 예상 못한 에러는 500으로 통일
            JsonResponse.error(req, resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", e.getMessage());
        }
    }
    
}