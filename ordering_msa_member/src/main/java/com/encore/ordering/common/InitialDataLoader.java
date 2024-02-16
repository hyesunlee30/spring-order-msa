package com.encore.ordering.common;

import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.domain.Role;
import com.encore.ordering.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    //CommandLineRunner를 통해 스프링빈으로 등록되는 시점에 run메서드 실행

    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(MemberRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(repository.findByEmail("admin@test.com").isEmpty()){
            Member adminMember = Member.builder()
                    .name("admin")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .role(Role.ADMIN)
                    .build();
            repository.save(adminMember);
        }
    }
}