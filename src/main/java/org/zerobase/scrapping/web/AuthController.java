package org.zerobase.scrapping.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.scrapping.model.Auth;
import org.zerobase.scrapping.persist.entity.MemberEntity;
import org.zerobase.scrapping.security.TokenProvider;
import org.zerobase.scrapping.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Auth.SignUp request) {
        MemberEntity result = this.memberService.register(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Auth.SignIn request) {
        MemberEntity entity = this.memberService.authenticate(request);
        String token = this.tokenProvider.generateToken(entity.getUsername(), entity.getRoles());
        log.info("user login -> " + request.getUsername());
        return ResponseEntity.ok(token);
    }

}