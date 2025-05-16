package com.knowledgeVista.User.SecurityConfiguration;


import com.knowledgeVista.User.Repository.MuserRepositories;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AccessCheckAspect {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MuserRepositories muserRepository;

    @Autowired
    private CacheManager cacheManager;

    @Around("@annotation(com.knowledgeVista.User.SecurityConfiguration.CheckAccessAnnotation)")
    public Object checkAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader("Authorization");
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new UnauthorizedAccessException("Invalid Token");
        }

        String institution = jwtUtil.getInstitutionFromToken(token);
        if (institution == null) {
            throw new UnauthorizedAccessException("Invalid Token");
        }

        String cacheKey = "institutionBlocked::" + institution;
        Boolean isBlocked = cacheManager.getCache("institutionBlocked") != null
                ? cacheManager.getCache("institutionBlocked").get(cacheKey, Boolean.class)
                : null;

        if (isBlocked == null) {
            boolean adminActive = muserRepository.getactiveResultByInstitutionName("ADMIN", institution);
            if (cacheManager.getCache("institutionBlocked") != null) {
                cacheManager.getCache("institutionBlocked").put(cacheKey, !adminActive);
            }
            if (!adminActive) {
                throw new UnauthorizedAccessException("Institution Blocked");
            }
        } else if (isBlocked) {
            throw new UnauthorizedAccessException("Institution Blocked");
        }

        return joinPoint.proceed();
    }
}

