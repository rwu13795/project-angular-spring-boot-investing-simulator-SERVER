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

        // ----- (1) ----- //
        request.setAttribute("JWT","setAttribute-testing-text");

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

/*
// ----- (1) ----- //
After validating the JWT and extract the email and user_id from the JWT, I can put this data
inside the "request" (the same as Node middleware), and pass this data to the controller

For example, the JWT filter is applied on the "/portfolio" route, I can check the JWT and extract
the user's info, then pass to the portfolio controller.

If the JWT is not valid, just respond with
    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT")
here in the filter. So that the portfolio controller won't be reached with an invalid JWT

If the JWT is valid, I can get the user's data by using request.getAttribute()
in the controller, and fetch the portfolio detail


----- More Note -----
I can manually map the JSON, such as
    JSON = { "key_1" : "value_1", "key_2" : "value_2" }
in a java Map object by using the "ObjectMapper()" to read the "inputStream"

    try {
        byte[] inputStreamBytes = StreamUtils.copyToByteArray(request.getInputStream());

        @SuppressWarnings("unchecked")
        Map<String, String> jsonRequest = new ObjectMapper().readValue(inputStreamBytes, Map.class);

        String value_1 = jsonRequest.get("key_1");
        String value_2 = jsonRequest.get("key_2");

     } catch (IOException e) {
            throw new RuntimeException(e);
     }

*/