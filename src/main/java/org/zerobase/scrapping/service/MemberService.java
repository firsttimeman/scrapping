package org.zerobase.scrapping.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerobase.scrapping.exception.impl.AlreadyExistUserException;
import org.zerobase.scrapping.model.Auth;
import org.zerobase.scrapping.persist.entity.MemberEntity;
import org.zerobase.scrapping.persist.MemberRepository;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
//UserDetailsService 확인하기


    private final PasswordEncoder passwordEncoder; // 패스워드 인코더
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("could not find user -> " + username) );

    }

    public MemberEntity register(Auth.SignUp member) {
        boolean exists = memberRepository.existsByUsername(member.getUsername());

        if(exists) {
            throw new AlreadyExistUserException();
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        MemberEntity result = memberRepository.save(member.toEntity());


        return result;
    }

    public MemberEntity authenticate(Auth.SignIn member) {

        MemberEntity user = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));

        if(!passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        return user;
    }

}
