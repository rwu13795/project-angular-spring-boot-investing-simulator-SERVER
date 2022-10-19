package com.raywu.investingsimulator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JWTFilter extends OncePerRequestFilter {
    // to let the filter handle exception and make the response directly
    //	@Autowired
    //	@Qualifier("handlerExceptionResolver")
    //	private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Map<String, String[]> query = request.getParameterMap();


        System.out.println("------- query hello: " + query.get("hello")[0]);
        System.out.println("------- query abc: " + query.get("abc")[0]);

        System.out.println("------- in filter: " + request.getQueryString());


        // extract the body from the request and map it
        try {
            byte[] inputStreamBytes = StreamUtils.copyToByteArray(request.getInputStream());

            @SuppressWarnings("unchecked")
            Map<String, String> jsonRequest = new ObjectMapper().readValue(inputStreamBytes, Map.class);
            String test = jsonRequest.get("test");
            String xyz = jsonRequest.get("xyz");

            // other code
            System.out.println("------- body@ 'test': " + test);
            System.out.println("------- body@ 'xyz': " + xyz);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!query.get("abc")[0].equals("xyz")) {

            // custom exception handler
            // throw new TestException("------ In custom exception handler ------");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return !path.startsWith("/api/secret-page");
    }
}
