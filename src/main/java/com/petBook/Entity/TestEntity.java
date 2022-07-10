package com.petBook.Entity;

import lombok.*;

import javax.persistence.*;

// 해당 클래스의 인스턴스들이 JPA로 관리되는 엔티티 객체라는 것을 의미한다.
// 해당 어노테이션이 붙은 클래스는 오셥에 따라 자동으로 테이블 생성이 가능하고 클래스의 멤버 변수에 따라 자동으로 컬럼들도 생성된다
@Entity
@Table(name = "test_tb")
// @Entity 어노테이션과 같이 사용할 수 있는 어노테이션 -> DB 상에서 엔티티 클래스를 어떤 테이블로 생성할 것인지에 대한 정보를 담기 위함
@Data
@Builder
@AllArgsConstructor // @Builder 를 이용하기 위해서 항상 같이 처리해야 컴파일 에러가 발생하지 않음
@NoArgsConstructor // @Builder 를 이용하기 위해서 항상 같이 처리해야 컴파일 에러가 발생하지 않음
public class TestEntity {
    @Id //@Entity 가 붙은 클래스는 PK에 해당하는 특정 필드를 @Id로 지정해야 함
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column // 필드 생성.. 컬럼으로 생성하지 않는 경우 @Transient 사용
    private String memo;
}
